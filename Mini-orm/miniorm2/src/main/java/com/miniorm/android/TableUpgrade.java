package com.miniorm.android;

import android.database.Cursor;

import com.miniorm.MiniOrm;
import com.miniorm.dao.BaseDao2;
import com.miniorm.dao.utils.ResultType;
import com.miniorm.debug.DebugLog;
import com.miniorm.entity.TableColumnEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by admin on 2016/10/28.
 */
public class TableUpgrade {
    DatabaseExcute databaseExcute;
    public TableUpgrade( ){

        databaseExcute= new DatabaseExcute();
    }

    public void update(){
        ArrayList<Class<BaseDao2>> daolist= MiniOrm.getUpdateTables();
        try {
            databaseExcute. beginTransaction();
            for (Class<BaseDao2> daoClass:daolist){
                BaseDao2 dao=daoClass.newInstance();
                tableUpdate(dao);

            }
            databaseExcute.  setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            databaseExcute.   endTransaction();
        }

    }
    private void tableUpdate(BaseDao2 baseDao) throws InstantiationException, IllegalAccessException {
        String talbeName=baseDao.getReflexEntity().getTableEntity().getTableName();

        String  queryIsexitTableSql="select count(*) from sqlite_master where type='table'  and  name='"+talbeName+"'";

        Cursor cursorisexit=	databaseExcute.excuteQuery(queryIsexitTableSql, null);

        if (cursorisexit != null && cursorisexit.moveToFirst()) {
            int  num=cursorisexit.getInt(0);
            if(num<=0){
                return;
            }
            cursorisexit.close();
        }
        DebugLog.e("tableUpdate===第一步  查询表"+talbeName+"是否存在");


        String queryCreateSql="select name,sql from sqlite_master  where type='table'  and  name='"+talbeName+"'";

        Cursor  querycursor=	databaseExcute.excuteQuery(queryCreateSql, null);

        if (querycursor != null && querycursor.moveToFirst()) {
            String createsql=querycursor.getString(1);
            createsql=createsql.replace(" "+talbeName," "+talbeName+"_temp");
            databaseExcute.excuteUpdate("DROP TABLE   IF EXISTS   " + talbeName + "_temp ;");

            int result=databaseExcute.excuteUpdate(createsql);

            DebugLog.e( "createsql="+createsql);

            if(ResultType.SUCCESS!=result){
                return;
            }
            querycursor.close();
            DebugLog.e("tableUpdate===第二步  查询表"+talbeName+"创建的sql语句，然后创建临时表");
        }


        String copyDataSql="insert into "+talbeName+"_temp    select  *  from  "+talbeName ;
        int result=databaseExcute.excuteUpdate(copyDataSql);
        DebugLog.e("copyDataSql=" + copyDataSql);

        if(ResultType.SUCCESS!=result){
            return;
        }
        DebugLog.e("tableUpdate===第三步  查询老表"+talbeName+"   向临时表 插入老数据");

       int  dropOldTableResult=  databaseExcute.excuteUpdate("DROP TABLE IF EXISTS   " + talbeName + ";");
        if(ResultType.SUCCESS!=dropOldTableResult){
            return;
        }
        DebugLog.e("tableUpdate===第四步  删除老表"+talbeName+"   ");




        int  createTableResult=	baseDao.createTable();
        if(ResultType.SUCCESS!=createTableResult){
            return;
        }
        DebugLog.e("tableUpdate===第五步  创建新表"+talbeName );

        //==============================查看表中的数据结构==================================
        String getAllColumns="pragma table_info ('"+talbeName+"_temp');";
        ArrayList<String> tablecolums=new ArrayList<String>();
        Cursor getColumsCorsurs=databaseExcute.excuteQuery(getAllColumns, null);
        if (getColumsCorsurs != null && getColumsCorsurs.moveToFirst()) {
            do {
                String column=getColumsCorsurs.getString(1);
                tablecolums.add(column);

            }while (getColumsCorsurs.moveToNext());
            getColumsCorsurs.close();
        }
        //======================================================================================


        //******************************获取当前实体类中所有的列**********************************

         HashMap<String,TableColumnEntity> columnEntityHashMap= baseDao.getReflexEntity().getTableColumnMap();
        int nowBeanColumns=columnEntityHashMap.size();
        Iterator<String> key=  columnEntityHashMap.keySet().iterator();
        ArrayList<String> nowBeanColumsList=new ArrayList<String>();
        while (key.hasNext()){
          String keys=  key.next();
            TableColumnEntity tableColumnEntity=    columnEntityHashMap.get(keys);
            nowBeanColumsList.add(tableColumnEntity.getColumnName());
        }
        nowBeanColumsList.add(baseDao.getReflexEntity().getTableIdEntity().getColumnName());
        //********************************************************************************************

       ArrayList<String> queryColumns= getIntersection(tablecolums,nowBeanColumsList);

        StringBuilder  columsString=new StringBuilder();
        Iterator<String> iterator= queryColumns.iterator();
        while (iterator.hasNext()){
            columsString.append(iterator.next());
            columsString.append(",");
        }
        String cs=columsString.deleteCharAt(columsString.length()-1).toString();

        StringBuilder dataSql=new StringBuilder();
        dataSql.append("insert into ");
        dataSql.append(talbeName);
        dataSql.append("  ( ");
        dataSql.append(cs);
        dataSql.append(" ) ");
        dataSql.append("   select   ");
        dataSql.append(cs);
        dataSql.append("    from  ");
        dataSql.append(talbeName);
        dataSql.append("_temp");

        int dataCopyresult=	databaseExcute.excuteUpdate(dataSql.toString());
        DebugLog.e("copyDataSql=" + dataSql);

        if(dataCopyresult!=ResultType.SUCCESS){

            return;
        }
        DebugLog.e( "tableUpdate===第六步  查询临时表 " + talbeName + "_temp   向新标 插入临时表数据");

      int  dropresultLinshi=  databaseExcute.excuteUpdate("DROP TABLE   IF EXISTS   " + talbeName + "_temp ;");
        if(dropresultLinshi==ResultType.FAIL ){
            return;
        }
        DebugLog.e( "tableUpdate===第七步  删除临时表 ");

    }

    private  ArrayList<String>  getIntersection(ArrayList<String> table,ArrayList<String> bean){
        ArrayList<String>  result=new ArrayList<String>();
        for (String colums:table){
            if(bean.contains(colums)){
                result.add(colums);
            }
        }
        return  result;
    }



}
