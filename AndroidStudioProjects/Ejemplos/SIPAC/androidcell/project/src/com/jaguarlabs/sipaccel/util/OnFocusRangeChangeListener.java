package com.jaguarlabs.sipaccel.util;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class OnFocusRangeChangeListener<numberType> implements
		View.OnFocusChangeListener {

	private numberType minRange;
	private numberType maxRange;
	private IRangeCallback callback;
	private EditText target;
	private IKeyboardHider hider;

	public numberType getMinRange() {
		return minRange;
	}

	public void setMinRange(numberType minRange) {
		this.minRange = minRange;
	}

	public numberType getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(numberType maxRange) {
		this.maxRange = maxRange;
	}

	public OnFocusRangeChangeListener(numberType pMin, numberType pMax,
			IRangeCallback callback, EditText target,IKeyboardHider pHider) {
		minRange = pMin;
		maxRange = pMax;
		this.target = target;
		this.callback = callback;
		this.hider = pHider;

		target.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				changeTextColor(s.toString());

			}
		});
		
		target.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					onFocusChange(v, false);
					return true;
				}
				return false;
			}
		});
	}

	public void changeTextColor(String pText) {
		try {
			double val = Double.parseDouble(pText);
			if (Math.min(Double.parseDouble(maxRange.toString()),
					Math.max(Double.parseDouble(minRange.toString()), val)) != val) {
				target.setTextColor(Color.RED);

			} else {
				target.setTextColor(Color.BLACK);
			}

		} catch (NumberFormatException ex) {
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		EditText editText = (EditText) v;
		changeTextColor(editText.getText().toString());
		if (!hasFocus) {
			try {
				double val = Double.parseDouble(editText.getText().toString());
				if (Math.min(Double.parseDouble(maxRange.toString()), Math
						.max(Double.parseDouble(minRange.toString()), val)) != val) {
					callback.doAfterValidation(Math.min(Double
							.parseDouble(maxRange.toString()), Math.max(
							Double.parseDouble(minRange.toString()), val)),
							false);
				} else {
					callback.doAfterValidation(Math.min(Double
							.parseDouble(maxRange.toString()), Math.max(
							Double.parseDouble(minRange.toString()), val)),
							true);
					
				}
				
				hider.hideKeyboard(target);

			} catch (NumberFormatException ex) {
			}
		}
	}

}
