package com.test.cermati.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.cermati.R;

import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Map<String,String>> list = null;

    public ListViewAdapter(Context context, List<Map<String,String>> animalNamesList) {
        this.inflater = LayoutInflater.from(context);
        this.list = animalNamesList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        TextView name;
        ImageView image;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.user_name);
            holder.image = (ImageView) view.findViewById(R.id.avatar_url);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(list.get(i).get("login"));
        Picasso.get().load(list.get(i).get("avatar_url")).into(holder.image);
        return view;
    }
}
