package coursera.project.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Peter on 25/11/2014.
 */
public class DailySelfieAdapter extends BaseAdapter {

    private static final int THUMBNAIL_SIZE = 100;

    private ArrayList<DailySelfie> list = new ArrayList<DailySelfie>();
    private static LayoutInflater inflater = null;
    private Context context;

    public DailySelfieAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        ViewHolder holder;

        DailySelfie curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.photo_thumbnail_list, null);
            holder.thumbnail = (ImageView) newView.findViewById(R.id.photo_thumbnail);
            holder.filename = (TextView) newView.findViewById(R.id.photo_filename);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.thumbnail.setImageBitmap(getPreview(URI.create(curr.getFileUrl())));
        String selfieFilename = curr.getFileUrl();
        selfieFilename =
                selfieFilename.replace(context.getExternalFilesDir(null).toString(),"");
        selfieFilename = selfieFilename.replace(".jpg","");
        selfieFilename = selfieFilename.replace("file:/Selfie_","");
        selfieFilename = selfieFilename.substring(0, selfieFilename.lastIndexOf("_"));
        holder.filename.setText(selfieFilename);

        return newView;
    }

    static class ViewHolder {

        ImageView thumbnail;
        TextView filename;

    }

    public void add(DailySelfie listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public void addAllViews() {
        list.addAll(getFileList());
        notifyDataSetChanged();
    }

    public ArrayList<DailySelfie> getFileList() {
        ArrayList<DailySelfie> dailySelfieList = new ArrayList<DailySelfie>();
        if (context.getExternalFilesDir(null) != null) {
            String path = context.getExternalFilesDir(null).toString();
            Log.d("Files", "Path: " + path);
            File f = new File(path);
            File file[] = f.listFiles();
            Log.d("Files", "Size: " + file.length);
            for (int i = 0; i < file.length; i++) {
                Log.d("Files", "Is selfie: " + file[i].getName().startsWith("Selfie_"));
                if (file[i].getName().startsWith("Selfie_")) {
                    Log.d("Files", "FileName:" + file[i].getName());
                    dailySelfieList.add(new DailySelfie("file:" + path + "/" + file[i].getName()));
                }
            }
        }
        return dailySelfieList;
    }

    public ArrayList<DailySelfie> getList(){
        return list;
    }

    public void removeAllViews(){
        list.clear();
        this.notifyDataSetChanged();
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
