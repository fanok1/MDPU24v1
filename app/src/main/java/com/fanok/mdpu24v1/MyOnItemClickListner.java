package com.fanok.mdpu24v1;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.fanok.mdpu24v1.activity.PopupConfirmStudent;
import com.fanok.mdpu24v1.activity.StudentInfoActivity;

import java.util.ArrayList;

public class MyOnItemClickListner implements AdapterView.OnItemClickListener {

    private static ArrayList<Student> list;

    public MyOnItemClickListner(ArrayList<Student> list) {
        MyOnItemClickListner.list = list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (list.get(i).getConfirm() == 1) {
            Intent intent = new Intent(view.getContext(), StudentInfoActivity.class);
            intent.putExtra("name", list.get(i).getName());
            intent.putExtra("email", list.get(i).getEmail());
            intent.putExtra("phone", list.get(i).getPhone());
            view.getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(view.getContext(), PopupConfirmStudent.class);
            intent.putExtra("name", list.get(i).getName());
            view.getContext().startActivity(intent);
        }
    }
}
