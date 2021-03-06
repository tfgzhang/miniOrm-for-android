package com.miniorm.entity;

import java.lang.reflect.Field;

public class TableColumnEntity {

	private String columnName;

	private boolean isPrimaryKey;

	private boolean isForeignkey;

	private String  columnType;
	private boolean IgnoreBooleanParam;
	private Field  field;

	private String  defaultVal;


	private  boolean isHierarchicalQueries;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String name) {
		this.columnName = name;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isForeignkey() {
		return isForeignkey;
	}

	public void setForeignkey(boolean isForeignkey) {
		this.isForeignkey = isForeignkey;
	}



	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public void setIsHierarchicalQueries(boolean isHierarchicalQueries) {
		this.isHierarchicalQueries = isHierarchicalQueries;
	}

	public boolean isHierarchicalQueries() {
		return isHierarchicalQueries;
	}

	public boolean isIgnoreBooleanParam() {
		return IgnoreBooleanParam;
	}

	public void setIgnoreBooleanParam(boolean ignoreBooleanParam) {
		IgnoreBooleanParam = ignoreBooleanParam;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}
}
