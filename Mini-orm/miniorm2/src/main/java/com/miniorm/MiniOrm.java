package com.miniorm;

import android.app.Application;

import com.miniorm.dao.BaseDao2;

import java.util.ArrayList;

/**
 *
 *  ┏┓　　　┏┓
 ┏┛┻━━━┛┻┓
 ┃　　　　　　　┃ 　
 ┃　　　━　　　┃ ++ + + +
 ██━██ ┃++

 ┃　　　　　　　┃
 ┃　　　┻　　　┃
 ┃　　　　　　　┃
 ┗━┓　　　┏━┛

 ┃　　　┃   Code is far away from bug with the animal protecting　　　　　　
 ┃　　　┃   神兽保佑,代码无bug　
 ┃　　　┗━━━┓
 ┃　　　　　　　┣┓
 ┃　　　　　　　┏┛
 ┗┓┓┏━┳┓┏┛
 ┃┫┫　 ┃┫┫
 ┗┻┛　 ┗┻┛
 author :ml
 qq:1634990276
 email:1634990276@qq.com
 *
 * Created by ML on 2016/9/27.
 */
public class MiniOrm {
        public static  Application application;
        public static  int   version;
        public static  String   dbName;

        private   static ArrayList<Class<BaseDao2>>  daos;

        public  static  void  init(Application application,int versionCode,String dbName){
            MiniOrm.application=application;
            MiniOrm.dbName=dbName;
            MiniOrm.version=versionCode;

        }


    /**
     * 在版本升级的时候，调用传入需要升级的表的 dao文件  class类型
     * @param dao
     */

        public static  void  addUpdateTable(Class dao){
            if(daos==null)daos=new ArrayList<Class<BaseDao2>>();
            daos.add(dao);
        }


        public static ArrayList<Class<BaseDao2>>  getUpdateTables(){
            return daos;
        }

}
