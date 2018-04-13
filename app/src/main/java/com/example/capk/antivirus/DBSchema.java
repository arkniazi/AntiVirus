package com.example.capk.antivirus;

/**
 * Created by capk on 4/13/18.
 */

public class DBSchema {

    public static final String TABLE_NAME = "Scanlog";

    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_DEX_CHECK = "dex_check";
    public static final String COLUMN_VERSION_NAME = "version_name";
    public static final String COLUMN_VERSION_NUMBER = "version_number";

    private String packageName;
    private String dexCheck;
    private String versionNumber;


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_PACKAGE_NAME + " PRIMARY KEY,"
                    + COLUMN_DEX_CHECK + " TEXT,"
                    + COLUMN_VERSION_NAME + " TEXT,"
                    + COLUMN_VERSION_NUMBER + " TEXT"
                    + ")";


    public DBSchema() {
    }

    public DBSchema(String packageName,String dexCheck,String versionNumber){
        this.packageName = packageName;
        this.dexCheck = dexCheck;
        this.versionNumber = versionNumber;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDexCheck() {
        return dexCheck;
    }

    public void setDexCheck(String dexCheck) {
        this.dexCheck = dexCheck;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
}
