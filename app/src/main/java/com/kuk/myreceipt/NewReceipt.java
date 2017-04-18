package com.kuk.myreceipt;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.kuk.myreceipt.model.ReceiptSQLite;
import com.squareup.picasso.Picasso;


public class NewReceipt extends AppCompatActivity {

    EditText titletxt;
    EditText datetxt;
    EditText pricetxt;
    EditText desctxt;
    ImageView imageView;

    private int CAMERA_REQUEST_CODE = 999;
    Context context = this;
    Button savebtn;
    Bitmap pic;
    ReceiptSQLite sqLite;
    private Uri mImageUri;
    File photo;
    File thumbPhoto;
    Bitmap thumbpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_receipt);


        titletxt = (EditText) findViewById(R.id.titletxt);
        pricetxt =(EditText)findViewById(R.id.pricetxt);
        desctxt = (EditText)findViewById(R.id.desctxt);
        datetxt = (EditText)findViewById(R.id.datetxt);
        String timeStamp = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        datetxt.setText(timeStamp);
        sqLite = new ReceiptSQLite(this);

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try
                {
                    // place where to store camera taken picture
                    photo = createTemporaryFile("picture", ".jpg");
                    photo.delete();
                }
                catch(Exception e)
                {
                    Log.v("takepic", "Can't create file to take picture!");
                    Toast.makeText(context, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
                    //return false;
                }
                mImageUri = Uri.fromFile(photo);
                i.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);


                startActivityForResult( i , CAMERA_REQUEST_CODE );
            }


        });

        savebtn = (Button)findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        String title = titletxt.getText().toString();
                        String date = datetxt.getText().toString();

                        int price = Integer.valueOf(pricetxt.getText().toString());
                        String description = desctxt.getText().toString();
                        String filePath = saveBitmapToFile(pic);
                        String thumbnail = createThumbnail(filePath);
                        sqLite.saveReceipt(title, date, filePath, price, description,thumbnail);
                        Toast.makeText(context, "Save " + title, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, "Can't save the receipt, Please fill price", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        sqLite.getReceiptList();
    }

    public String createThumbnail(String imagefullpath) {

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

    public File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }


    public void grabImage(ImageView imageView)
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

            Log.d("Bitmap","new Width  :"+String.valueOf(w));
            Log.d("Bitmap","nwe Height :"+String.valueOf(h));

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK  ) {
            grabImage(imageView);
        }
    }

    public String saveBitmapToFile(Bitmap bitmap) {

        String fullPath = null;

        if( bitmap!= null)
        {

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/receipt_images");
            myDir.mkdirs();

            //Random generator = new Random();
            //int n = 10000;
            //n = generator.nextInt(n);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fname = "Image-" + timeStamp + ".jpg";

            File file = new File(myDir, fname);
            fullPath = myDir + "/" + fname;

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                Log.d("savefile","Success");
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("savefile","Error");
            }
        }
        return fullPath;
    }



}
