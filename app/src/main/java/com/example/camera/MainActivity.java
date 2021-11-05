package com.example.camera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private int CAMERA_CAPRURE = 100;
    private  static  final  int REQUEST_ID_VIDEO_CAPTURE = 101;

    Button btn;
    ImageView img;
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.ButtonImage);
        img = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);

        btn.setOnClickListener(view -> {
            try {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(captureIntent, CAMERA_CAPRURE);
            }
            catch (ActivityNotFoundException cant)
            {
                String errorMessage = "Камера не поддерживает вашим устройством";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        Button btnstrvideo = findViewById(R.id.StartVideo);
        btnstrvideo.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                File dir = Environment.getDataDirectory();

                String savePath = dir.getAbsolutePath() + "/myvideo.mp4";
                File videoFile = new File(savePath);
                Uri videoUri = Uri.fromFile(videoFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                this.startActivityForResult(intent, REQUEST_ID_VIDEO_CAPTURE);
            }
            catch (ActivityNotFoundException cant)
            {
                String errorMessage = "Камера не поддерживает вашим устройством";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_CAPRURE) {
            Bundle extras = data.getExtras();
            Bitmap thumbnailBimap = (Bitmap) extras.get("data");
            img.setImageBitmap(thumbnailBimap);
        }
        else if(requestCode == REQUEST_ID_VIDEO_CAPTURE)
        {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                Toast.makeText(this, "Video saved to:\n" +
                        videoUri, Toast.LENGTH_LONG).show();
                this.videoView.setVideoURI(videoUri);
                this.videoView.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}