package com.kuk.myreceipt.model;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KUK on 9/4/2560.
 */



public class ReceiptSQLite extends SQLiteOpenHelper {
    public ReceiptSQLite(Context context) {
        super(context, "receipt", null, 2);
    }

    public void deleteFile(String imagePath, String thumbnailPath){
        File fullimg = new File (imagePath);
        File thumbimg = new File(thumbnailPath);
        boolean fulldeleted = fullimg.delete();
        boolean thumbdeleted = thumbimg.delete();
    }

    public void deleteReceipt(int id, String imagePath, String thumbnailPath){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM receipt WHERE rid =" +id;
        db.execSQL(sql);

       deleteFile(imagePath,thumbnailPath);
    }

    public void updateReceipt(int rid,String title, String dateTime,String filePath,int price, String description,String thumbnail){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE receipt SET title = \"" + title + "\", dateTime = \"" + dateTime + "\" ,filePath = \"" + filePath + "\",price = \"" + price + "\" ,description = \"" + description + "\",thumbnailPath = \"" + thumbnail + "\" WHERE rid = "+ rid ;
        Log.d("receipt","update"+" "+rid+" "+ title+ " "+ price);
        db.execSQL(sql);

    }

    public void saveReceipt( String title, String dateTime,String filePath,int price, String description,String thumbnail){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO receipt (title, dateTime,filePath,price,description,thumbnailPath) VALUES ( \""+title+"\" , \""+dateTime+"\", \"" + filePath + "\", \"" + price + "\", \"" + description +"\", \"" + thumbnail+ "\")";
        Log.d("receipt","insert"+ title+ " "+ price);
        db.execSQL(sql);
    }

    public List<ReceiptModel> getReceiptList(){
       Log.d("startList","===start list==");
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM receipt";
        if(db==null)
        {
            Log.d("GETDB",null);
        }else{Log.d("GETDB","not null");}
        Cursor c = db.rawQuery(sql,null);

        List<ReceiptModel> list = new ArrayList<>();

        if( c == null )return list;

        if( c.moveToFirst() ){
            do{
                int id = c.getInt(0);
                String title = c.getString(1);
                String dateTime = c.getString(2);
                String filePath =c.getString(3);
                int price = c.getInt(4);
                String description = c.getString(5);
                String thumbnailPath = c.getString(6);

                ReceiptModel m = new ReceiptModel(id,title,dateTime,filePath,price,description,thumbnailPath);
                list.add(m);

                Log.d("receiptList","getReceipt: "+m.getRid()+" "+m.getTitle()+" "+ m.getFilePath()+" "+m.getPrice()+" "+m.getDateTime()+" "+m.getThumbnailPath());

            }while( c.moveToNext() );
        }

        return list;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECEIPT_TABLE = "CREATE TABLE receipt (rid INTEGER PRIMARY KEY, title TEXT, dateTime TEXT,filePath TEXT,price INTEGER,description TEXT,thumbnailPath TEXT)";
        db.execSQL(CREATE_RECEIPT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
