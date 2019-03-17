package com.fanok.mdpu24v1.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanok.mdpu24v1.MyTask;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<MyTask> model;
    private int level;

    public TaskAdapter(Context context, ArrayList<MyTask> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        SharedPreferences mPref = context.getSharedPreferences(StartActivity.PREF_NAME, Context.MODE_PRIVATE);
        level = mPref.getInt("level", 0);
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

        Holder holder = new Holder();
        if (view == null) {
            view = inflater.inflate(R.layout.task_item, viewGroup, false);
            holder.name = view.findViewById(R.id.name);
            holder.them = view.findViewById(R.id.them);
            holder.date = view.findViewById(R.id.date);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }


        show(holder);
        //if (i!=0) {
        holder.name.setText(model.get(i).getName());
        holder.them.setText(model.get(i).getText());
        holder.date.setText(model.get(i).getDate());
        //}
        return view;
    }

    protected void show(Holder holder) {
        if (level == 4) holder.name.setVisibility(View.VISIBLE);
        else holder.name.setVisibility(View.GONE);
    }

    class Holder {
        TextView name;
        TextView them;
        TextView date;
    }
}
