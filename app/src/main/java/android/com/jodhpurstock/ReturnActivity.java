package android.com.jodhpurstock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ReturnActivity extends AppCompatActivity {

    EditText name_p,item_p,quty_p;
    private DatabaseReference mFirebaseDatabase;
    final int[] flag = new int[1];
    final int[] flag1 = new int[1];
    private FirebaseDatabase mFirebaseInstance;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        name_p = (EditText) findViewById(R.id.name_p);
        item_p = (EditText) findViewById(R.id.item_n);
        quty_p = (EditText) findViewById(R.id.quantity_p);
        flag[0] = 0;
        flag1[0] = 0;
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("application");
    }
    public void submitReturn(View v) {
        if(!TextUtils.isEmpty(name_p.getText().toString()) && !TextUtils.isEmpty(item_p.getText().toString()) &&
                !TextUtils.isEmpty(quty_p.getText().toString())) {
            progressBar.setVisibility(View.VISIBLE);
            mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                        String itemName = String.valueOf(chidSnap.child("item").getValue());
                        String quantity = String.valueOf(chidSnap.child("quantity").getValue());
                        String nameOfPerson = String.valueOf(chidSnap.child("name").getValue());
                        String submitted = String.valueOf(chidSnap.child("submitted").getValue());
                        if (nameOfPerson.toLowerCase().equals(name_p.getText().toString().toLowerCase()) && !submitted.equals("true")
                                && itemName.toLowerCase().equals(item_p.getText().toString().toLowerCase()) && flag[0] == 0 ) {
                            if (quantity.equals(quty_p.getText().toString())) {
                                submit(quantity, itemName, nameOfPerson, 0);
                                Toast.makeText(getApplicationContext(), "Fields Cannot be empty", Toast.LENGTH_SHORT).show();
                            } else {
                                submit(quantity, itemName, nameOfPerson, 1);
                                Toast.makeText(getApplicationContext(), "Complete Items Not Submitted", Toast.LENGTH_SHORT).show();
                            }
                            flag[0] = 1;
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Stock has already be updated.",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Fields Cannot be empty",Toast.LENGTH_SHORT).show();
        }
    }

    public void submit(String q, String i, String n, int ch) {
        DatabaseReference mFirebaseDatabase2;
        mFirebaseDatabase2 = mFirebaseInstance.getReference("application");
        if(ch == 0) {
            mFirebaseDatabase2.child(n.toUpperCase()+i.toUpperCase()).child("submitted").setValue("true");
        }
        DatabaseReference mFirebaseDatabase1 = mFirebaseInstance.getReference("stock");
        mFirebaseDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                    //*Log.v("tmz",""+ chidSnap.getKey()); //displays the key for the node
                    // Log.v("tmz",""+ chidSnap.child("market_name").getValue());   //gives the value for given keyname*//*
                    String item_x = String.valueOf(chidSnap.child("item").getValue());
                    String quantity_x = String.valueOf(chidSnap.child("quantity").getValue());
                    if(item_x.toLowerCase().equals(item_p.getText().toString().toLowerCase()) && flag1[0] == 0) {
                        updateData(Integer.parseInt(quantity_x));
                        flag1[0] = 1;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public  void updateData(int q) {
        int sum = Integer.parseInt(quty_p.getText().toString());
        DatabaseReference mFirebaseDatabase1;
        mFirebaseDatabase1 = mFirebaseInstance.getReference("stock");
        mFirebaseDatabase1.child(item_p.getText().toString().toUpperCase()).child("quantity").setValue(q + sum);
        Toast.makeText(getApplicationContext(),"Stock Count Updated",Toast.LENGTH_SHORT).show();
    }
}
