package com.example.nikhil.camerareader;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Button;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.content.pm.PackageInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    static final int CAPTURE_IMAGE_REQUEST = 1;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imgView = (ImageView)findViewById(R.id.imageView);
        Button cameraBtnVar = (Button)findViewById(R.id.cameraClickButton);

        if (!hasCamera())
            cameraBtnVar.setEnabled(false);
    }

    private boolean hasCamera(){
        return  getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//
//        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
//        imagesFolder.mkdirs();
//
//        File image = new File(imagesFolder, "QR_" + timeStamp + ".png");
//        Uri uriSavedImage = Uri.fromFile(image);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extra = data.getExtras();
            Bitmap img = (Bitmap)extra.get("data");
            imgView.setImageBitmap(img);

            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(img, partFilename);

            MediaStore.Images.Media.insertImage(getContentResolver(),img,"1isthe","my first image");
        }
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getRootDirectory(),/*.getExternalStorageDirectory(), */"photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

