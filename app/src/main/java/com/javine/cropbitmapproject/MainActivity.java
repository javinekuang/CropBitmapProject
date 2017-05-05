package com.javine.cropbitmapproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends Activity {

    private static final int REQUEST_CHOOSE = 1;
    private static final int REQUEST_CROP = 2;

    private String cropPath = Environment.getExternalStorageDirectory() + "/tmp_crop.jpeg";

    ImageView iv_preview;
    Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
    }

    public void cropClick(View v){
        if (imgUri == null){
            chooseImage();
        }else{
            cropImageUri(this, imgUri, cropPath, 400, 200, REQUEST_CROP);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CHOOSE);
    }

    public static void cropImageUri(Context context, Uri uri, String filePath, int width, int height, int requestCode) {
        Intent intent = new Intent("k.javine.cropbitmap.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("circleCrop", false);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        intent.putExtra("noFaceDetection", true); // no face detection
        ((Activity)context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CHOOSE:
                    imgUri = data.getData();
                    if (imgUri != null){
                        cropImageUri(this, imgUri, cropPath, 400, 200, REQUEST_CROP);
                    }
                    break;
                case REQUEST_CROP:
                    iv_preview.setImageBitmap(BitmapFactory.decodeFile(cropPath));
                    break;
            }
        }
    }
}
