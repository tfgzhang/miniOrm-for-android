package com.miniorm.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteTransactionListener;
import android.provider.Settings;

import com.miniorm.MiniOrm;
import com.miniorm.dao.utils.ResultType;
import com.miniorm.debug.DebugLog;

import java.util.concurrent.atomic.AtomicInteger;

public class SQLHelper extends SQLiteOpenHelper {

    private static SQLHelper sqlHelper;
    SQLiteDatabase db;
    static String DbName = "";
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private static  java.util.concurrent.Semaphore semaphoreTransaction = new java.util.concurrent.Semaphore(1);




    protected SQLHelper(Context context, int version, String dbname) {
        super(context, dbname, null, version);

        db=openDatabase();
    }
    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            db =getWritableDatabase();
        }
        return db;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            db.close();
        }
    }



    public static synchronized SQLHelper getInstance() {


        if (DbName != null && DbName.equals(MiniOrm.dbName)) {
            if (sqlHelper == null) initDbHelper();

            return sqlHelper;
        } else {
            sqlHelper = null;
            initDbHelper();
            return sqlHelper;
        }
    }

    private static void initDbHelper() {
        if (sqlHelper == null) {
            DebugLog.e("initDbHelper()  DbName=" + DbName + "  MiniOrm.dbName=" + MiniOrm.dbName);
            DbName = MiniOrm.dbName;

		/*	if(StringUtils.isNull(MiniOrm.dbName)){
                MiniOrm.dbName="123";
				MiniOrm.version=1;
			}
*/

		/*	if(StringUtils.isNull(DbName)||MiniOrm.version==0){
                try {
					File path=StroageUtils.getCacheFile(MiniOrm.application,"db.txt");
					String json=FileUtils.readFile(path);
					JSONObject jsonObject=new JSONObject(json);
					String name=jsonObject.getString("name");
					int version=jsonObject.getInt("version");
					if(StringUtils.isNull(name)){
					}else {
						DbName=name;
						MiniOrm.dbName=name;
						MiniOrm.version=version;
					}


				} catch (Exception e) {
					e.printStackTrace();
					DbName="ML";
					MiniOrm.version=1;
				}

			}else {
				File path=StroageUtils.getOwnCacheDirectory(MiniOrm.application,"db.txt");
				try {
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("name",DbName);
					jsonObject.put("version",MiniOrm.version);
					FileUtils.writeFile(jsonObject.toString(),path);
				}catch (Exception e){

				}

			}*/

            sqlHelper = new SQLHelper(MiniOrm.application, MiniOrm.version, MiniOrm.dbName);
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void beginTransaction() {
        try {
            semaphoreTransaction.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
          db.beginTransaction();
     }

    public void endTransaction() {
        synchronized (db){
            if(db.inTransaction())  db.endTransaction();
        }
        semaphoreTransaction.release();

    }

    public int execSQL(String sql) {
        try {
            long timer1= System.currentTimeMillis();

            db.execSQL(sql);
            timer+=(System.currentTimeMillis()-timer1);

            return ResultType.SUCCESS;
        } catch (Exception e) {
            return ResultType.FAIL;
        }
    }

    public static long timer;

    public int execSQL2(SQLiteStatement sqLiteStatement) {
        try {
            long timer1= System.currentTimeMillis();
            sqLiteStatement.executeUpdateDelete();
            timer+=(System.currentTimeMillis()-timer1);
            return ResultType.SUCCESS;
        } catch (Exception e) {
            return ResultType.FAIL;
        }
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                new TableUpgrade().update();
            }
        }).start();

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new TableUpgrade().update();
            }
        }).start();

    }

    public void insert(String table, String nullColumnHack, String... str) {

        ContentValues values = new ContentValues();
        values.put("name", str[0]);
        //db.execSQL(sql);
        db.insert("stu", nullColumnHack, values);
    }

    public Cursor query(String string, Object object, Object object2,
                        Object object3, Object object4, Object object5, Object object6) {
        // TODO Auto-generated method stub


        return db.query("stu", null, null, null, null, null, null);
    }


    public Cursor rawQuery(String sql, String... selectionArgs) throws SQLiteException {

        return db.rawQuery(sql, selectionArgs);
    }

    public int update(String table, String whereClause, String[] whereArgs, String... values) {

        return db.update(table, null, whereClause, whereArgs);


    }


    public void setTransactionSuccessful() {
        if(db.isDbLockedByCurrentThread()&&db.inTransaction())
        db.setTransactionSuccessful();
    }



}
