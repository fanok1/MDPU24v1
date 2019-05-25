package com.fanok.mdpu24v1;

import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.fanok.mdpu24v1.activity.MainActivity;
import com.fanok.mdpu24v1.activity.StudentInfoActivity;
import com.fanok.mdpu24v1.dowland.InsertDataInSqlConfirmStudent;

import java.util.ArrayList;

public class MyOnItemClickListner implements AdapterView.OnItemClickListener {

    private static ArrayList<Student> list;
    private ProgressBar progressBar;
    private MainActivity activity;

    public MyOnItemClickListner(MainActivity activity, ArrayList<Student> list, ProgressBar progressBar) {
        MyOnItemClickListner.list = list;
        this.activity = activity;
        this.progressBar = progressBar;
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
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.confirm_student);
            popupMenu
                    .setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.confirm:
                                click(view, 1, i);
                                return true;
                            case R.id.cancel:
                                click(view, 0, i);
                                return true;
                            default:
                                return false;
                        }
                    });
            popupMenu.show();
        }
    }

    private void click(View view, int b, int item) {
        final String url = view.getContext().getResources().getString(R.string.server_api) + "confirm_student.php";
        InsertDataInSqlConfirmStudent inSql = new InsertDataInSqlConfirmStudent(view, url, activity);
        if (inSql.isOnline()) {
            inSql.setData("name", list.get(item).getName());
            inSql.setData("type", String.valueOf(b));
            inSql.setProgressBar(progressBar);
            inSql.execute();
        }
    }
}
