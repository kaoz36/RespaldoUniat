package com.jaguarlabs.sipac.util;

import android.view.View;

public interface IRowRenderer <dataType>{
	void renderRow(View targetRow, dataType dataItem);
}
