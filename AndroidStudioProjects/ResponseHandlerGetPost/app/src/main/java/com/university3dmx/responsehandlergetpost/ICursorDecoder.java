package com.university3dmx.responsehandlergetpost;

import android.database.Cursor;

public interface ICursorDecoder {
	void decode(Cursor pCursor);
	long getId();
	void setId(long pId);
}
