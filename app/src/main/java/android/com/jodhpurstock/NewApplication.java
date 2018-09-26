package android.com.jodhpurstock;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NewApplication extends AppCompatActivity {

    ImageView img;
    private ProgressBar progressBar;
    EditText name,item,quty,tod,fromd,days;
    Bitmap mImageUri1;
    String imageUrl;
    final int[] flg = new int[1];
    byte[] b;
    private static final int CAMERA_REQUEST_CODE = 1;
    Calendar myCalendar;
    int flag;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private StorageReference mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_application);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        img = (ImageView) findViewById(R.id.application);
        name = (EditText) findViewById(R.id.name);
        item = (EditText) findViewById(R.id.item);
        quty = (EditText) findViewById(R.id.quantity);
        tod = (EditText) findViewById(R.id.to);
        fromd = (EditText) findViewById(R.id.from);
        days = (EditText) findViewById(R.id.days);
        flag = 0;
        flg[0] = 0;
        mImageUri1 = null;
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabase = mFirebaseInstance.getReference("application");
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(flag == 0) {
                    updateLabel();
                }
                else {
                    updateLabel1();
                }
            }
        };
        tod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                new DatePickerDialog(NewApplication.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fromd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                new DatePickerDialog(NewApplication.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromd.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tod.setText(sdf.format(myCalendar.getTime()));
    }

    public void SubmitApplication(View v) {
        if(!TextUtils.isEmpty(item.getText().toString())) {
            progressBar.setVisibility(View.VISIBLE);
             final String str = item.getText().toString();
             FirebaseDatabase mFirebaseInstance1 = FirebaseDatabase.getInstance();
             final DatabaseReference mFirebaseDatabase1 = mFirebaseInstance1.getReference("stock");
             mFirebaseDatabase1.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                    //*Log.v("tmz",""+ chidSnap.getKey()); //displays the key for the node
                   // Log.v("tmz",""+ chidSnap.child("market_name").getValue());   //gives the value for given keyname*//*
                         String item = String.valueOf(chidSnap.child("item").getValue());
                         String quantity = String.valueOf(chidSnap.child("quantity").getValue());
                         String valu = String.valueOf(chidSnap.child("ppitem").getValue());
                         if(item.toLowerCase().equals(str.toLowerCase()) && flg[0] == 0) {
                             updateValue(mFirebaseDatabase1,Integer.parseInt(quantity));
                             goTO(Integer.parseInt(valu));
                         }
                     }
                 }
                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });
        }
        else {
            Toast.makeText(getApplicationContext(),"Item field cannot be empty.",Toast.LENGTH_SHORT).show();
        }
    }
    public void Upload(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
        if(resultCode == RESULT_OK)
        {
            if(requestCode == CAMERA_REQUEST_CODE)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(photo);
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                File finalFile = new File(getRealPathFromURI(tempUri));
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(finalFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bm = BitmapFactory.decodeStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                b = baos.toByteArray();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Image Taking/Selection Failed",Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void uploadImageToFirebase(final StorageReference uploadRef, byte[] data, final int uploadProfileOrCoveOrCallApi, final Application app)
    {
        UploadTask uploadTask ;
        try
        {
            uploadTask = uploadRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                imageUrl = task.getResult().toString();
                                app.setURL(imageUrl);
                              //  Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();

                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Image Uploading Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception e)
        {
            Log.d("UpdateProfileA",e.getLocalizedMessage());
        }
    }
    public void goTO(int v) {
        if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(item.getText().toString()) &&
                !TextUtils.isEmpty(quty.getText().toString()) && !TextUtils.isEmpty(fromd.getText().toString())
                && !TextUtils.isEmpty(tod.getText().toString())) {
            Intent it = new Intent(getApplicationContext(),BillActivity.class);
            Log.d("Saala Kutta:",String.valueOf(v));
            Application app = new Application();
            app.setName(name.getText().toString());
            app.setItem(item.getText().toString());
            app.setQuantity(Integer.parseInt(quty.getText().toString()));
            app.setFrom(fromd.getText().toString());
            app.setTo(tod.getText().toString());
            StorageReference childRef = mStorage.child(name.getText().toString().toUpperCase() + item.getText().toString().toUpperCase());
            uploadImageToFirebase(childRef,b,1, app);
            app.setURL(imageUrl);
            app.setSubmitted("false");
            mFirebaseDatabase.child(name.getText().toString().toUpperCase() + item.getText().toString().toUpperCase()).setValue(app);
            Toast.makeText(getApplicationContext(), "Application is Submitted", Toast.LENGTH_SHORT).show();
            flg[0] = 1;
            it.putExtra("quantity",Integer.parseInt(quty.getText().toString()));
            it.putExtra("item",item.getText().toString());
            it.putExtra("name",name.getText().toString());
            it.putExtra("days", Integer.parseInt(days.getText().toString()));
            it.putExtra("money", v);
            startActivity(it);
            finish();
        }
        else {
            Toast.makeText(NewApplication.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
    public void updateValue(DatabaseReference mdatabase, int quant) {
        int diff = Integer.parseInt(quty.getText().toString());
        diff = quant - diff;
        mdatabase.child(item.getText().toString().toUpperCase()).child("quantity").setValue(diff);
    }
}
