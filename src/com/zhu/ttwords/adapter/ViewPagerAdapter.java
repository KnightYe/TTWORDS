package com.zhu.ttwords.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhu.ttwords.R;
import com.zhu.ttwords.bean.AbstractCommonBean;
import com.zhu.ttwords.bean.WordBean;

public class ViewPagerAdapter extends PagerAdapter implements
		OnPageChangeListener {
	public static final int LEARN = 0;
	public static final int TEST = 1;

	List<AbstractCommonBean> mData;
	Activity mContext;
	LayoutInflater inflater;
	SparseArray<ViewHolder> views;
	OnClickListener clearListener;
	OnLongClickListener resetListener;
	OnFocusChangeListener focusListener;
	ViewHolder currentViewHolder;
	int index = 1;

	public ViewPagerAdapter(Activity context, List<AbstractCommonBean> mData) {
		super();
		this.mContext = context;
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
				currentViewHolder.status = LEARN;
				updateView();
				return true;
			}
		};
		this.focusListener = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (currentViewHolder.status == LEARN) {
						currentViewHolder.content.setText("");
						InputMethodManager inputMethodManager1 = (InputMethodManager) mContext
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager1.showSoftInput(
								currentViewHolder.content,
								InputMethodManager.HIDE_NOT_ALWAYS);
						currentViewHolder.mark.setVisibility(View.VISIBLE);
						if (currentViewHolder.result == 1) {
							currentViewHolder.status = TEST;
							updateView();
						}
					} else if (currentViewHolder.status == TEST) {
						updateView();
					}
				}
			}
		};
		this.clearListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentViewHolder.result == 1) {
					return;
				}
				currentViewHolder.result = 0;
				currentViewHolder.mark
						.setImageResource(R.drawable.image_mark_attention);
				if (currentViewHolder.status == LEARN) {
					currentViewHolder.content.setText("");
					currentViewHolder.content.requestFocus();
				}
				if (currentViewHolder.status == TEST) {
					currentViewHolder.test.getText().clear();
					currentViewHolder.test.requestFocus();
				}
			}
		};

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
			/** 得到各个控件的对象 */
			holder.explain = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_explain);
			holder.pos = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_pos);
			holder.down = (LinearLayout) holder.all
					.findViewById(R.id.view_study_viewpager_down);
			holder.focus = (LinearLayout) holder.all
					.findViewById(R.id.view_study_viewpager_focus);
			holder.prono = (TextView) holder.all
					.findViewById(R.id.view_study_viewpager_prono);
			holder.content = (TextView) holder.all
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
		holder.content.setText(bean.getContent());
		holder.content.setHint(bean.getContent());
		holder.content.setOnFocusChangeListener(focusListener);
		// holder.content.setInputType(InputType.TYPE_CLASS_TEXT
		// | InputType.TYPE_TEXT_VARIATION_NORMAL);
		holder.mark.setOnClickListener(clearListener);
		holder.mark.setOnLongClickListener(resetListener);
		holder.test.setHint(bean.getContent());
		container.addView(holder.all);
		if (currentViewHolder == null) {
			currentViewHolder = holder;
		}
		return holder.all;
	}

	private void updateView() {

		switch (currentViewHolder.status) {
		case LEARN:
			currentViewHolder.down.setVisibility(View.VISIBLE);
			currentViewHolder.content.setVisibility(View.VISIBLE);
			currentViewHolder.test.setVisibility(View.INVISIBLE);
			currentViewHolder.mark.setVisibility(View.INVISIBLE);
			break;
		case TEST:
			currentViewHolder.down.setVisibility(View.INVISIBLE);
			currentViewHolder.content.setVisibility(View.INVISIBLE);
			currentViewHolder.test.setVisibility(View.VISIBLE);
			currentViewHolder.mark.setVisibility(View.VISIBLE);
			currentViewHolder.result = 0;
			currentViewHolder.mark
					.setImageResource(R.drawable.image_mark_attention);
			currentViewHolder.test.requestFocus();
			// InputMethodManager inputMethodManager1 = (InputMethodManager)
			// mContext
			// .getSystemService(Context.INPUT_METHOD_SERVICE);
			// inputMethodManager1.showSoftInput(currentViewHolder.test,
			// InputMethodManager.SHOW_FORCED);
			break;
		}
	}

	public boolean checkWord() {
		CharSequence content = currentViewHolder.content.getHint().toString()
				.trim();
		CharSequence answer1 = currentViewHolder.content.getText().toString()
				.trim();
		CharSequence answer2 = currentViewHolder.test.getText().toString()
				.trim();
		boolean result = false;
		if (currentViewHolder.status == LEARN) {
			result = content.equals(answer1);
		} else if (currentViewHolder.status == TEST) {
			result = content.equals(answer2);
			if (result) {
				currentViewHolder.test.setFocusable(false);
				if (index < mData.size()) {
					index++;
					notifyDataSetChanged();
				}
			}
		}
		currentViewHolder.result = result ? 1 : 2;
		currentViewHolder.mark
				.setImageResource(result ? R.drawable.image_mark_right
						: R.drawable.image_mark_wrong);
		currentViewHolder.focus.requestFocus();
		InputMethodManager inputMethodManager1 = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager1.showSoftInput(currentViewHolder.focus,
				InputMethodManager.SHOW_FORCED);
		return result;
	}

	// 半角转化为全角的方法
	public CharSequence ToSBC(CharSequence input) {
		// 半角转全角：
		StringBuilder tmep = new StringBuilder(input);
		for (int i = 0; i < tmep.length(); i++) {
			if (tmep.charAt(i) == 32) {
				tmep.setCharAt(i, (char) 12288);
				continue;
			}
			if (tmep.charAt(i) < 127 && tmep.charAt(i) > 32)
				tmep.setCharAt(i, (char) (tmep.charAt(i) + 65248));
		}
		return tmep.toString();
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

	/** 存放控件 */
	public final class ViewHolder {
		private View all;
		ImageView mark;
		TextView explain;
		TextView pos;
		TextView prono;
		TextView content;
		LinearLayout down;
		LinearLayout focus;
		EditText test;
		int status = 0;
		int result = 0;// 0未写，1正确，2错误
	}

}
