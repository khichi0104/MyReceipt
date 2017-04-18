package com.kuk.myreceipt;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Toast;

import com.kuk.myreceipt.model.ReceiptModel;
import com.kuk.myreceipt.model.ReceiptSQLite;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewReceiptActivity extends AppCompatActivity {


    EditText titletxt;
    EditText datetxt;
    EditText pricetxt;
    EditText desctxt;
    ImageView imageView;
    ImageButton btndel;

    Button btnEdit;
    Button btnCancel;
    Context context;
    ReceiptModel data;
    ReceiptSQLite sqLite;

    boolean editStatus = false;
    private Uri mImageUri = Uri.EMPTY;
    private int CAMERA_REQUEST_CODE = 888;
    File photo;
    Bitmap thumbpic;
    Bitmap pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt);

        titletxt = (EditText) findViewById(R.id.titletxt);
        pricetxt =(EditText)findViewById(R.id.pricetxt);
        desctxt = (EditText)findViewById(R.id.desctxt);
        datetxt = (EditText)findViewById(R.id.datetxt);
        btnEdit= (Button)findViewById(R.id.editbtn);
        btnCancel = (Button)findViewById(R.id.cancelbtn);
        imageView = (ImageView)findViewById(R.id.imageView);
        btndel =(ImageButton)findViewById(R.id.delbtn);
        context = this;
        sqLite = new ReceiptSQLite(this);

        editStatus = true;

        titletxt.setTextColor(Color.BLACK);
        pricetxt.setTextColor(Color.BLACK);
        desctxt.setTextColor(Color.BLACK);
        datetxt.setTextColor(Color.BLACK);

        SetUI();
        SetData();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editStatus == false)
                {
                    //Open fullimage activity
                    Intent i = new Intent(context,FullimageActivity.class) ;
                    i.putExtra("image",data.getFilePath());
                    startActivity(i);

                }else
                {
                    //Open Camera
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try
                    {
                        // place where to store camera taken picture
                        NewReceipt cc = new NewReceipt();
                        photo = cc.createTemporaryFile("picture", ".jpg");
                        photo.delete();
                    }
                    catch(Exception e)
                    {
                        Log.v("takepic", "Can't create file to take picture!");
                        Toast.makeText(context, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
                    }
                    mImageUri = Uri.fromFile(photo);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                    startActivityForResult( i , CAMERA_REQUEST_CODE );
                }

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetUI();
               SetData();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetUI();

                if(editStatus == false)
                {
                    NewReceipt cc = new NewReceipt();
                    //Check condition
                    try {
                        String title = titletxt.getText().toString();
                        String date = datetxt.getText().toString();

                        int price = Integer.valueOf(pricetxt.getText().toString());
                        String description = desctxt.getText().toString();

                        String filePath = data.getFilePath();
                        String thumbnail = data.getThumbnailPath();

                        //Compare imgpath
                        if(mImageUri != Uri.EMPTY && pic != null)
                        {
                            //delete old file
                            sqLite.deleteFile(filePath,thumbnail);
                            //take new image
                            filePath = cc.saveBitmapToFile(pic);
                            thumbnail = createThumbnail(filePath);
                        }

                        //UpdateDB
                        sqLite.updateReceipt(data.getRid(),title, date, filePath, price, description,thumbnail);

                        Toast.makeText(context, "Update " + title, Toast.LENGTH_SHORT).show();
                        SetUI();

                        finish();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, "Can't save the receipt, Please fill price", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

       btndel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setMessage("Are you sure you want to delete?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               sqLite.deleteReceipt(data.getRid(),data.getFilePath(),data.getThumbnailPath());
                               Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                               finish();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {

                           }
                       });

               AlertDialog alert = builder.create();

               alert.show();
           }
       });
    }

    private String createThumbnail(String imagefullpath) {

        String thumbpath = null;

        if(imagefullpath != null)
        {

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/receipt_images/thumbnails");
            myDir.mkdirs();

            //String fname = "Image-" + n + ".jpg";
            String fname = imagefullpath.substring(imagefullpath.lastIndexOf("/") + 1);

            File file = new File(myDir, fname);
            thumbpath = myDir + "/" + fname;

            try {
                FileOutputStream out = new FileOutputStream(file);
                thumbpic.compress(Bitmap.CompressFormat.JPEG, 90, out);

                Log.d("savefile", "Success");
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("savefile", "Error");
            }
        }
        return thumbpath;
    }

    private void SetUI() {
        if(editStatus == true)
        {
            editStatus = false;
            titletxt.setEnabled(false);
            pricetxt.setEnabled(false);
            desctxt.setEnabled(false);
            datetxt.setEnabled(false);
            btnEdit.setText("EDIT");
            btnCancel.setVisibility(Button.INVISIBLE);
            btndel.setVisibility(Button.INVISIBLE);

        }
        else
        {
            editStatus = true;
            titletxt.setEnabled(true);
            pricetxt.setEnabled(true);
            desctxt.setEnabled(true);
            datetxt.setEnabled(true);
            btnEdit.setText("UPDATE");
            btnCancel.setVisibility(Button.VISIBLE);
            btndel.setVisibility(Button.VISIBLE);
        }
    }

    private void SetData() {

        data = (ReceiptModel) getIntent().getSerializableExtra("selectedData");
        titletxt.setText(data.getTitle());
        datetxt.setText(data.getDateTime());
        pricetxt.setText(String.valueOf(data.getPrice()));
        desctxt.setText(data.getDescription());
        imageView.setImageURI(Uri.parse(data.getFilePath()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK  ) {
            grabImage(imageView);
        }
    }

    private void grabImage(ImageView imageView)
    {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;

        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            Bitmap thumbnailbitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200);
            Log.d("Bitmap","Original Width  :"+String.valueOf(bitmap.getWidth()));
            Log.d("Bitmap","Original Height :"+String.valueOf(bitmap.getHeight()));

            //int ratio = 60/100;
            int  w = bitmap.getWidth()*60/100; //(int)Math.round(bitmap.getWidth()*ratio);
            int  h = bitmap.getHeight()*60/100;//(int)Math.round(bitmap.getHeight()*ratio);

            Log.d("scaledBitmap","new Width  :"+String.valueOf(w));
            Log.d("scaledBitmap","new Height :"+String.valueOf(h));

            Bitmap scaledBitmap = Bitmap.createScaledBitmap( bitmap ,  w , h, false);

            pic = scaledBitmap;
            thumbpic= thumbnailbitmap;
            //thumbpic = n;
            imageView.setImageBitmap(scaledBitmap);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("takepic", "Failed to load", e);
        }
    }


}
