package com.fanok.mdpu24v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanok.mdpu24v1.Article;
import com.fanok.mdpu24v1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CastomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Article> model;

    public CastomAdapter(Context context, ArrayList<Article> model) {
        inflater = LayoutInflater.from(context);
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return model.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);
        }
        TextView title = view.findViewById(R.id.name);
        TextView data = view.findViewById(R.id.date);
        ImageView imageView = view.findViewById(R.id.icon);
        title.setText(model.get(i).getTitle());
        data.setText(model.get(i).getDate());

        if (!model.get(i).getImage().isEmpty())
            Picasso.get().load(model.get(i).getImage()).into(imageView);
        return view;
    }
}
