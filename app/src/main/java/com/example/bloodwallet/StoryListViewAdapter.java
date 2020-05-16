package com.example.bloodwallet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StoryListViewAdapter extends BaseAdapter {

    private ArrayList<StoryListItem> listViewItemList = new ArrayList<StoryListItem>() ;

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.story_list_item, parent, false);
        }

        ImageView profilePhoto = (ImageView) convertView.findViewById(R.id.story_list_profile_photo);
        TextView storyTitle = (TextView) convertView.findViewById(R.id.story_list_title);
        TextView storyContent = (TextView) convertView.findViewById(R.id.story_list_content);
        TextView uploadTime = (TextView) convertView.findViewById(R.id.story_list_time);
        TextView percent = (TextView) convertView.findViewById(R.id.story_list_percent);

        StoryListItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        if (listViewItem.getProfilePhoto() != null) {
            profilePhoto.setImageDrawable(listViewItem.getProfilePhoto());
        }
        storyTitle.setText(listViewItem.getStoryTitle());
        storyContent.setText(listViewItem.getStoryContent());
        uploadTime.setText(listViewItem.getUploadTime());
        percent.setText(listViewItem.getPercent());

        return convertView;
    }

    public void addItem(Drawable photo, String title, String content, String uploadTime, int percent) {
        StoryListItem item = new StoryListItem();
        item.setProfilePhoto(photo);
        item.setStoryTitle(title);
        item.setStoryContent(content);
        item.setUploadTime(uploadTime);
        item.setPercent(percent);

        listViewItemList.add(item);
    }
}
