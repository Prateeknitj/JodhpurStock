package android.com.jodhpurstock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSIONS = 1;
    EditText itemsearch;
    TextView i,q,p;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForPermissions(getApplicationContext(),Home.this);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // itemsearch = (EditText) findViewById(R.id.name_item);
        itemsearch = (EditText) findViewById(R.id.search_it);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        q = (TextView) findViewById(R.id.item_quantity);
        i = (TextView) findViewById(R.id.name_item);
        p = (TextView) findViewById(R.id.item_price);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("stock");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.home, menu);
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

        if (id == R.id.nav_home) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        } else if (id == R.id.nav_stock) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        } else if (id == R.id.nav_app) {
            startActivity(new Intent(getApplicationContext(),ApplicationActivity.class));
        }
        else if (id == R.id.nav_new_app) {
            startActivity(new Intent(getApplicationContext(),NewApplication.class));
        }
        else if(id == R.id.nav_return_stock) {
            startActivity(new Intent(getApplicationContext(),ReturnActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static void checkForPermissions(Context context, Activity activity)
    {
        //if the permissions are already given to the app
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            //-Toast.makeText(context,"Permission granted",Toast.LENGTH_LONG).show();
        }
        //if the location permission is not given ask for it in run time
        else {
            //  Toast.makeText(getContext(),"Please grant the Permissions",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA},
                    PERMISSIONS);// from this the control goes to the main activity's(i.e. here send_detail/ carry detail) onRequestPermissionsResult
        }
    }



    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults,Context context,Activity activity) {
        if (requestCode == PERMISSIONS) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.WRITE_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //checkForStoragePermission(context,activity);
            }
        }
    }

    public void SearchThis(View v) {
        final String val_item = itemsearch.getText().toString();
        if(!TextUtils.isEmpty(val_item)) {
            progressBar.setVisibility(View.VISIBLE);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                    /*Log.v("tmz",""+ chidSnap.getKey()); //displays the key for the node
                    Log.v("tmz",""+ chidSnap.child("market_name").getValue());   //gives the value for given keyname*/
                        String ppitem_ = String.valueOf(chidSnap.child("ppitem").getValue());
                        String item_p = String.valueOf(chidSnap.child("item").getValue());
                        String quantity = String.valueOf(chidSnap.child("quantity").getValue());
                        if(!val_item.isEmpty() && item_p.equals(val_item) ) {
                            i.setText(item_p);
                            q.setText(quantity);
                            p.setText(ppitem_);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
