package com.example.norik.chatrealtime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private String DATABASE_NAME = "data_message";
    private static final int DATABASE_VERSION = 1;
    String TABLE_MESSAGE = "";
    private static final String NAME = "name";
    private static final String MESSAGE  = "message";
    ArrayList<item_message_list_data> arrMsg = new ArrayList<>();

    public MyDatabaseHelper(Context context,String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);

        this.DATABASE_NAME = databaseName;
    }
    public void addTableName(String tableName)
    {
        this.TABLE_MESSAGE = tableName;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS";
        sql+=TABLE_MESSAGE;
        sqLiteDatabase.execSQL(sql);

        // Recreate
        onCreate(sqLiteDatabase);
    }

    public void addMessage(String name,String msg)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME,name);
        values.put(MESSAGE,msg);
        db.insert(TABLE_MESSAGE,null,values);
    }
    public ArrayList<item_message_list_data> getAllMessage()
    {
        arrMsg.clear();
        String sql = "SELECT  * FROM ";
        sql+=TABLE_MESSAGE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                item_message_list_data messageLine = new item_message_list_data();
                messageLine.setName(cursor.getString(0));
                messageLine.setMessage(cursor.getString(1));
                arrMsg.add(messageLine);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arrMsg;
    }
    boolean tableIsExisted()
    {
        String sql = "SELECT count(*) FROM "+DATABASE_NAME+" WHERE type='table' AND name='"+TABLE_MESSAGE+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
    public void createTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "CREATE TABLE IF NOT EXISTS "+TABLE_MESSAGE;
        SQL = SQL+"( "+ NAME+" TEXT ,";
        SQL = SQL+ MESSAGE +" TEXT )";
        db.execSQL(SQL);
    }
}
