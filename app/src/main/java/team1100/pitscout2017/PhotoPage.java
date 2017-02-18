package team1100.pitscout2017;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoPage extends AppCompatActivity {

    private String teamNuber;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_page);
        Intent intent = getIntent();
        teamNuber = intent.getStringExtra(InfoPage.TEAM_NUMBER_EXTRA);
        updateImages();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateImages();
    }

    @Override
    public void onStart(){
        super.onStart();
        updateImages();
    }

    public void takePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException e){
            }
            if(photoFile!=null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "team1100.pitscout2017.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = teamNuber+"_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void updateImages(){
        File[] files;
        try{
            String path = Environment.getExternalStorageDirectory().toString()+"/Android/data/team1100.pitscout2017/files/Pictures";
            System.out.println(path);
            File directory = new File(path);
            files = directory.listFiles();
        }catch (Exception e){
            files = null;
        }
        if(files!=null){
            LinearLayout view = (LinearLayout)findViewById(R.id.photo_page);
            view.removeAllViews();
            for(File file:files){
                System.out.println(file.getName());
                String num = file.getName().split("_")[0];
                if(num.equals(teamNuber)){
                    Bitmap pic = BitmapFactory.decodeFile(file.getAbsolutePath());
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(pic);
                    view.addView(imageView);
                }
            }
        }
    }
}