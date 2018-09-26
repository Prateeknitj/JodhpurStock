package android.com.jodhpurstock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    EditText name,quantity,ppitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("stock");
        name = (EditText) findViewById(R.id.name);
        quantity = (EditText) findViewById(R.id.qty);
        ppitem = (EditText) findViewById(R.id.ppi);
    }

    public void addStock(View view) {
        if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(quantity.getText().toString()) && !TextUtils.isEmpty(ppitem.getText().toString())) {
            Stock stock = new Stock();
            stock.setItem(name.getText().toString());
            stock.setQuantity(Integer.parseInt(quantity.getText().toString()));
            stock.setPpitem(Integer.parseInt(ppitem.getText().toString()));
            mFirebaseDatabase.child(name.getText().toString().toUpperCase()).setValue(stock);
            Toast.makeText(getApplicationContext(), "New Item is Added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateStock(View view) {
        if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(quantity.getText().toString()) && !TextUtils.isEmpty(ppitem.getText().toString())) {
            Stock stock = new Stock();
            stock.setItem(name.getText().toString());
            stock.setQuantity(Integer.parseInt(quantity.getText().toString()));
            stock.setPpitem(Integer.parseInt(ppitem.getText().toString()));
            mFirebaseDatabase.child(name.getText().toString().toUpperCase()).setValue(stock);
            Toast.makeText(getApplicationContext(), "Item Entry is Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
