package com.example.book_v2.data.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;



public class database extends SQLiteOpenHelper {
    public static final String DB_NAME = "App_Reports";
    public static final int DB_VERSION = 4;
    public static final String TABLE_NAME = "report";
    public static final String ID_COL = "id";
    public static final String PAGENUM_COL = "pageNumer";
    public static final String LETTERNAME_COL = "letterName";
    public static final String SCORE_COL = "score";


    public database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_NAME + " " +
                        "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        "" + LETTERNAME_COL + " TEXT  ," +
                        "" + PAGENUM_COL + " INTEGER Unique ," +
                        "" + SCORE_COL + " REAL )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long dbSize() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);


    }

    public boolean insert(ReportData data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LETTERNAME_COL, data.getLetter());
        contentValues.put(PAGENUM_COL, data.getPageNum());
        contentValues.put(SCORE_COL, data.getScore());

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateItem(ReportData data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LETTERNAME_COL, data.getLetter());
        contentValues.put(PAGENUM_COL, data.getPageNum());
        contentValues.put(SCORE_COL, data.getScore());
        String args[] = {String.valueOf(data.getPageNum())};
        int result = sqLiteDatabase.update(TABLE_NAME, contentValues, "pageNumer=?", args);

        return result != 0;
    }

    @SuppressLint("Range")
    public ArrayList<ReportData> getAllData() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ArrayList<ReportData> data = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ID_COL));
                String letter = cursor.getString(cursor.getColumnIndex(LETTERNAME_COL));
                double score = cursor.getDouble(cursor.getColumnIndex(SCORE_COL));
                int pagenum = cursor.getInt(cursor.getColumnIndex(PAGENUM_COL));
                ReportData c = new ReportData(id, pagenum, letter, score);
                data.add(c);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return data;
    }

    //by Letter
    @SuppressLint("Range")
    public ReportData getScoreOfLetter(int pageNum) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                PAGENUM_COL + " LIKE ?", new String[]{String.valueOf(pageNum)}, null);
        if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(ID_COL));
                String letter = cursor.getString(cursor.getColumnIndex(LETTERNAME_COL));
                double score = cursor.getDouble(cursor.getColumnIndex(SCORE_COL));
                int pagenum = cursor.getInt(cursor.getColumnIndex(PAGENUM_COL));
                ReportData c = new ReportData(id, pagenum, letter, score);
                cursor.close();
                return c;
            }
        return null;
    }

}
