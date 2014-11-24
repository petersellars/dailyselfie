package coursera.project.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailySelfieActivity extends Activity {

    private static final String TAG = "DAILY_SELFIE";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int THUMBNAIL_SIZE = 100;

    private ImageView thumbnail;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_selfie);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_selfie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d(TAG, "Photo taken successfully...");
            Log.d(TAG, "Current Photo Path: " + currentPhotoPath);
            Bitmap imageBitmap = getPreview(URI.create(currentPhotoPath));
            thumbnail.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.take_picture) {
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void dispatchTakePictureIntent() {
        Log.d(TAG, "Dispatched Take Picture Intent...");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "...found an application that can handle Take Picture Intent");
            // Create the file where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ioe) {
                Log.d(TAG, "Error occurred trying to create image file!");
            }
            // Only continue if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Selfie_" + timestamp + "_";
        File storageDirectory =
                Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",             /* suffix */
                storageDirectory    /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d(TAG, "Current Photo Path: " + currentPhotoPath);
        return image;
    }

    private Bitmap getPreview(URI uri) {
        File image = new File(uri);

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
            return null;

        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / THUMBNAIL_SIZE;
        return BitmapFactory.decodeFile(image.getPath(), opts);
    }
}
