package coursera.project.dailyselfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailySelfieActivity extends ListActivity {

    private static final String TAG = "DAILY_SELFIE";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final long SELFIE_INTERVAL_TWO_MINUTES = 2 * 60 * 1000;
    private static final long INITIAL_ALARM_DELAY = SELFIE_INTERVAL_TWO_MINUTES;

    private AlarmManager alarmManager;
    private Intent selfieNotificationReceiverIntent;
    private PendingIntent selfieNotificationReceiverPendingIntent;
    private DailySelfieAdapter dailySelfieAdapter;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_selfie);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        selfieNotificationReceiverIntent = new Intent(DailySelfieActivity.this,
                SelfieAlarmNotificationReceiver.class);
        selfieNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                DailySelfieActivity.this, 0, selfieNotificationReceiverIntent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                SELFIE_INTERVAL_TWO_MINUTES, selfieNotificationReceiverPendingIntent);

        dailySelfieAdapter = new DailySelfieAdapter(getApplicationContext());
        dailySelfieAdapter.addAllViews();
        setListAdapter(dailySelfieAdapter);
    }

/*    @Override
    protected void onResume() {
        super.onResume();
        // dailySelfieAdapter.addAllViews();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_selfie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d(TAG, "Photo taken successfully...");
            Log.d(TAG, "Current Photo Path: " + currentPhotoPath);
            dailySelfieAdapter.add(new DailySelfie(currentPhotoPath));
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

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        // Do something when a list item is clicked
        Intent showSelfieIntent = new Intent(this, SelfieActivity.class);
        DailySelfie theSelfie = (DailySelfie) dailySelfieAdapter.getItem(position);
        Log.d(TAG, "Selfie to show: " + theSelfie.getFileUrl());
        showSelfieIntent.putExtra("selfieURL", theSelfie.getFileUrl());
        startActivity(showSelfieIntent);
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
                this.getExternalFilesDir(null);
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
}
