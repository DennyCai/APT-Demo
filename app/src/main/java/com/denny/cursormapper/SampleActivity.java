package com.denny.cursormapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.denny.cmp.CursorMapper;
import com.denny.cmp.DatabaseDump;
import com.denny.cursormapper.model.UserModel;
import com.denny.cursormapper.table.UserColums;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        SQLiteOpenHelper helper = new SQLiteOpenHelper(this,"test",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(UserColums.CREATE_TABLE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db,
                                  int oldVersion,
                                  int newVersion) {
            }
        };
        SQLiteDatabase db  = helper.getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(UserColums.ID,1);
        values.put(UserColums.NAME,"Jerry");
        values.put(UserColums.AGE,20);
        db.insert(UserColums.TABLE_NAME,null,values);
        values = new ContentValues();
        values.put(UserColums.ID,2);
        values.put(UserColums.NAME,"Denny");
        values.put(UserColums.AGE,21);
        db.insert(UserColums.TABLE_NAME,null,values);
        values = new ContentValues();
        values.put(UserColums.ID,3);
        values.put(UserColums.NAME,"Tommy");
        values.put(UserColums.AGE,22);
        db.insert(UserColums.TABLE_NAME,null,values);

        db.setTransactionSuccessful();
        db.endTransaction();

        Cursor cursor = db.query(UserColums.TABLE_NAME,UserColums.COLUMNS,
                null,null,null,null,null);
        Log.i("",cursor.getCount()+"");
        while (cursor.moveToNext()){
            UserModel model = CursorMapper.load(cursor, UserModel.class);
            Log.i("",model.toString());
        }
        DatabaseDump.dump(helper,UserColums.TABLE_NAME);


    }
}
