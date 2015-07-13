package com.uniat.eduscore.com.uniat.eduscore.util;

import android.database.Cursor;

/**
 * Created by Admin on 14/04/2015.
 */
public interface ICursorDecoder {
    void decode(Cursor pCursor);
    long getId();
    void setId(long pId);
}
