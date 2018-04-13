package com.example.capk.antivirus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by capk on 4/13/18.
 */

public class DBContract extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "logs_db";

    public DBContract(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBSchema.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DBSchema.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String packageName,String dexCheck,String versionName,String versionNumber){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBSchema.COLUMN_PACKAGE_NAME,packageName);
        values.put(DBSchema.COLUMN_DEX_CHECK,dexCheck);
        values.put(DBSchema.COLUMN_VERSION_NAME,versionName);
        values.put(DBSchema.COLUMN_VERSION_NUMBER,versionNumber);

        sqLiteDatabase.insert(DBSchema.TABLE_NAME,null,values);
        sqLiteDatabase.close();

    }
    public void dropTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DBSchema.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
