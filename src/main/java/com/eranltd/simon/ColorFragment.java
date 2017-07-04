package com.eranltd.simon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class ColorFragment extends View implements OnTouchListener {
	private GradientDrawable drawable;


	public interface PushListener {
		 void onPush(View v);
	}
	
	private PushListener onPushListener;
	
	public ColorFragment(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void onDraw(Canvas canvas) { 
		setOnTouchListener(this);
		drawable = (GradientDrawable)this.getBackground();
	}

	@Override
	public boolean onTouch(View v, MotionEvent evt) {
		if(evt.getAction() == MotionEvent.ACTION_DOWN) {
			on();
		}
		if(evt.getAction() == MotionEvent.ACTION_UP) {
			off();
			if(onPushListener != null) {
				onPushListener.onPush(this);
			}
		}
		return true;
	}

	public void on() {
		drawable.setAlpha(0);
	}

	public void off() {
		drawable.setAlpha(255);
	}


	public void setPushListener(PushListener l) {
		this.onPushListener = l;
	}
}
