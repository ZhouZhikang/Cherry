package cherry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cherry.action.model.ViewTag;

/**
 * Created by aqi on 15/7/13.
 */
public class CherryHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cherry.db";
    private static final int DATABASE_VERSION = 4;
    private static final String[] TABLE_NAME = {"tag"};

    public CherryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("dbhelper", "dbhelperdbhelperdbhelperdbhelper");
        db.execSQL("create table tag (t_id varchar(50) primary key, t_name varchar(200));");
        db.execSQL("create table chosentag (c_id integer primary key autoincrement, t_id varchar(50), t_name varchar(200), u_id varchar(225));");
        db.execSQL("create table collection (c_id integer primary key autoincrement, user_id varchar(225), news_id varchar(50));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String name : TABLE_NAME) {
            db.execSQL("DROP TABLE IF EXISTS " + name + ";");
        }
        onCreate(db);
    }

    //chosentag
    //select
    public List<ViewTag> selectChosenTagByUserid(String userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from chosentag where u_id=?", new String[]{userid});
        List<ViewTag> list = new ArrayList<ViewTag>();
        cursor.moveToFirst();
        for(int i = 0 ; i < cursor.getCount(); i++){
            ViewTag tag = new ViewTag();
            tag.setTagid(cursor.getString(cursor.getColumnIndex("t_id")));
            tag.setTagname(cursor.getString(cursor.getColumnIndex("t_name")));
            list.add(tag);
            cursor.moveToNext();
        }
        return list;
    }

    //insert
    public void insertChosenTag(ViewTag viewTag,String userid) {
        ContentValues cv = new ContentValues();
        cv.put("t_id", viewTag.getTagid());
        cv.put("t_name", viewTag.getTagname());
        cv.put("u_id", userid);

        getWritableDatabase().insert("chosentag", null, cv);
    }

    //delete
    public void deleteChosenTag(String tagid, String userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("chosentag", "t_id=? and u_id=?", new String[]{tagid, userid});
    }

    //tag
    //select
    public List<ViewTag> selectTag() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tag",null,null,null,null,null,null);
        List<ViewTag> list = new ArrayList<ViewTag>();
        cursor.moveToFirst();
        for(int i = 0 ; i < cursor.getCount(); i++){
            ViewTag tag = new ViewTag();
            tag.setTagid(cursor.getString(cursor.getColumnIndex("t_id")));
            tag.setTagname(cursor.getString(cursor.getColumnIndex("t_name")));
            list.add(tag);
            cursor.moveToNext();
        }
        return list;
    }

    //insert
    public void insertTag(ViewTag viewTag) {
        ContentValues cv = new ContentValues();
        cv.put("t_id", viewTag.getTagid());
        cv.put("t_name", viewTag.getTagname());

        getWritableDatabase().insert("tag", null, cv);
    }

    //delete
    public void deleteTag(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("tag", "t_id=?", new String[]{id});
    }

    //collection
    //init
    public void initCollection(String userid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("collection","user_id=?",new String[]{userid});
    }

    //isExist
    public boolean isExistCollection(String userid, String newsid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from collection where user_id=? and news_id=?", new String[]{userid, newsid});
        if(cursor.moveToFirst() == true){
            return true;
        }else{
            return false;
        }

    }

    //insert
    public void insertCollection(String userid, String newsid){
        ContentValues cv = new ContentValues();
        cv.put("user_id", userid);
        cv.put("news_id", newsid);

        getWritableDatabase().insert("collection", null, cv);
    }

    //delete
    public void deleteCollection(String userid,String newsid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("collection","user_id=? and news_id=?",new String[]{userid,newsid});
    }

}
