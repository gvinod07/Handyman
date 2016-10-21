package demonic.handyman;

/**
 * Created by demonic on 13/10/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.ref.SoftReference;

public class Handy extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TutorData.db";

    public static final String TUTOR_TABLE_NAME = "tutor";
    public static final String REVIEW_TABLE_NAME = "ratings";


    public Handy(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table tutor " +
                        "(id text primary key, name text,phone text,email text, place text, profession text, rating integer, latitude text, longitude text, ratings integer);"
        );

        db.execSQL(
                "create table ratings " +
                        "(id text, rating integer, review text);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS tutor");
        db.execSQL("DROP TABLE IF EXISTS ratings");
        onCreate(db);
    }

    public boolean insertContact(String id, String name, String phone, String email, String place, String proff, int rating, String latitude, String longitude, int ratings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("place", place);
        contentValues.put("profession", proff);
        contentValues.put("rating", rating);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("ratings", ratings);
        db.insert("tutor", null, contentValues);
        return true;
    }

    public boolean insertRating(String id, int rating, String review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("rating", rating);
        contentValues.put("review", review);
        db.insert("ratings", null, contentValues);
        return true;
    }

    public Cursor getTutors(String sub) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tutor where profession = '" + sub + "';", null);
        return res;
    }

    public Cursor getTutorsById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tutor where id = '" + id + "' ;", null);
        return res;
    }

    public boolean findTutorsById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tutor where id = '" + id + "' ;", null);
        if (res != null) {
            res.close();
            return true;
        }

        res.close();
        return false;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TUTOR_TABLE_NAME);
        return numRows;
    }

    public boolean updateTutor(String id, String name, String phone, String email, String place, String subject, int rating, String latitude, String longitude, int ratings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("place", place);
        contentValues.put("profession", subject);
        contentValues.put("rating", rating);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("ratings", ratings);
        db.update("tutor", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public boolean updateRatings(String id, int ratings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ratings", ratings);
        db.update("tutor", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public Cursor setRatings(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select rating from ratings where id = " + id + " ;", null);
        return res;
    }

    public Integer deleteTutor(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tutor",
                "id = ? ",
                new String[]{id});
    }

    public Cursor getAllTutors() {

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tutor", null);
        res.moveToFirst();

        return res;
    }

    public Cursor getRatings(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ratings where id = '" + id + "' ;", null);
        res.moveToFirst();

        return res;
    }

    public void refreshTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS tutor");
        db.execSQL("DROP TABLE IF EXISTS ratings");
        onCreate(db);
    }
}