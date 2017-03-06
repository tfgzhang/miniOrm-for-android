package com.miniorm.android.parseType.parser;

import android.database.Cursor;

import com.miniorm.android.parseType.ParseTypeInterface;

public class FloatParser implements ParseTypeInterface {

	@SuppressWarnings("unchecked")
	public Float getValFromCursor(Cursor corCursor, int index) {
		// TODO Auto-generated method stub
		return corCursor.getFloat(index);
	}

}
