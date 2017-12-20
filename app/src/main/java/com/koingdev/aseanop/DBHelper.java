package com.koingdev.aseanop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.koingdev.aseanop.Models.FavoriteModel;

import java.util.ArrayList;

/**
 * Created by SSK on 09-May-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "ScholarshipDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tbMyFavorite(ID TEXT, Title TEXT, Dates TEXT, Url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbMyFavorite");
        onCreate(db);
    }
    public void insertData (FavoriteModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", model.id);
        contentValues.put("Title", model.title);
        contentValues.put("Dates", model.date);
        contentValues.put("Url", model.url);
        db.insert("tbMyFavorite", null, contentValues);
        db.close();
    }
    public ArrayList<FavoriteModel> getAllData(){
        ArrayList<FavoriteModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbMyFavorite", null);
        if(cursor.moveToFirst()){
            do{
                FavoriteModel model = new FavoriteModel();
                model.id = cursor.getString(0);
                model.title = cursor.getString(1);
                model.date = cursor.getString(2);
                model.url = cursor.getString(3);
                list.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public void deleteData(FavoriteModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbMyFavorite", "ID" + " = ?", new String[] { model.id });
        db.close();
    }
    public boolean searchData(String id){
        String query = "SELECT * FROM tbMyFavorite WHERE ID='" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        return false;
    }
}
