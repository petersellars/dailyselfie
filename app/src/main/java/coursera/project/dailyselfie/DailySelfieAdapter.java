package coursera.project.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Peter on 25/11/2014.
 */
public class DailySelfieAdapter extends BaseAdapter {

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

        holder.thumbnail.setImageBitmap(curr.getSelfie());
        holder.filename.setText(curr.getFileUrl());

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

    public ArrayList<DailySelfie> getList(){
        return list;
    }

    public void removeAllViews(){
        list.clear();
        this.notifyDataSetChanged();
    }
}
