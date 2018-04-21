package com.example.capk.antivirus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

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
    public void insertData(String packageName,String dexCheck,String versionName,String versionNumber,String scanStatus,String zdm_Analysis){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBSchema.COLUMN_PACKAGE_NAME,packageName);
        values.put(DBSchema.COLUMN_DEX_CHECK,dexCheck);
        values.put(DBSchema.COLUMN_VERSION_NAME,versionName);
        values.put(DBSchema.COLUMN_VERSION_NUMBER,versionNumber);
        values.put(DBSchema.COLUMN_SCAN_STATUS,scanStatus);
        values.put(DBSchema.COLUMN_ZDM_ANALYSIS, zdm_Analysis);

        sqLiteDatabase.insert(DBSchema.TABLE_NAME,null,values);
        sqLiteDatabase.close();


    }
    public void updateScanData(String packageName,String scanStatus){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.COLUMN_SCAN_STATUS,scanStatus);
        sqLiteDatabase.update(DBSchema.TABLE_NAME,values,"package_name='"+packageName+"'",null );
//        sqLiteDatabase.execSQL("update ScanLog set scan_status = '"+);
        sqLiteDatabase.close();
    }
    public void updateZDMData(String packageName,String scanStatus){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.COLUMN_ZDM_ANALYSIS,scanStatus);
        sqLiteDatabase.update(DBSchema.TABLE_NAME,values,"package_name='"+packageName+"'",null );
//        sqLiteDatabase.execSQL("update ScanLog set scan_status = '"+);
        sqLiteDatabase.close();
    }

    public List<DBSchema> getData(){
        List<DBSchema > dbSchemaList = new LinkedList<DBSchema>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(DBSchema.SELECT_QUERY,null);
        DBSchema dbSchema = null;
        if (cursor.moveToFirst()){
            do {
                dbSchema  = new DBSchema();
                dbSchema.setPackageName(cursor.getString(0));
                dbSchema.setDexCheck(cursor.getString(1));
                dbSchema.setVersionNumber(cursor.getString(3));
                dbSchema.setScanStatus(cursor.getString(4));
                dbSchema.setZdmAnalysis(cursor.getString(5));
                dbSchemaList.add(dbSchema);
            }while (cursor.moveToNext());

        }
        return dbSchemaList;
    }

    public void dropTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DBSchema.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
