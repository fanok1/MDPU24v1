package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.fanok.mdpu24v1.MyOnItemClickListner;
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


        if (level == 2 || level == 4)
            listView.setOnItemClickListener(new MyOnItemClickListner(students));

        if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);
    }
}