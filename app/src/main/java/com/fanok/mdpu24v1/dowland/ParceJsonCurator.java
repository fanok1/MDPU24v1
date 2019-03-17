package com.fanok.mdpu24v1.dowland;

import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.MyTask;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.adapter.CuratorAdapter;

import java.util.ArrayList;

public class ParceJsonCurator extends ParceJsonTask {

    private String studentName;


    public ParceJsonCurator(String json, ListView listView) {
        super(json, listView);
    }

    @Override
    protected void onPostExecute(ArrayList<MyTask> tasks) {
        CuratorAdapter adapter = new CuratorAdapter(getListView().getContext(), tasks);
        getListView().setAdapter(adapter);
        SharedPreferences mPref = getListView().getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        int level = mPref.getInt("level", 0);
        String login = mPref.getString("login", "");

        if (level == 4)
            getListView().setOnItemLongClickListener((adapterView, view, i, l) -> {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view, Gravity.END);
                MyTask task = (MyTask) adapter.getItem(i);
                popupMenu.inflate(R.menu.popup_menu_delete);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.delete) {
                        String url = view.getResources().getString(R.string.server_api) + "curator_houers_delete.php";
                        InsertDataInSql inSql = new InsertDataInSql(view, url);

                        if (inSql.isOnline()) {
                            inSql.setData("level", String.valueOf(level));
                            inSql.setData("login", login);
                            inSql.setData("task", task.getText());
                            inSql.execute();
                            tasks.remove(i);
                            CuratorAdapter newAdapter = new CuratorAdapter(getListView().getContext(), tasks);
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
