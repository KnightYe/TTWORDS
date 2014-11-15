package com.zhu.ttwords.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import com.zhu.ttwords.common.IMessageFactory;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.util.DateUtil;
import com.zhu.ttwords.value.SQLS;

public class StudyAdapter extends PagerAdapter implements OnPageChangeListener {
	public static final int MODE_READY = 0X00;
	public static final int MODE_LEARN = 0x10;
	public static final int MODE_TEST = 0x20;
	public static final int MODE_OVER = 0x30;
	public static final int RESULT_UNDO = 0x00;
	public static final int RESULT_WRONG = 0x01;
	public static final int RESULT_RIGHT = 0x02;

	List<AbstractCommonBean> mData;
	Context mContext;
	IMessageFactory mMessageFactory;
	LayoutInflater inflater;
	SparseArray<ViewHolder> views;
	OnClickListener clearListener;
	OnLongClickListener resetListener;
	OnFocusChangeListener focusListener;
	ViewHolder currentViewHolder;
	int current_index = 1;
	boolean hasNextStatus = false;

	public StudyAdapter(Context context, List<AbstractCommonBean> mData,
			IMessageFactory messageFactory) {
		super();
		this.mContext = context;
		this.mMessageFactory = messageFactory;
		if (mData != null) {
			this.mData = mData;
		} else {
			this.mData = new ArrayList<AbstractCommonBean>();
		}
		this.inflater = LayoutInflater.from(mContext);
		this.views = new SparseArray<StudyAdapter.ViewHolder>();
		this.resetListener = new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (hasNextStatus) {
					return true;
				}
				currentViewHolder.level = MODE_READY;
				currentViewHolder.result = RESULT_RIGHT;
				updateStatusAndMode();
				return false;
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
						if (currentViewHolder.level <= 0x20) {
							currentViewHolder.level += 0x10;
						} else {
							currentViewHolder.level = 0x30;
						}
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
						if (currentViewHolder.level <= 0x20) {
							currentViewHolder.level += 0x10;
						} else {
							currentViewHolder.level = 0x30;
						}
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
		switch (currentViewHolder.level + currentViewHolder.result) {
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
			Log.d("MODE", "0x02");
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
			Log.d("MODE", "0x10");
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
			Log.d("MODE", "0x11");
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
			Log.d("MODE", "0x12");
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
			Log.d("MODE", "0x20");
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
			Log.d("MODE", "0x21");
			break;
		case 0x22:
			// currentViewHolder.down.setVisibility(View.INVISIBLE);
			// currentViewHolder.content.requestFocus();
			// currentViewHolder.test.setFocusable(true);
			// currentViewHolder.test.setFocusableInTouchMode(true);
			// currentViewHolder.test.setAlpha(1);
			// currentViewHolder.mark
			// .setImageResource(R.drawable.image_mark_right);
			// currentViewHolder.mark.setVisibility(View.VISIBLE);
			// Log.d("MODE","0x22");
			// break;

		case 0x30:
		case 0x31:
		case 0x32:
			currentViewHolder.down.setVisibility(View.INVISIBLE);
			currentViewHolder.content.requestFocus();
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_right);
			currentViewHolder.test.setAlpha(1);

			if (current_index == mData.size()) {
				Message msg = mMessageFactory.getMessage(Activity.RESULT_OK);
				msg.sendToTarget();
			}
			Log.d("MODE", "0x32");
			break;
		}
		hasNextStatus = false;
	}
	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
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
			/** 得到各个控件的对象 */
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

			views.put(position, holder);// 绑定ViewHolder对象
		} else {
			holder = views.get(position);// 取出ViewHolder对象
		}
		/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
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
		holder.test.setAlpha(0);;
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
		this.current_index = position + 1;
		Message msg = mMessageFactory.getMessage(Activity.RESULT_FIRST_USER);
		msg.arg1 = current_index;
		msg.sendToTarget();
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	/** 存放控件 */
	public final class ViewHolder {
		private View all;
		ImageView mark;
		TextView explain;
		TextView pos;
		TextView prono;
		EditText content;
		LinearLayout down;
		EditText test;
		int level = 0x00;// 0x0*准备好，0x1*第一次，0x2*第二次，0x3*正确锁定。
		int result = 0x02;// 0未写，1正确，2错误
	}

}
