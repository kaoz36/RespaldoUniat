package com.jaguarlabs.sipaccel.util;

import android.view.View;

public interface IRowRenderer <dataType>{
	void renderRow(View targetRow, dataType dataItem);
}
