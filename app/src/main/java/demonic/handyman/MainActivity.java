package demonic.handyman;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.R.id.list;
import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Location loc = new Location("");
    GPSTracker gps;

    Typeface semilight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(checkLocationPermission() == false)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);



        final Handy Db = new Handy(this);
        Db.refreshTable();

        Db.insertContact("1", "Ramu", "9742021923", "ramu@outlook.com", "mumbai", "Carpenter", 4, "18.9750", "72.8258", 5);
        Db.insertContact("2", "Gundu", "962086923", "gundu@outlook.com", "mumbai", "Plumber", 4, "28.6139", "77.2090", 5);


        Db.insertRating("1", 4, "Excellent");
        Db.insertRating("1", 5, "Brilliant motherfucker");
        Db.insertRating("1", 4, "MindBlasting");
        Db.insertRating("1", 3, "Alright");
        Db.insertRating("1", 1, "Balls!");

        Spinner s1 = (Spinner) findViewById(R.id.spinner);
        AwesomizeSpinner(s1 , Arrays.asList(getResources().getStringArray(R.array.Proff)));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num;
                Spinner s1 = (Spinner) findViewById(R.id.spinner);
                num = s1.getSelectedItem().toString();

                int flag = 0;

                if (num.equals("11th") || num.equals("12th"))
                    num = num.substring(0, num.length() - 2);
                Cursor r = null;
                Cursor r1 = null;
                List<HandyMan> list = new ArrayList<>();
                try {
                    r = Db.getTutors(num);
                    int x = r.getCount();
                    if (x != 0) {
                        HandyMan handyMan = new HandyMan();
                        list = handyMan.dataFromCursor(r);
                        flag = 0;
                    }
                    else
                    {
                        flag = 1;
                    }
                } finally {
                    if (r != null) {
                        r.close();
                    }
                }

                if (flag == 0)
                {
                    RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvTutors);
                    TextView t = (TextView) findViewById(R.id.err);
                    t.setVisibility(View.GONE);

                    gps = new GPSTracker(MainActivity.this);

                    // Check if GPS enabled
                    if(gps.canGetLocation()) {

                        loc.setLatitude(gps.getLatitude());
                        loc.setLongitude(gps.getLongitude());

                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }


                    for (int i = 0; i < list.size(); i++)
                    {
                        list.get(i).calDist(loc);
                    }

                    Collections.sort(list, new Distcomparator());
                    Collections.reverse(list);

                    for (int i = 0; i < list.size(); i++)
                    {
                        HandyMan HandyMan;
                        HandyMan = list.get(i);
                        r1 = Db.setRatings(HandyMan.getId());
                        HandyMan.getRatingsFromCursor(r1);
                        list.set(i, HandyMan);
                    }
                    r1.close();

                    rvContacts.setVisibility(View.VISIBLE);
                    // Create adapter passing in the sample user data
                    HandyMansAdapter handyMansAdapter = new HandyMansAdapter(list);
                    // Attach the adapter to the recyclerview to populate items
                    rvContacts.setAdapter(handyMansAdapter);
                    // Set layout manager to position the items
                    rvContacts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
                else
                {
                    TextView t = (TextView) findViewById(R.id.err);
                    t.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public <ViewGroup> void AwesomizeSpinner(Spinner mySpinner, List ar) {

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ar) {
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                return v;
            }

            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter1);
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public class Distcomparator implements Comparator<HandyMan>
    {
        public int compare(HandyMan o1, HandyMan o2)
        {
            return Double.compare(o2.getDist(), o1.getDist());
        }
    }
}
