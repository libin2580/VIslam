package com.meridian.voiceofislam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Anvin on 4/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME="audio_db";
    static final int DB_VERSION=1;
    static final String TABLE_NAME="clicked_audios";
    static final String TABLE_MEMBER_ID="id";
    static final String TABLE_MEMBER_NAME="subject";
    static final String TABLE_MEMBER_DATE="added_date";


    static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ("+TABLE_MEMBER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+TABLE_MEMBER_NAME+" VARCHAR,"+TABLE_MEMBER_DATE+" VARCHAR)";
    static final String DELETE_TABLE="DROP TABLE IF EXISTS "+TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        System.out.println("table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        System.out.println("table deleted");
    }

public int insertData(String name,String add_date){
    System.out.println("name in insertData : "+name);
    SQLiteDatabase db=this.getWritableDatabase();
    db.beginTransaction();
    ContentValues cv;
    int i=0;
    try{
        cv=new ContentValues();
        cv.put(TABLE_MEMBER_NAME,name);
        cv.put(TABLE_MEMBER_DATE,add_date);


        db.insert(TABLE_NAME,null,cv);
        i=1;
        System.out.println("value of i : "+i);
        db.setTransactionSuccessful();
    }catch (Exception e){
        e.printStackTrace();
        i=0;
    }
    finally {
        db.endTransaction();
        db.close();
    }
    return i;
}


public ArrayList<TableModel> getAllDatas(){
    ArrayList<TableModel> passingArrey = new ArrayList<>();
    TableModel tm = new TableModel();
    SQLiteDatabase db = this.getReadableDatabase();
    try {

        db.beginTransaction();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                tm = new TableModel();
                tm.setId(c.getString(c.getColumnIndex(TABLE_MEMBER_ID)));
                tm.setName(c.getString(c.getColumnIndex(TABLE_MEMBER_NAME)));
                tm.setDate(c.getString(c.getColumnIndex(TABLE_MEMBER_DATE)));

                System.out.println("---------------------------------------------------------------------------------");
                System.out.println("ID = " + c.getString(c.getColumnIndex(TABLE_MEMBER_ID)));
                System.out.println("NAME = " + c.getString(c.getColumnIndex(TABLE_MEMBER_NAME)));
                System.out.println("DATE = " + c.getString(c.getColumnIndex(TABLE_MEMBER_DATE)));
                System.out.println("---------------------------------------------------------------------------------");

                passingArrey.add(tm);
            }
        }
        db.setTransactionSuccessful();
    }catch (Exception e){
        e.printStackTrace();
    }
    finally {
        db.endTransaction();
        db.close();
    }
    return  passingArrey;

}


    public int checkExistance(String mem_name){
        SQLiteDatabase db = this.getReadableDatabase();
        int val=0;
        try {

            db.beginTransaction();
            String selectQuery = "SELECT "+TABLE_MEMBER_ID+" FROM "+TABLE_NAME+" WHERE "+TABLE_MEMBER_NAME+"='"+mem_name+"'";
            System.out.println("selectQuery : "+selectQuery);
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    val=Integer.parseInt(c.getString(c.getColumnIndex(TABLE_MEMBER_ID)));
                    System.out.println("---------------------------------------------------------------------------------");
                    System.out.println("ID = " + c.getString(c.getColumnIndex(TABLE_MEMBER_ID)));
                    System.out.println("---------------------------------------------------------------------------------");

                }
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
        return  val;

    }

public void DeleteRow(String id){
    System.out.println("Id inside DatabaseHelper : "+id);
    System.out.println("Deleted id is "+id);
    SQLiteDatabase db=this.getWritableDatabase();
    try{

        db.beginTransaction();
        db.delete(TABLE_NAME,TABLE_MEMBER_ID+" = ?", new String[]{id});
        db.setTransactionSuccessful();

    }catch (Exception e){
        e.printStackTrace();
    }
    finally {
db.endTransaction();
        db.close();

    }
}

}
