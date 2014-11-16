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
import com.zhu.ttwords.util.SuperMeMoUtil;
import com.zhu.ttwords.value.SQLS;

public class TestAdapter extends PagerAdapter implements OnPageChangeListener {

	public static final int RESULT_UNDO = 0x00;
	public static final int RESULT_DOING = 0x01;
	public static final int RESULT_WRONG = 0x02;
	public static final int RESULT_RIGHT = 0x03;
	public static final int RESULT_LOCK = 0x04;
	SharedPreferences sp;
	List<AbstractCommonBean> mData;
	Context mContext;
	IMessageFactory mMessageFactory;
	LayoutInflater inflater;
	SparseArray<ViewHolder> views;
	OnClickListener clearListener;
	OnLongClickListener resetListener;
	OnFocusChangeListener focusListener;
	ViewHolder currentViewHolder;
	String username;
	int current_index = 1;
	boolean hasNextStatus = false;
	public int testRight;

	public TestAdapter(Context context, List<AbstractCommonBean> mData,
			IMessageFactory messageFactory) {
		super();
		this.mContext = context;
		this.mMessageFactory = messageFactory;
		sp = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		username = sp.getString("USERNAME", null);
		if (mData != null) {
			this.mData = mData;
		} else {
			this.mData = new ArrayList<AbstractCommonBean>();
		}
		this.inflater = LayoutInflater.from(mContext);
		this.views = new SparseArray<TestAdapter.ViewHolder>();
		this.clearListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (hasNextStatus || currentViewHolder.result == RESULT_RIGHT
						|| currentViewHolder.result == RESULT_LOCK) {
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
					switch (currentViewHolder.result) {
					case RESULT_UNDO:
						break;
					case RESULT_DOING:

						CharSequence question = currentViewHolder.test
								.getHint().toString().trim();
						CharSequence answer = currentViewHolder.test.getText()
								.toString().trim();
						currentViewHolder.result = question.equals(answer) ? RESULT_RIGHT
								: RESULT_WRONG;
						break;
					case RESULT_WRONG:
						return;
					case RESULT_RIGHT:
						currentViewHolder.result = RESULT_LOCK;
						return;
					}
				} else if (v == currentViewHolder.test) {
					switch (currentViewHolder.result) {
					case RESULT_UNDO:
						currentViewHolder.result = RESULT_DOING;
						break;
					case RESULT_DOING:
						break;
					case RESULT_WRONG:
						currentViewHolder.result = RESULT_DOING;
						break;
					case RESULT_RIGHT:
						currentViewHolder.result = RESULT_LOCK;
						break;
					}
				}
				updateStatusAndMode();
			}
		};
	}

	private void updateStatusAndMode() {
		this.hasNextStatus = true;
		switch (currentViewHolder.result) {
		case RESULT_UNDO:
			currentViewHolder.content.setVisibility(View.INVISIBLE);
			currentViewHolder.test.requestFocus();
			currentViewHolder.test.getText().clear();
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_attention);
			break;
		case RESULT_DOING:
			currentViewHolder.content.setVisibility(View.VISIBLE);
			currentViewHolder.test.requestFocus();
			currentViewHolder.test.getText().clear();
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_attention);
			break;
		case RESULT_WRONG:
			currentViewHolder.content.setVisibility(View.VISIBLE);
			currentViewHolder.content.requestFocus();
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_wrong);
			saveWord(RESULT_WRONG);
			break;
		case RESULT_RIGHT:
			currentViewHolder.content.setVisibility(View.VISIBLE);
			currentViewHolder.content.requestFocus();
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_right);
			Log.d("DEBUG", "RESULT_RIGHT");
			testRight++;
			Message msg = mMessageFactory.getMessage(Activity.RESULT_OK);
			msg.sendToTarget();
			saveWord(RESULT_RIGHT);
			break;
		case RESULT_LOCK:
			currentViewHolder.content.setVisibility(View.VISIBLE);
			currentViewHolder.content.requestFocus();
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_right);
			Log.d("DEBUG", "RESULT_RIGHT");
			break;
		}

		this.hasNextStatus = false;
	}

	private long saveWord(int result) {
		WordBean bean_word = (WordBean) mData.get(current_index - 1);
		RepertoryBean bean = null;
		Long returnLong;
		try {
			bean = (RepertoryBean) DataHelpUtil.getSingleBean(
					RepertoryBean.class, SQLS.Study_getResource,
					new String[] { bean_word.getWid() + "" });
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (bean == null) {
			bean = new RepertoryBean();
			String create_date = DateUtil.getCurrentDate();
			bean.setTname("TT_RESOURCE_JP");
			bean.setWid(((WordBean) mData.get(current_index - 1)).getWid());
			bean.setUid(username);
			bean.setCreate_date(create_date);
			bean.setCountall(1);
			bean.setEf(2.5f);
			if (result == RESULT_WRONG) {
				bean.setLevel(2);
				bean.setCountn(0);
				bean.setCountw(1);
				bean.setUpdate_date(create_date);
			} else {
				bean.setLevel(3);
				bean.setCountn(0);
				bean.setCountw(0);
				int day = SuperMeMoUtil.getDay(bean.getEf(),
						bean.getCountn() + 1);
				String update_date = DateUtil.getUpdateDate(day);
				bean.setUpdate_date(update_date);
			}
			bean.setEf(SuperMeMoUtil.getEM(bean.getEf(), bean.getLevel()));
			if (bean.getLevel() < 3) {
				bean.setCountn(0);
			}
			returnLong = DataHelpUtil.saveBeanData("TT_REPERTORY_JP", bean);
			return returnLong;
		} else {
			bean.setCountall(bean.getCountall() + 1);
			int day = SuperMeMoUtil.getDay(bean.getEf(), bean.getCountn() + 1);
			bean.setUpdate_date(DateUtil.getUpdateDate(day));
			if (result == RESULT_WRONG) {
				bean.setCountw(bean.getCountw() + 1);
				bean.setLevel(bean.getLevel() >= 5 ? 5 : bean.getLevel() - 1);
			} else {
				bean.setLevel(bean.getLevel() >= 5 ? 5 : bean.getLevel() + 1);
			}
			bean.setEf(SuperMeMoUtil.getEM(bean.getEf(), bean.getLevel()));
			bean.setCountn(bean.getCountn() + 1);
			if (bean.getLevel() < 3) {
				bean.setCountn(0);
			}
			returnLong = DataHelpUtil.updateBeanData("TT_REPERTORY_JP", bean,
					SQLS.Study_updateWordInfo, new String[] { "TT_RESOURCE_JP",
							bean.getWid() + "" });
			return returnLong;
		}
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
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
			/** 得到各个控件的对象 */
			holder.explain = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_explain);
			holder.pos = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_pos);
			holder.down = (LinearLayout) holder.all
					.findViewById(R.id.view_study_viewpager_down);
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
		holder.content.setOnFocusChangeListener(focusListener);
		holder.content.setAlpha(0);
		holder.content.setVisibility(View.INVISIBLE);
		holder.test.setHint(bean.getContent());
		holder.test.setHintTextColor(0x06000000);
		holder.test.setOnFocusChangeListener(focusListener);
		holder.test.setNextFocusDownId(holder.content.getId());
		holder.mark.setOnClickListener(clearListener);
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
		EditText content;
		LinearLayout down;
		EditText test;
		int result = 0x00;// 0未写，1正确，2错误
	}

}
