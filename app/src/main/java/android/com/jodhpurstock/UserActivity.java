package android.com.jodhpurstock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {

    EditText itemsearch;
    TextView i,q,p;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        itemsearch = (EditText) findViewById(R.id.search_it);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        q = (TextView) findViewById(R.id.item_quantity);
        i = (TextView) findViewById(R.id.name_item);
        p = (TextView) findViewById(R.id.item_price);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("stock");
    }
    public void goSearch(View v) {
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
