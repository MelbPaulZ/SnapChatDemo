package com.example.paul.snapchatdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.Story;
import com.example.paul.snapchatdemo.manager.FriendManager;
import com.example.paul.snapchatdemo.manager.StoryManager;
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
        Story story = storyList.get(position);
        View view = LayoutInflater.from(context).inflate(resource, null);
        TextView textView = (TextView) view.findViewById(R.id.friend_stories_text);
        textView.setText(story.getStoryText());

        TextView name = (TextView) view.findViewById(R.id.friend_stories_username);
        name.setText(StoryManager.getInstance().getFriendName(story.getId()));

        ImageView imageView = (ImageView) view.findViewById(R.id.friend_stories_image);
        if (story.getImage()!=null){
            Picasso.with(context).load(story.getImage()).into(imageView);
        }
        return view;
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
