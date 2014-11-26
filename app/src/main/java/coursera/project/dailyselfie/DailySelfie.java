package coursera.project.dailyselfie;

/**
 * Created by Peter on 25/11/2014.
 */
public class DailySelfie {

    private String fileUrl;

    public DailySelfie(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public DailySelfie() {
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
