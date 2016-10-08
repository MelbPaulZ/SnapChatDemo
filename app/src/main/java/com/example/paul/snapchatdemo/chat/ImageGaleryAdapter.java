package com.example.paul.snapchatdemo.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.paul.snapchatdemo.R;

import java.util.HashMap;
import java.util.Map;

public class ImageGaleryAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private  Map<String,String> selectedImagePath;
    private Activity activity;
    private int ids[];
    private boolean[] imageSelection;
    private String[] imagePath;
    private int count;

    public ImageGaleryAdapter(LayoutInflater mInflater, int count, boolean[] imageSelection, int ids[],
                              Activity activity, String[] imagePath) {
        this.mInflater = mInflater;
        this.selectedImagePath = new HashMap<String, String>();
        this.activity = activity;
        this.ids = ids;
        this.imageSelection = imageSelection;
        this.imagePath = imagePath;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public  Map<String,String> getSelectedImagePath(){
        return selectedImagePath;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ChatMessageViewHolder holder;
        if (convertView == null) {
            holder = new ChatMessageViewHolder();
            convertView = mInflater.inflate(R.layout.item_image_gallery, null);
            holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
            holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);

            convertView.setTag(holder);
        } else {
            holder = (ChatMessageViewHolder) convertView.getTag();
        }
        holder.chkImage.setId(position);
        holder.imgThumb.setId(position);
        holder.chkImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int id = cb.getId();
                if (imageSelection[id]) {
                    // de-select image
                    cb.setChecked(false);
                    imageSelection[id] = false;

                    selectedImagePath.remove(imagePath[position]);
                } else {
                    // select image
                    cb.setChecked(true);
                    imageSelection[id] = true;

                    selectedImagePath.put(imagePath[position],"selected");
                }
            }
        });
        holder.imgThumb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int id = holder.chkImage.getId();
                if (imageSelection[id]) {
                    // de-select image
                    holder.chkImage.setChecked(false);
                    imageSelection[id] = false;

                    selectedImagePath.remove(imagePath[position]);
                } else {
                    // select image
                    holder.chkImage.setChecked(true);
                    imageSelection[id] = true;

                    selectedImagePath.put(imagePath[position],"selected");
                }
            }
        });

        // render the image gallery
        try {
            setBitmap(holder.imgThumb, ids[position]);
        } catch (Throwable e) {
        }

        holder.chkImage.setChecked(imageSelection[position]);
        holder.id = position;
        return convertView;
    }

    private void setBitmap(final ImageView iv, final int id) {

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                iv.setImageBitmap(result);
            }
        }.execute();
    }
}