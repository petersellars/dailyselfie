package coursera.project.dailyselfie;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class SelfieActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_selfie);

        String selfieURL = getIntent().getStringExtra("selfieURL");
        Log.d("Selfie", "Selfie to load: " + selfieURL);
        ImageView selfieView = (ImageView) findViewById(R.id.daily_selfie);
        try {
            selfieView.setImageBitmap(
                MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(), Uri.parse(selfieURL)));
        } catch (IOException e) {
            Log.d("Selfie", "Issue displaying selfie!");
        }
    }
}
