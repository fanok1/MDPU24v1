package com.fanok.mdpu24v1.adapter;

import android.content.Context;
import android.view.View;

import com.fanok.mdpu24v1.MyTask;

import java.util.ArrayList;

public class ProjectAdapter extends TaskAdapter {


    public ProjectAdapter(Context context, ArrayList<MyTask> model) {
        super(context, model);
    }

    @Override
    protected void show(Holder holder) {
        holder.name.setVisibility(View.VISIBLE);
    }
}
