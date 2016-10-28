package com.miniorm;

import android.app.Application;

import com.miniorm.dao.BaseDao;

import java.util.ArrayList;

/**
 * Created by admin on 2016/9/27.
 */
public class MiniOrm {
        public static  Application application;
        public static  int   version;
        public static  String   dbName;

        private   static ArrayList<Class<BaseDao>>  daos;

        public  static  void  init(Application application,int versionCode,String dbName){
            MiniOrm.application=application;
            MiniOrm.dbName=dbName;
            MiniOrm.version=versionCode;

        }


        public static  void  addUpdateTable(Class dao){
            if(daos==null)daos=new ArrayList<Class<BaseDao>>();
            daos.add(dao);
        }


        public static ArrayList<Class<BaseDao>>  getUpdateTables(){
            return daos;
        }

}
