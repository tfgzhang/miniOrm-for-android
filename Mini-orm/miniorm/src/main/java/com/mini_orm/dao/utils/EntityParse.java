package com.mini_orm.dao.utils;

import android.util.Log;

import com.mini_orm.annotation.Table;
import com.mini_orm.annotation.TableColumn;
import com.mini_orm.annotation.TableID;
import com.mini_orm.entity.TableColumnEntity;
import com.mini_orm.entity.TableEntity;
import com.mini_orm.entity.TableIdEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityParse<T> {

	private 	Class<? extends Object> c ;
	
	private   Table table;
	TableID     tableID;
	public      EntityParse(T t){
		  c = t.getClass();
 	}
	
	public   Table getTable() {
		try {

			 if(table==null)
			  table = c.getAnnotation(Table.class);
  			
			return table;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}
	}

	public TableID getTableID() {
		Field[] fields = c.getDeclaredFields();
		for (Field filed : fields) {
			 
			TableID id = filed.getAnnotation(TableID.class);
			if (id != null)
				return id;
		}
 		return null;
	}
	public  TableID[] getTableIDs() {
		ArrayList<TableID> list=new ArrayList<TableID>();
		Field[] fields = c.getFields();
		for (Field filed : fields) {
			TableID id = filed.getAnnotation(TableID.class);
			if (id != null)
				  list.add(id);
		}

		return list.toArray(new TableID[list.size()]);
	}
	public  Method[] getMethods(){
		return c.getMethods();
	}
	

	public ReflexEntity getFieldValueFromT(Object t){
		
		 ReflexEntity reflexEntity=new ReflexEntity();
		
		
		Class<? extends Object> c = t.getClass();

		boolean flag = c.isAnnotationPresent(Table.class);
		if(flag){

			Table table=c.getAnnotation(Table.class);
			
			reflexEntity.setTableName(table.name());
			
			reflexEntity.setTableEntity(new TableEntity(table.name(),c));
			
  			
			Field[] fields = c.getDeclaredFields();

			for (Field field : fields) {
				
				TableColumn column = field.getAnnotation(TableColumn.class);
				if (column != null) {
					reflexEntity.addKey(column.name());
					TableColumnEntity tableColumnEntity=new TableColumnEntity();
					tableColumnEntity.setColumnName(column.name());
					tableColumnEntity.setColumnType(column.columnType());
					//tableColumnEntity.setFieldObject(fieldObject);
					tableColumnEntity.setForeignkey(column.isForeignkey());
					//tableColumnEntity.setVal(val);
					
					reflexEntity.getTableColumnMap().put(field.getName(), tableColumnEntity);
					
					
					
 
				} else {

					TableID id = field.getAnnotation(TableID.class);
					
   					if (id != null ) {
   						reflexEntity.addKey(id.name());
   						TableIdEntity tableIdEntity=new TableIdEntity();
						tableIdEntity.setDefaultVal(id.defaultVal());
						tableIdEntity.setFieldName(id.name());
						tableIdEntity.setPrimaryKey(true);
 						tableIdEntity.setKeytype(id.type());
 						tableIdEntity.setColumnName(id.name());
 						tableIdEntity.setColumnVal(getObjFromField(field,t));
 						tableIdEntity.setColumnType(id.columnType());
   						reflexEntity.setTableIdEntity(tableIdEntity);
  						
 					} else  {
						 
						continue;
					}
 						
				}
				 	
 	    		String firstLetter = field.getName().substring(0,1).toUpperCase();


	    	    String getMethodName = "get"+firstLetter+field.getName().substring(1); 
	    	    
	    	    try {
					Method m=c.getMethod(getMethodName);
					
					
					
					Object obj=m.invoke(t);
					
					
 					if(obj!=null){
 						
 						TableColumnEntity tableColumnEntity=null;
 						
 						if(reflexEntity.getTableColumnMap().get(field.getName())!= null){
 							tableColumnEntity=reflexEntity.getTableColumnMap().get(field.getName());
 						}else{
 							continue;
 						}
 						
 						tableColumnEntity.setFieldObject(obj);
 						
 						Class<? extends Object> cla= obj.getClass();
 						tableColumnEntity.setMethod(m);
 						//tableColumnEntity.setColumnType(columnType);
 						
 						if(cla!=null){
 							
  							Table table2=obj.getClass().getAnnotation(Table.class);
 	  						if(table2!=null){
 	 							//	Class<? extends Object> cla1= field.getClass();
 	  							
 	  							tableColumnEntity.setForeignkey(true);
 	  							
  	 							FieldColumnAndVal f= getColumnAndVal(obj);

  	 	 						tableColumnEntity.setColumnName(f.fileColumn);
  	 	 						
  	 	 						tableColumnEntity.setColumnVal(f.val);
  	 	 						
 	 	 	 					reflexEntity.addKeyValue(f.fileColumn, f.val);
   	 						 }else{
   	 							tableColumnEntity.setColumnVal(obj);
   	 						 }
 						}
 						
 					
  						
 	 					reflexEntity.addKeyValue(getTableColumn(field), obj);
 	 					
 	 					reflexEntity.getTableColumnMap().put(field.getName(),tableColumnEntity);
 					}
 					 
   				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return reflexEntity;
		
		
		
	}
	
	
	public  String  getTableColumn(Field field){
		
		TableColumn column = field.getAnnotation(TableColumn.class);
		if (column != null) {
			
			return column.name();
		} else {

			TableID id = field.getAnnotation(TableID.class);
			if(id!=null){
				return id.name();
			}
		}
		return null;
	}
	
	public   Table getTable(T t) {
		try {

			Class<? extends Object> c = t.getClass();

			Table table = c.getAnnotation(Table.class);
			
 			
			return table;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}
	}

	public    TableID getTableID(T classs) {

		Class<? extends Object> c = classs.getClass();
		Field[] fields = c.getFields();
		for (Field filed : fields) {
			TableID id = filed.getAnnotation(TableID.class);
			if (id != null)
				return id;
		}

		return null;
	}
	
	
	public Object getObjFromField(Field field,Object t){
		String firstLetter = field.getName().substring(0,1).toUpperCase(); 
		//ת�����ֶε�get����
	    String getMethodName = "get"+firstLetter+field.getName().substring(1); 
	    
	    try {
			Method m=c.getMethod(getMethodName);
			
   			Object obj=m.invoke(t);
   			
   			return obj;
   			
	    }catch(Exception e){
	    	
	    }
			return null;

	}
	
	
	
	
	public class  FieldColumnAndVal{
		public String fileColumn;
		public Object val;
		public FieldColumnAndVal(String fileColumn,Object val){
			this.fileColumn=fileColumn;
			this.val=val;
		}
	}
	
	public  FieldColumnAndVal  getColumnAndVal(Object obj1) {
		Field[] fields = obj1.getClass().getDeclaredFields();
		for (Field filed : fields) {
			TableID id = filed.getAnnotation(TableID.class);
			 if(id!=null){

		    		String firstLetter = filed.getName().substring(0,1).toUpperCase(); 

		    	    String getMethodName = "get"+firstLetter+filed.getName().substring(1); 
  					Object obj = null;
					try {
			    	    Method m=obj1.getClass().getMethod(getMethodName);
						obj = m.invoke(obj1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
  					
				 FieldColumnAndVal fieldColumnAndVal=new FieldColumnAndVal( id.name(),obj);
				 
				 return  fieldColumnAndVal;
  			 }
		}

		return null;
	}
	
	
	
	
	public HashMap<String,Field>  getColumnAndField(T t){
		HashMap<String, Field>  hashMap=new HashMap<String, Field>();
			Field[] fields=t.getClass().getDeclaredFields();
			for(Field field:fields){
				String columnName=getColumnNameFormField(field);
				hashMap.put(columnName, field);

			}
		
		
		return hashMap;
		
	}
	
	public  T  setEntityValue(T t,Object val,Field field){
		
		   
		try {
			field.setAccessible(true);
			field.set(t, val);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return t;
	}
	
	
	
	
	public String  getColumnNameFormField(Field field){
		
		TableColumn column = field.getAnnotation(TableColumn.class);
		Log.e("tag",field.getName());
		if (column != null) {
  
			return column.name();
			
		} else {

			TableID id = field.getAnnotation(TableID.class);
			
			return id.name();
		}
		
	}
	
	
	
	
	public String  isMySelfAnnotion(Method method){
		String methodName=null;
		TableColumn tableColumn=method.getAnnotation(TableColumn.class);
		TableID  tableID=method.getAnnotation(TableID.class);

		if(tableColumn==null&&tableID==null){
			 
			
		}else{
			methodName=method.getName();
		}
		
		if(methodName==null)return null;
		
		if(methodName.startsWith("set")){
			//methodName.sub
		}
		
		return null;
		
	}
	
	
	
}
