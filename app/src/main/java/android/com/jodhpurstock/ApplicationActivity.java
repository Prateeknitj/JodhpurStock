package android.com.jodhpurstock;

import android.app.*;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ApplicationActivity extends AppCompatActivity {

    EditText sarch,it;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView n,q,i,s;
    final int[] flag = new int[1];
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        sarch = (EditText) findViewById(R.id.search);
        it = (EditText) findViewById(R.id.editItem);
        database = FirebaseDatabase.getInstance();
        flag[0] = 0;
        myRef = database.getReference("application");
        n = (TextView) findViewById(R.id.name_person);
        q = (TextView) findViewById(R.id.quantity_item);
        i = (TextView) findViewById(R.id.item_name);
        s = (TextView) findViewById(R.id.boolean_var);
    }


    public void Search(View v) {
        if(!TextUtils.isEmpty(sarch.getText().toString()) && !TextUtils.isEmpty(it.getText().toString())) {
            progressBar.setVisibility(View.VISIBLE);
            final String names = sarch.getText().toString().toLowerCase();
            final String itx = it.getText().toString().toLowerCase();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                    /*Log.v("tmz",""+ chidSnap.getKey()); //displays the key for the node
                    Log.v("tmz",""+ chidSnap.child("market_name").getValue());   //gives the value for given keyname*/
                        String name_p = String.valueOf(chidSnap.child("name").getValue()).toLowerCase();
                        String item_p = String.valueOf(chidSnap.child("item").getValue()).toLowerCase();
                        String quantity = String.valueOf(chidSnap.child("quantity").getValue());
                        String isSubmitted = String.valueOf(chidSnap.child("submitted").getValue());
                        if(name_p.equals(names.toLowerCase()) && item_p.equals(itx.toLowerCase()) && flag[0] == 0) {
                            n.setText(name_p);
                            i.setText(item_p);
                            q.setText(quantity);
                            s.setText(isSubmitted);
                            flag[0] = 1;
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
