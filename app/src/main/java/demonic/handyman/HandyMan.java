package demonic.handyman;

/**
 * Created by demonic on 13/10/16.
 */

import android.database.Cursor;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class HandyMan {

    private String id;
    private String name;
    private String email;
    private String place;
    private String phone;
    private String proff;
    private int rating;
    private List<Rate_Review> rate_review;
    private Location location = new Location("");
    private double distance;
    private int ratings;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public List<HandyMan> dataFromCursor(Cursor cursor) {
        List<HandyMan> HandyMans = new ArrayList<HandyMan>();
        cursor.moveToFirst();
        HandyMan HandyMan;
        if (cursor != null)
            for (int i = 0; i < cursor.getCount(); i++) {
                HandyMan = new HandyMan();
                HandyMan.id = cursor.getString(cursor.getColumnIndex("id"));
                HandyMan.name = cursor.getString(cursor.getColumnIndex("name"));
                HandyMan.email = cursor.getString(cursor.getColumnIndex("email"));
                HandyMan.place = cursor.getString(cursor.getColumnIndex("place"));
                HandyMan.proff = cursor.getString(cursor.getColumnIndex("profession"));
                HandyMan.phone = cursor.getString(cursor.getColumnIndex("phone"));
                HandyMan.rating = cursor.getInt(cursor.getColumnIndex("rating"));
                HandyMan.ratings = cursor.getInt(cursor.getColumnIndex("ratings"));
                HandyMan.location.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude"))));
                HandyMan.location.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("longitude"))));

                cursor.moveToNext();
                HandyMans.add(HandyMan);
            }
        cursor.close();

        return HandyMans;
    }

    public List<Rate_Review> r() {
        return rate_review;
    }

    public void HandyManFromCursor(Cursor cursor) {

        cursor.moveToFirst();

        id = cursor.getString(cursor.getColumnIndex("id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        email = cursor.getString(cursor.getColumnIndex("email"));
        place = cursor.getString(cursor.getColumnIndex("place"));
        proff = cursor.getString(cursor.getColumnIndex("profession"));
        phone = cursor.getString(cursor.getColumnIndex("phone"));
        rating = cursor.getInt(cursor.getColumnIndex("rating"));
        ratings = cursor.getInt(cursor.getColumnIndex("ratings"));
        location.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude"))));
        location.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("longitude"))));

        cursor.close();
    }

    public void setText(View a2, View a3, View a4) {
        ;

        TextView t1 = (TextView) a2;
        t1.setText(place);

        TextView t2 = (TextView) a3;
        t2.setText(phone);

        RatingBar r = (RatingBar) a4;
        r.setRating((float) rating);

    }

    public Location getLocation() {
        return location;
    }

    public void calDist(Location l1) {
        distance = location.distanceTo(l1);
    }

    public double getDist() {
        return distance;
    }

    public int getrates() {
        return ratings;
    }

    public int getRatingsFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        rating = 0;
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                rating += cursor.getInt(cursor.getColumnIndex("rating"));
                Log.v("TAG", String.valueOf(rating));
                cursor.moveToNext();
            }
            if (cursor.getCount() == 0)
                return 0;
            rating /= cursor.getCount();
            Log.v("TAG", String.valueOf(rating));
        }

        return rating;
    }

    public void incRate() {
        ratings += 1;
    }

    public void ratingsFromCursor(Cursor cursor) {
        List<Rate_Review> r = new ArrayList<Rate_Review>();
        cursor.moveToFirst();
        Rate_Review r1;
        if (cursor != null)
            for (int i = 0; i < cursor.getCount(); i++) {
                r1 = new Rate_Review();
                r1.id = cursor.getString(cursor.getColumnIndex("id"));
                r1.rate = cursor.getInt(cursor.getColumnIndex("rating"));
                r1.review = cursor.getString(cursor.getColumnIndex("review"));

                Log.d("Rating", r1.review.toString());
                cursor.moveToNext();
                r.add(r1);
            }
        cursor.close();

        rate_review = r;
    }

    public List<Rate_Review> getRate_review() {
        return rate_review;
    }
}