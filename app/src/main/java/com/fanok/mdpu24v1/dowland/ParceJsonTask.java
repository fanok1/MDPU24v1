package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.MyTask;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.adapter.TaskAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ParceJsonTask extends AsyncTask<Void, Void, ArrayList<MyTask>> {
    @SuppressLint("StaticFieldLeak")
    private String json;
    @SuppressLint("StaticFieldLeak")
    private ListView listView;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;

    public ParceJsonTask(String json, ListView listView) {
        this.json = json;
        this.listView = listView;
    }

    public ListView getListView() {
        return listView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null) progressBar.setVisibility(ProgressBar.VISIBLE);

    }

    @Override
    protected ArrayList<MyTask> doInBackground(Void... voids) {
        ArrayList<MyTask> tasks = new ArrayList<>();
        //tasks.add(null);
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            MyTask task = new MyTask();
            JsonObject jStudent = jsonArray.get(i).getAsJsonObject();
            task.setName(jStudent.get("name").getAsString());
            task.setText(jStudent.get("text").getAsString());
            task.setDate(jStudent.get("date").getAsLong());
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    protected void onPostExecute(ArrayList<MyTask> tasks) {
        super.onPostExecute(tasks);
        TaskAdapter adapter = new TaskAdapter(listView.getContext(), tasks);
        listView.setAdapter(adapter);

        SharedPreferences mPref = listView.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        int level = mPref.getInt("level", 0);

        if (level == 4) listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view, Gravity.END);
            MyTask task = (MyTask) adapter.getItem(i);
            popupMenu.inflate(R.menu.popup_menu_delete);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.delete) {
                    String url = view.getResources().getString(R.string.server_api) + "task_delete.php";
                    InsertDataInSql inSql = new InsertDataInSql(view, url);

                    if (inSql.isOnline()) {
                        inSql.setData("name", task.getName());
                        inSql.setData("task", task.getText());
                        inSql.execute();
                        tasks.remove(i);
                        TaskAdapter newAdapter = new TaskAdapter(listView.getContext(), tasks);
                        listView.setAdapter(newAdapter);


                    } else {
                        Toast.makeText(view.getContext(), view.getResources().getText(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            });
            popupMenu.show();
            return false;
        });
        if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);
    }
}
