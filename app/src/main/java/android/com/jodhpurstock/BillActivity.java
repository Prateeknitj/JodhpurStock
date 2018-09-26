package android.com.jodhpurstock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import static com.itextpdf.text.Annotation.FILE;

public class BillActivity extends AppCompatActivity {
    private static String FILES = "mnt/sdcard/";

    String name,itm;
    int amount,quant,money,dys;
    TextView n,q,i,a;
    Button b1;
    static byte[] bArray;
    static Image image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        Intent it = getIntent();
        itm = it.getStringExtra("item");
        name = it.getStringExtra("name");
        quant = it.getIntExtra("quantity", 0);
        dys = it.getIntExtra("days", 0);
        money = it.getIntExtra("money",0);
        amount = quant * money * dys;
        n = (TextView) findViewById(R.id.nam);
        q = (TextView) findViewById(R.id.qunt);
        i = (TextView) findViewById(R.id.itemm);
        a = (TextView) findViewById(R.id.amt);
        b1 = (Button)  findViewById(R.id.pdf);
        n.setText(name);
        q.setText(String.valueOf(quant));
        i.setText(itm);
        a.setText(String.valueOf(amount));
        FILES = FILES + name + itm + ".pdf";
    }
    public void Save(View v) {
        b1.setVisibility(View.INVISIBLE);
        Bitmap bitmap = takeScreenshot();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
        bArray = baos.toByteArray();
        try
        {
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(FILES));
            document.open();

            addImage(document);
            document.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        b1.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"PDF Saved.", Toast.LENGTH_LONG).show();
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    private static void addImage(Document document)
    {

        try
        {
            image = Image.getInstance(bArray);  ///Here i set byte array..you can do bitmap to byte array and set in image...
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
