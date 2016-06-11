package com.denny.cmp;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.PrintWriterPrinter;

import java.io.PrintWriter;

/**
 * Created by hasee on 2016/4/27.
 */
public class DatabaseDump {
    public static void dump(SQLiteOpenHelper helper,String tableName){
        Cursor cursor = helper.getReadableDatabase().query(tableName,null,null,null,null,null,null);
        PrintWriter pw = new PrintWriter(System.out);
        printColums(cursor,pw);
        while(cursor.moveToNext()){
            printContent(cursor,pw);
        }
        cursor.close();
        pw.flush();
    }

    private static void printContent(Cursor cursor, PrintWriter pw) {
        final int count = cursor.getColumnCount();
        pw.append("| ");
        for (int i=0;i<count;++i){
            pw.append(cursor.getString(i)+" | ");
        }
        pw.println();
    }

    private static void printColums(Cursor cursor, PrintWriter pw) {
        final int count = cursor.getColumnCount();
        pw.println();
        pw.append("| ");
        for (int i=0;i<count;++i){
            pw.append(cursor.getColumnName(i)+" | ");
        }
        pw.println();
    }
}
