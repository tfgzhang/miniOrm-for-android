package com.mini_orm.android.parseType;

import android.database.Cursor;

public interface ParseTypeInterface<T> {
	
	public  T  getValFromCursor(Cursor corCursor, int index);

}
