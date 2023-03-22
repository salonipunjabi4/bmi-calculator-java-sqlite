package com.sp.bmicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase db;
    DatabaseHandler(Context context)
    {
        super(context,"bmidb",null,1);
        this.context = context;
        db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table bmi (id INTEGER, bmi text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addDetails(int id, String bmi)
    {
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("bmi", bmi);
        long rid = db.insert("bmi",null , values);
        if (rid < 0)
            Toast.makeText(context, "Insert issue", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Record inserted", Toast.LENGTH_SHORT).show();
    }

    public String getDetails()
    {
     Cursor cursor = db.query("bmi",null,null,null,null,null,null);
     StringBuffer sb = new StringBuffer();
     cursor.moveToFirst();
     if(cursor.getCount() > 0)
     {
         do {
             String bmi = cursor.getString(1);
             sb.append("BMI = "+bmi+"\n");

         }while(cursor.moveToNext());
     }
     else
     {
         sb.append("No records to show");
     }
     return  sb.toString();
    }
}
