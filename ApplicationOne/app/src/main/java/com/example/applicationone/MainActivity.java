package com.example.applicationone;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/** This Activity sends the broadcast. */
public class MainActivity extends AppCompatActivity {

  AssetManager assetManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.assetManager = getAssets();

    // Send a broadcast with a provided text.
    Button sendBroadcastButton = findViewById(R.id.sendBroadcast);
    final ImageView imageView = findViewById(R.id.image_view);
    final TextView textView = findViewById(R.id.text_view);
    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
      int count = 0;
      @Override
      public void run() {
        if (count++ < 187) {
          // int subIteration = count % 3;
          // int imageId = subIteration == 0 ? R.drawable.moonhighres_icon : subIteration == 1 ? R.drawable.cloudhighres_icon : R.drawable.sunhighres_icon;
          //
          // // Create Image and Text representing file that is going to be broadcasted
          // imageView.setImageResource(imageId);
          // textView.setText("File" + count + ".jpg");

          String nameString;
          if(count < 10) {
            nameString = "cine_0000" + count;
          } else if(count < 100) {
            nameString = "cine_000" + count;
          } else {
            nameString = "cine_00" + count;
          }
          int imageId = getResources().getIdentifier(nameString, "drawable", getPackageName());

          // Save Images and broadcast Content URIs
          createAndSendIntent(imageId, count, nameString);

          // Waits 100 ms between each iteration.
          handler.postDelayed(this, 10);
        }
      }
    };
    sendBroadcastButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        handler.post(runnable);
      }
    });
  }

  private void createAndSendIntent(int imageId, int iteration, String nameString) {
    Intent intent = new Intent();
    intent.setAction("com.example.applicationone.SCANNED_IMAGE");

    // Path of root directory of this application's internal storage.
    File rootDirectory = getFilesDir();

    // Get the files/images subdirectory.
    File imagesDirectory = new File(rootDirectory, "images");
    if(!imagesDirectory.exists()) {
      imagesDirectory.mkdir();
    }

    // Create file name.
    String fileName = "File" + iteration + ".png"; // Change depending on file type.
    File imageFilePath = new File(imagesDirectory, fileName);

    // Save file to internal file system.
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(imageFilePath);

      // Option 1. Get image from resources. Makes bitmap larger.
      //Log.w("ApplicationOne", "Getting image from res");
      //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);

      // Option 2. Get image from assets. Preserve image size.
      Log.w("ApplicationOne", "Getting image from assets");
      InputStream inputStream = assetManager.open(nameString + ".png");
      Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

      // To make byte stream smaller, change PNG to JPEG (and make change to .png above). To make even more small, lower quality!
      bitmap.compress(CompressFormat.PNG, 100, fos); // Change depending on file type
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally
     {
      try {
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Get File we want to send.
    File requestFile = new File(imageFilePath.getAbsolutePath());

    Uri fileUri = null;

    // Get URI of this File.
    try {
      fileUri = FileProvider.getUriForFile(
          MainActivity.this,
          "com.example.applicationone.fileprovider",
          requestFile
      );
    } catch (IllegalArgumentException e) {
      Log.w("ApplicationOne", "IllegalArgumentException when retrieving Uri. Selected file cannot be shared.");
      return;
    }

    // If URI retrieved, attach permissions and data type. Send broadcast.
    if (fileUri != null) {
      grantUriPermission("com.example.applicationtwo", fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
      Log.w("ApplicationOne", "Successfully sent broadcast");
      sendBroadcast(intent);
    } else {
      Log.w("ApplicationOne", "Failed to send broadcast. fileUri is null.");
    }
  }
}