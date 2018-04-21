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
    public static final String COLUMN_SCAN_STATUS = "scan_status";
    public static final String COLUMN_ZDM_ANALYSIS = "zdm_analysis";
    public static final String SELECT_QUERY = "Select * from "+TABLE_NAME;

    private String packageName;
    private String dexCheck;
    private String versionNumber;
    private String versionName;
    private String scanStatus;
    private String zdmAnalysis;


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_PACKAGE_NAME + " PRIMARY KEY,"
                    + COLUMN_DEX_CHECK + " TEXT,"
                    + COLUMN_VERSION_NAME + " TEXT,"
                    + COLUMN_VERSION_NUMBER + " TEXT,"
                    + COLUMN_SCAN_STATUS + " TEXT,"
                    + COLUMN_ZDM_ANALYSIS + " TEXT"
                    + ")";


    public DBSchema() {
    }

    public DBSchema(String packageName,String dexCheck,String versionNumber){
        this.packageName = packageName;
        this.dexCheck = dexCheck;
        this.versionNumber = versionNumber;
    }
    public DBSchema(String packageName,String dexCheck,String versionNumber,String scanStatus,String zdmAnalysis){
        this.packageName = packageName;
        this.dexCheck = dexCheck;
        this.versionNumber = versionNumber;
        this.scanStatus = scanStatus;
        this.zdmAnalysis = zdmAnalysis;
    }

    public static String getColumnScanStatus() {
        return COLUMN_SCAN_STATUS;
    }

    public static String getColumnZdmAnalysis() {
        return COLUMN_ZDM_ANALYSIS;
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

    public String getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(String scanStatus) {
        this.scanStatus = scanStatus;
    }

    public String getZdmAnalysis() {
        return zdmAnalysis;
    }

    public void setZdmAnalysis(String zdmAnalysis) {
        this.zdmAnalysis = zdmAnalysis;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
}
