package com.example.paul.snapchatdemo.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.Story;
import com.example.paul.snapchatdemo.manager.FriendManager;
import com.example.paul.snapchatdemo.manager.StoryManager;
import com.example.paul.snapchatdemo.utils.UserUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 10/10/16.
 */
public class StoryAdapter extends ArrayAdapter<Story> implements Filterable {
    private Context context;
    private int resource;
    private List<Story> storyList;
    private List<Story> filteredStoryList = new ArrayList<>();
    private StoryFilter storyFilter;

    public StoryAdapter(Context context, int resource, List<Story> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.storyList = objects;
        this.filteredStoryList = objects;
        getFilter();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Story story = storyList.get(position);
        View view = LayoutInflater.from(context).inflate(resource, null);
        TextView textView = (TextView) view.findViewById(R.id.friend_stories_text);
        textView.setText(story.getStoryText());

        TextView name = (TextView) view.findViewById(R.id.friend_stories_username);
        name.setText(StoryManager.getInstance().getFriendName(story.getId()));

        final ImageView blockImage = (ImageView) view.findViewById(R.id.friend_stories_block);
        if (StoryManager.getInstance().isSecretBlocked(UserUtil.getId(), story)){
            blockImage.setVisibility(View.VISIBLE);
        }else{
            blockImage.setVisibility(View.GONE);
        }

        final ImageView imageView = (ImageView) view.findViewById(R.id.friend_stories_image);
        if (story.getImage()!=null){
            Picasso.with(context).load(story.getImage()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StoryManager.getInstance().isSecretBlocked(UserUtil.getId(), story)){
                        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                        final View popupView = inflater.inflate(R.layout.popup_secret, null);
                        final PopupWindow popupWindow = new PopupWindow(popupView, 800, 800,true);
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        final EditText pocketAmount = (EditText) popupView.findViewById(R.id.secret_money_edit);
                        Button doneBtn = (Button) popupView.findViewById(R.id.secret_done);
                        doneBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String msg = String.format("Send %s red pocket %s AUD",StoryManager.getInstance().getFriendName(story.getId()),pocketAmount.getText().toString());
                                Toast.makeText(getContext(), msg , Toast.LENGTH_LONG).show();
                                story.setIsSecret(Story.STORY_NOT_SECRET);
                                popupWindow.dismiss();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        blockImage.setVisibility(View.GONE);
                                    }
                                },2000);
                            }
                        });
                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                popupWindow.dismiss();
                            }
                        });
                    }else{
                        // story is not secret, just display the image
                        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                        final View popupView = inflater.inflate(R.layout.popup_image, null);
                        final PopupWindow popupWindow = new PopupWindow(popupView, 800, 800,true);
                        popupWindow.setOutsideTouchable(false);
                        ImageView popupImage = (ImageView) popupView.findViewById(R.id.popup_image);
                        popupImage.setOnClickListener(popupImageClickListener());
                        Picasso.with(context).load(story.getImage()).into(popupImage);
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                popupWindow.dismiss();
                            }
                        });
                    }
                }
            });
        }
        return view;
    }


    public View.OnClickListener popupImageClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    @Override
    public Filter getFilter() {
        if (storyFilter==null){
            storyFilter = new StoryFilter();
        }
        return storyFilter;
    }

    @Override
    public int getCount() {
        return filteredStoryList.size();
    }

    @Override
    public Story getItem(int position) {
        return filteredStoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class StoryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constrains) {
            FilterResults filterResults = new FilterResults();
            if (constrains!=null && constrains.length()>0){
                ArrayList<Story> showingFriendArrayList = new ArrayList<>();
                for (Story story : storyList){
                    if (story.getStoryText().toLowerCase().contains(constrains.toString().toLowerCase())){
                        showingFriendArrayList.add(story);
                    }
                }

                filterResults.count = showingFriendArrayList.size();
                filterResults.values = showingFriendArrayList;
            }else{
                filterResults.count = storyList.size();
                filterResults.values = storyList;
            }
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredStoryList = (ArrayList<Story>) filterResults.values;
            notifyDataSetChanged();
        }
    }


}
