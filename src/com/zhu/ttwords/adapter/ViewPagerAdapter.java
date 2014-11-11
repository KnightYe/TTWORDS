package com.zhu.ttwords.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhu.ttwords.R;
import com.zhu.ttwords.bean.AbstractCommonBean;
import com.zhu.ttwords.bean.RepertoryBean;
import com.zhu.ttwords.bean.WordBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.util.DateUtil;

public class ViewPagerAdapter extends PagerAdapter implements
		OnPageChangeListener {
	public static final int MODE_READY = 0X00;
	public static final int MODE_LEARN = 0x10;
	public static final int MODE_TEST = 0x20;
	public static final int MODE_OVER = 0x30;
	public static final int RESULT_UNDO = 0x00;
	public static final int RESULT_WRONG = 0x01;
	public static final int RESULT_RIGHT = 0x02;

	SharedPreferences sp;
	List<AbstractCommonBean> mData;
	Activity mContext;
	LayoutInflater inflater;
	SparseArray<ViewHolder> views;
	OnClickListener clearListener;
	OnLongClickListener resetListener;
	OnFocusChangeListener focusListener;
	ViewHolder currentViewHolder;
	int index = 1;
	boolean hasNextStatus = false;

	public ViewPagerAdapter(Activity context, List<AbstractCommonBean> mData) {
		super();
		this.mContext = context;
		sp = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		if (mData != null) {
			this.mData = mData;
		} else {
			this.mData = new ArrayList<AbstractCommonBean>();
		}
		this.inflater = LayoutInflater.from(mContext);
		this.views = new SparseArray<ViewPagerAdapter.ViewHolder>();
		this.resetListener = new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (hasNextStatus) {
					return true;
				}
				currentViewHolder.mode = MODE_READY;
				currentViewHolder.result = RESULT_RIGHT;
				updateStatusAndMode();
				return true;
			}
		};
		this.clearListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (hasNextStatus || currentViewHolder.result == RESULT_RIGHT) {
					return;
				}
				currentViewHolder.result = RESULT_UNDO;
				updateStatusAndMode();
			}
		};
		this.focusListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasNextStatus || !hasFocus) {
					return;
				}
				if (v == currentViewHolder.content) {
					if (currentViewHolder.result == RESULT_RIGHT) {
						currentViewHolder.mode += 0x10;
						currentViewHolder.result = RESULT_UNDO;
					} else if (currentViewHolder.result == RESULT_WRONG) {
						currentViewHolder.result = RESULT_UNDO;
					} else {
						CharSequence question = currentViewHolder.content
								.getHint().toString().trim();
						CharSequence answer1 = currentViewHolder.content
								.getText().toString().trim();
						CharSequence answer2 = currentViewHolder.test.getText()
								.toString().trim();
						currentViewHolder.result = (question.equals(answer1)
								|| (question.equals(answer2)) ? RESULT_RIGHT
								: RESULT_WRONG);

					}
				} else if (v == currentViewHolder.test) {
					if (currentViewHolder.result == RESULT_RIGHT) {
						currentViewHolder.mode += 0x10;
					} else if (currentViewHolder.result == RESULT_WRONG) {
						currentViewHolder.result = RESULT_UNDO;
					} else {
						CharSequence question = currentViewHolder.content
								.getHint().toString().trim();
						CharSequence answer1 = currentViewHolder.content
								.getText().toString().trim();
						CharSequence answer2 = currentViewHolder.test.getText()
								.toString().trim();
						currentViewHolder.result = (question.equals(answer1)
								|| (question.equals(answer2)) ? RESULT_RIGHT
								: RESULT_WRONG);
					}
				}
				updateStatusAndMode();
			}
		};
	}

	private void updateStatusAndMode() {
		this.hasNextStatus = true;
		switch (currentViewHolder.mode + currentViewHolder.result) {
		case 0x00:
		case 0x01:
		case 0x02:
			currentViewHolder.down.setVisibility(View.VISIBLE);
			currentViewHolder.content.setText(currentViewHolder.content
					.getHint());
			currentViewHolder.test.getText().clear();
			currentViewHolder.test.setFocusable(true);
			currentViewHolder.test.setFocusableInTouchMode(true);
			currentViewHolder.test.requestFocus();
			currentViewHolder.test.setAlpha(0);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_attention);
			currentViewHolder.mark.setVisibility(View.INVISIBLE);
			break;
		case 0x10:
			currentViewHolder.down.setVisibility(View.VISIBLE);
			currentViewHolder.content.getText().clear();
			currentViewHolder.content.requestFocus();
			currentViewHolder.test.getText().clear();
			currentViewHolder.test.setFocusable(true);
			currentViewHolder.test.setFocusableInTouchMode(true);
			currentViewHolder.test.setAlpha(0);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_attention);
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			break;
		case 0x11:
			currentViewHolder.down.setVisibility(View.VISIBLE);
			currentViewHolder.test.getText().clear();
			currentViewHolder.test.setFocusable(true);
			currentViewHolder.test.setFocusableInTouchMode(true);
			currentViewHolder.test.requestFocus();
			currentViewHolder.test.setAlpha(0);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_wrong);
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			break;
		case 0x12:
			currentViewHolder.down.setVisibility(View.VISIBLE);
			currentViewHolder.test.getText().clear();
			currentViewHolder.test.setFocusable(true);
			currentViewHolder.test.setFocusableInTouchMode(true);
			currentViewHolder.test.requestFocus();
			currentViewHolder.test.setAlpha(0);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_right);
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			break;
		case 0x20:
			currentViewHolder.down.setVisibility(View.INVISIBLE);
			currentViewHolder.content.getText().clear();
			currentViewHolder.test.getText().clear();
			currentViewHolder.test.setFocusable(true);
			currentViewHolder.test.setFocusableInTouchMode(true);
			currentViewHolder.test.requestFocus();
			currentViewHolder.test.setAlpha(1);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_attention);
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			break;
		case 0x21:
			currentViewHolder.down.setVisibility(View.INVISIBLE);
			currentViewHolder.content.requestFocus();
			currentViewHolder.test.getText().clear();
			currentViewHolder.test.setFocusable(true);
			currentViewHolder.test.setFocusableInTouchMode(true);
			currentViewHolder.test.setAlpha(1);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_wrong);
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			break;
		case 0x22:
			currentViewHolder.down.setVisibility(View.INVISIBLE);
			currentViewHolder.content.requestFocus();
			currentViewHolder.test.setFocusable(true);
			currentViewHolder.test.setFocusableInTouchMode(true);
			currentViewHolder.test.setAlpha(1);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_right);
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			break;

		case 0x30:
		case 0x31:
		case 0x32:
			// currentViewHolder.down.setVisibility(View.INVISIBLE);
			currentViewHolder.down.setVisibility(View.INVISIBLE);
			currentViewHolder.content.requestFocus();
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_right);
			currentViewHolder.test.setAlpha(1);
			currentViewHolder.test.setFocusable(false);
			currentViewHolder.test.setFocusableInTouchMode(false);
			currentViewHolder.test.setAlpha(1);// ������һ����
			if (index < mData.size()) {
				index++;
				notifyDataSetChanged();
			}
			saveWord();
			break;
		}
		hasNextStatus = false;
	}

	private long saveWord() {

		String create_date = DateUtil.getCurrentDate();
		RepertoryBean bean = new RepertoryBean();
		bean.setTable("TT_RESOURCE_JP");
		bean.setWid(((WordBean) mData.get(index)).getWid());
		bean.setUid(sp.getString("USERNAME", null));
		bean.setStatus("0");
		bean.setCount_right(0);
		bean.setCount_wrong(0);
		bean.setCreate_date(create_date);
		bean.setUpdate_date(create_date);
		return DataHelpUtil.saveBeanData("TT_REPERTORY_JP", bean);
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return index;
		} else {
			return 0;
		}
		// return count;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewHolder holder = views.get(position);
		if (holder == null || holder.all == null) {
			holder = new ViewHolder();
			holder.all = inflater.inflate(R.layout.view_study_viewpager,
					container, false);
			/** �õ������ؼ��Ķ��� */
			holder.explain = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_explain);
			holder.pos = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_pos);
			holder.down = (LinearLayout) holder.all
					.findViewById(R.id.view_study_viewpager_down);
			holder.prono = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_prono);
			holder.content = (EditText) holder.all
					.findViewById(R.id.view_study_viewpager_content);
			holder.test = (EditText) holder.all
					.findViewById(R.id.view_study_viewpager_test);
			holder.mark = (ImageView) holder.all
					.findViewById(R.id.view_study_viewpager_mark);

			views.put(position, holder);// ��ViewHolder����
		} else {
			holder = views.get(position);// ȡ��ViewHolder����
		}
		/** ����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е����� */
		WordBean bean = (WordBean) mData.get(position);
		holder.explain.setText(bean.getExplain());
		holder.pos.setText(bean.getPos());
		holder.prono.setText(bean.getPronounce());
		holder.content.setHint(bean.getContent());
		holder.content.setText(bean.getContent());
		holder.content.setOnFocusChangeListener(focusListener);
		holder.mark.setOnClickListener(clearListener);
		holder.mark.setOnLongClickListener(resetListener);
		holder.test.setOnFocusChangeListener(focusListener);
		holder.test.setHint(bean.getContent());
		holder.test.setNextFocusDownId(holder.content.getId());
		container.addView(holder.all);
		if (currentViewHolder == null) {
			currentViewHolder = holder;
		}
		return holder.all;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		this.currentViewHolder = views.get(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	/** ��ſؼ� */
	public final class ViewHolder {
		private View all;
		ImageView mark;
		TextView explain;
		TextView pos;
		TextView prono;
		EditText content;
		LinearLayout down;
		EditText test;
		int mode = 0x00;
		int result = 0x02;// 0δд��1��ȷ��2����
	}

}
