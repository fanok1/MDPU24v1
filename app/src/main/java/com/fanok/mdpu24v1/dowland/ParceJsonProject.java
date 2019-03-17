package com.fanok.mdpu24v1.dowland;

import android.view.Gravity;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.MyTask;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.adapter.ProjectAdapter;

import java.util.ArrayList;

public class ParceJsonProject extends ParceJsonTask {

    private String studentName;


    public ParceJsonProject(String json, ListView listView, String studentName) {
        super(json, listView);
        this.studentName = studentName;
    }

    @Override
    protected void onPostExecute(ArrayList<MyTask> tasks) {
        ProjectAdapter adapter = new ProjectAdapter(getListView().getContext(), tasks);
        getListView().setAdapter(adapter);

        if (TypeTimeTable.getType() == TypeTimeTable.teacherTimeTable)
            getListView().setOnItemLongClickListener((adapterView, view, i, l) -> {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view, Gravity.END);
                MyTask task = (MyTask) adapter.getItem(i);
                popupMenu.inflate(R.menu.popup_menu_delete);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.delete) {
                        String url = view.getResources().getString(R.string.server_api) + "project_delete.php";
                        InsertDataInSql inSql = new InsertDataInSql(view, url);

                        if (inSql.isOnline()) {
                            inSql.setData("student", studentName);
                            inSql.setData("predmet", task.getName());
                            inSql.setData("task", task.getText());
                            inSql.execute();
                            tasks.remove(i);
                            ProjectAdapter newAdapter = new ProjectAdapter(getListView().getContext(), tasks);
                            getListView().setAdapter(newAdapter);


                        } else {
                            Toast.makeText(view.getContext(), view.getResources().getText(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
                        }
                    }
                    return false;
                });
                popupMenu.show();
                return false;
            });
        if (getProgressBar() != null) getProgressBar().setVisibility(ProgressBar.GONE);
    }
}
