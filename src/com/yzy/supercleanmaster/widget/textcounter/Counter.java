package com.yzy.supercleanmaster.widget.textcounter;

import com.yzy.supercleanmaster.utils.LogUtils;


/**
 * Created by prem on 10/28/14.
 * 
 * Class that handles the counting up/down of the text value
 */
class Counter implements Runnable {

	private CounterView view;
	private float increment, startValue, endValue;
	private long interval;
	private float currentValue, newValue;
	private boolean isRunning;

	Counter(CounterView view, float startValue, float endValue, long interval,
			float increment) {
		this.view = view;
		this.startValue = startValue;
		this.endValue = endValue;
		this.interval = interval;
		this.increment = increment;
		this.newValue = this.startValue;
		this.currentValue = this.startValue - increment;
//		LogUtils.d("startValue--=" + startValue);
//		LogUtils.d("newValue--=" + newValue);
//		LogUtils.d("currentValue--=" + currentValue);
//		LogUtils.d("increment--=" + increment);
//		LogUtils.d("endValue--=" + endValue);
	}

	@Override
	public void run() {
		if (valuesAreCorrect()) {
			float valueToSet;
			if (newValue <= endValue) {
				valueToSet = newValue;
			} else {
				valueToSet = endValue;
				isRunning = true;
			}
			view.setCurrentTextValue(valueToSet);
			currentValue = newValue;
			newValue += increment;
			if(isRunning){
				view.removeCallbacks(Counter.this);
			}else {
				view.removeCallbacks(Counter.this);
				view.postDelayed(Counter.this, interval);
			}
		}
	}

	private boolean valuesAreCorrect() {
//		LogUtils.d("newValue--=" + newValue);
//		LogUtils.d("currentValue--=" + currentValue);
		if (increment >= 0) {
			return newValue >= currentValue;
		} else {
			return newValue <= currentValue;
		}
	}
}
