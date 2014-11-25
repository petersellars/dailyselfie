package coursera.project.dailyselfie;

import android.graphics.Bitmap;

/**
 * Created by Peter on 25/11/2014.
 */
public class DailySelfie {

    private String fileUrl;
    private Bitmap selfie;

    public DailySelfie(String fileUrl, Bitmap selfie) {
        this.fileUrl = fileUrl;
        this.selfie = selfie;
    }

    public DailySelfie(Bitmap selfie) {
        this.selfie = selfie;
    }

    public DailySelfie() {
    }

    public Bitmap getSelfie() {
        return selfie;
    }

    public void setSelfie(Bitmap selfie) {
        this.selfie = selfie;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString(){
        return "Selfie: " + fileUrl;

    }
}
