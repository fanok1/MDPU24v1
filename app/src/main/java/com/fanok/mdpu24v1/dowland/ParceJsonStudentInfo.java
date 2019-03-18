package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.MyOnItemClickListner;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.Student;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ParceJsonStudentInfo extends AsyncTask<Void, Void, ArrayList<Student>> {

    @SuppressLint("StaticFieldLeak")
    private String json;
    @SuppressLint("StaticFieldLeak")
    private ListView listView;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;

    public ParceJsonStudentInfo(String json, ListView listView) {
        this.json = json;
        this.listView = listView;
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
    protected ArrayList<Student> doInBackground(Void... voids) {
        ArrayList<Student> students = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Student student = new Student();
            JsonObject jStudent = jsonArray.get(i).getAsJsonObject();
            student.setName(jStudent.get("name").getAsString());
            student.setEmail(jStudent.get("email").getAsString());
            student.setPhone(jStudent.get("phone").getAsString());
            student.setConfirm(jStudent.get("confirm").getAsInt());
            students.add(student);
        }
        return students;
    }

    @Override
    protected void onPostExecute(ArrayList<Student> students) {
        super.onPostExecute(students);
        SharedPreferences mPref = listView.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        int level = mPref.getInt("level", 0);
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            if (level == 4 || students.get(i).getConfirm() == 1)
                names.add(students.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(listView.getContext(), android.R.layout.simple_list_item_1, names);

        listView.setAdapter(adapter);


        if (level == 2 || level == 4) {
            listView.setOnItemClickListener(new MyOnItemClickListner(students));

            listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                String url = listView.getContext().getResources().getString(R.string.server_api) + "set_starosta.php";
                if (students.get(i).getConfirm() == 1) {
                    PopupMenu popupMenu = new PopupMenu(listView.getContext(), view);
                    popupMenu.inflate(R.menu.menu_set_starosta);
                    popupMenu.setOnMenuItemClickListener(menuItem -> {
                        if (menuItem.getItemId() == R.id.setStarosta) {

                            InsertDataInSql inSql = new InsertDataInSql(view, url);
                            if (inSql.isOnline()) {
                                inSql.setData("name", students.get(i).getName());
                                inSql.execute();
                            } else
                                Toast.makeText(view.getContext(), R.string.error_no_internet_conection, Toast.LENGTH_LONG).show();
                        }
                        return false;
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        popupMenu.setGravity(Gravity.END);
                    }
                    popupMenu.show();
                }
                return false;
            });
        }

        if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);
    }
}