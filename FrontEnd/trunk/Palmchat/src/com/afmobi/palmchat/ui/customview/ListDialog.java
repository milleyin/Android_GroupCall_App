package com.afmobi.palmchat.ui.customview;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;


public class ListDialog extends Dialog  {
	
	private List<DialogItem> items;
	private Context mContext;
	private LinearLayout dialogView;
	private OnItemClick itemClick;
	public static boolean canLongClick = false;;
	public OnItemClick getItemClick() {
		return itemClick;
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public interface OnItemClick {
		void onItemClick(DialogItem item);
	}
	
	
	public ListDialog(Context context, List<DialogItem> items) {
		super(context , R.style.CustomDialogTheme);
		this.items = items;
		this.mContext = context;
		init();
	}
	@Override
	public void show() {
		canLongClick =true;
		super.show();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void init() {
		dialogView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.custom_dialog_layout, null);
		TextView textView;
		ImageView imageView;
		View mainRow;
		for (final DialogItem item : items) {
			final LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext).inflate(item.getViewId(), null);
			textView = (TextView) itemView.findViewById(R.id.popup_text);
			textView.setText(item.getTextId());
			textView.setClickable(true);
			imageView = (ImageView) itemView.findViewById(R.id.dialog_icon);
			mainRow = itemView.findViewById(R.id.dialog_row);
			if (imageView != null) {
				if (item.getImageId() > 0) { //判断是否在前面带图标
					imageView.setVisibility(View.VISIBLE);
					imageView.setBackgroundResource(item.getImageId());
				} else {
					imageView.setVisibility(View.GONE);
				}
			}
			if (item.getTextId() == R.string.report_abuse) {
				textView.setTextColor(mContext.getResources().getColor(R.color.red));
			}

			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (itemClick != null) {
						itemClick.onItemClick(item);
						dismiss();
					}
				}
			});
			if (mainRow != null) {
				mainRow.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (itemClick != null) {
							itemClick.onItemClick(item);
							dismiss();
						}
					}
				});
			}
			dialogView.addView(itemView);
		}

		WindowManager.LayoutParams localLayoutParams = getWindow()
				.getAttributes();
		localLayoutParams.x = 0;
		localLayoutParams.y = -1000;
		localLayoutParams.gravity = Gravity.BOTTOM;
		dialogView.setMinimumWidth(10000);

		onWindowAttributesChanged(localLayoutParams);
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}
	
	@Override
	public void dismiss() {
		canLongClick =false;
		super.dismiss();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(dialogView);
	}
	
}
