package com.yzy.supercleanmaster.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.hua.supercleanmaster.listener.FunctionButonClickListener;
import com.yzy.supercleanmaster.R;
import com.yzy.supercleanmaster.utils.BitmapUtils;
import com.yzy.supercleanmaster.utils.LogUtils;

@SuppressLint("NewApi")
public class HomeButton extends ImageView {

	private Bitmap bitmap;
	private Bitmap home_flight;
	private int state = 0; // 按下

	private float textsize;
	/**the description of button*/
	private String description;
	

	private Rect bounds;
	private TextPaint textPaint;
	private int descriptionWidth;
	private int descriptionHeight;
	private int dration = 200;
	private boolean isOpen;
	private float startPointX = 0;
	private float startPointY = 0;
	private int top = 0;
	private int left = 0;

	// 点击事件
	private FunctionButonClickListener listener = null;

	private int[] colors = { getResources().getColor(R.color.white) };

	public HomeButton(Context context) {
		super(context);
	}

	public HomeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmap = BitmapUtils.zoomImage(BitmapFactory.decodeResource(
				getResources(), R.drawable.fingerprint), 127, 122);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.HomeButton);
		textsize = typedArray.getDimension(R.styleable.HomeButton_textSize, 24);
		description = typedArray.getString(R.styleable.HomeButton_text);
		home_flight = ((BitmapDrawable)typedArray.getDrawable(R.styleable.HomeButton_image)).getBitmap();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 
		setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
				getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(getResources().getColor(R.color.card_normal));
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(24);
		
		Paint paintText = new Paint();
		paintText.setColor(Color.BLACK);
		paintText.setTextSize(textsize);
		paintText.setAntiAlias(true);
		//
		bounds = new Rect();
//		textPaint = new TextPaint();
		paintText.getTextBounds(description, 0, description.length(), bounds);
		descriptionWidth = bounds.width();
		descriptionHeight = bounds.height();
		
		///
			Matrix matrix = new Matrix();
			matrix.postTranslate(this.getWidth() / 2 - home_flight.getWidth()
					/ 2, this.getHeight() / 2 - home_flight.getHeight() / 2 - descriptionHeight);
			canvas.drawText(description, this.getWidth() / 2 - descriptionWidth/2, this.getHeight() / 2 + home_flight.getHeight()
					/ 2 + descriptionHeight/2, paintText);
			canvas.drawBitmap(home_flight, matrix, paint);
//		else {
//			Matrix matrix_small = new Matrix();
//			matrix_small.postTranslate(10,
//					this.getHeight() / 2 - home_flight.getHeight() / 2);
//			canvas.drawBitmap(home_flight, matrix_small, new Paint());
//			if (home == 3) {
//				paint.setTextSize(16);
//				canvas.drawText(
//						"夜宵酒店",
//						home_flight.getWidth() + 20,
//						this.getHeight() / 2 - home_flight.getHeight() / 2 + 10,
//						paint);
//				canvas.drawText("加载中...", home_flight.getWidth() + 20,
//						this.getHeight() / 2 + home_flight.getHeight() / 2,
//						paint);
//			} else if (home == 5) {
//				paint.setTextSize(16);
//				canvas.drawText(
//						"送机",
//						home_flight.getWidth() + 20,
//						this.getHeight() / 2 - home_flight.getHeight() / 2 + 10,
//						paint);
//				canvas.drawText("免费叫出租", home_flight.getWidth() + 20,
//						this.getHeight() / 2 + home_flight.getHeight() / 2,
//						paint);
//			} else {
//				canvas.drawText(description, home_flight.getWidth() + 20,
//						this.getHeight() / 2 + home_flight.getHeight() / 2,
//						paint);
//			}
//		}
		if (state == 1) {
			LogUtils.i("state=="+state);
			Matrix matrix2 = new Matrix();
			matrix2.postTranslate(this.getWidth() / 2 - bitmap.getWidth() / 2,
					this.getHeight() / 2 - bitmap.getHeight() / 2);
			canvas.drawBitmap(bitmap, matrix2, new Paint());
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float start = 1.0f;
		float end = 0.95f;
		float lastPointX = 0;
		float lastPointY = 0;
		
		Animation scaleAnimation = new ScaleAnimation(start, end, start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		Animation endAnimation = new ScaleAnimation(end, start, end, start,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(dration);
		scaleAnimation.setFillAfter(true);
		endAnimation.setDuration(dration);
		endAnimation.setFillAfter(true);
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			LogUtils.d("ACTION_DOWN--------");
			if(isOpen)break;
			startPointX =  event.getX();
			startPointY = event.getY();
			this.getTop();
			lastPointX = startPointX;
			lastPointY = startPointY;
			this.startAnimation(scaleAnimation);
			state = 1;
			isOpen = false;
			invalidate();
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			LogUtils.d("ACTION_MOVE--------");
			if(isOpen){
				break;
			}
			lastPointX = event.getX();
			lastPointY = event.getY();
			top = this.getTop();
			left = this.getLeft();
			
			LogUtils.e("lastPointX---="+(lastPointX));
			LogUtils.d("lastPointY---="+(lastPointY));
			
			if(lastPointX <=0 || lastPointX >= getWidth() || lastPointY >= getHeight() || lastPointY <=0){
				LogUtils.d("--------");
				this.startAnimation(endAnimation);
				state = 0;
				isOpen = true;
				event.setAction(MotionEvent.ACTION_CANCEL);
				invalidate();
			}
			break;
			
		case MotionEvent.ACTION_UP:
			LogUtils.e("------ACTION_UP----------");
			if(isOpen){
				isOpen = false;
				break;
			}
			this.startAnimation(endAnimation);
			state = 0;
			invalidate();
			if (listener != null) {
				listener.onclick(this);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			LogUtils.e("--------ACTION_CANCEL-------------");
			this.startAnimation(endAnimation);
			state = 0;
			invalidate();
			break;
		}
		return true;
	}

	/**
	 * 加入响应事件
	 * 
	 * @param clickListener
	 */
	public void setOnHomeClick(FunctionButonClickListener clickListener) {
		this.listener = clickListener;
	}
	
}
