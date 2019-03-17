package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.fanok.mdpu24v1.Message;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.activity.ChatActivity;
import com.fanok.mdpu24v1.adapter.ChatAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class DowlandChat extends DowladParent {
    private static ArrayList<Message> chat = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private ListView listView;

    @SuppressLint("StaticFieldLeak")

    public DowlandChat(@NonNull View view, @NonNull String url, @NonNull ListView listView) {
        super(view, url);
        this.listView = listView;
    }

    public static ArrayList<Message> getChat() {
        return chat;
    }

    public static void clear() {
        chat.clear();
    }

    @Override
    protected void onPreExecute() {
        listView.setEnabled(false);
        super.onPreExecute();
    }

    @Override
    protected void parce(Document data) {
        if (!data.getElementsByTag("body").text().equals("null")) {
            JsonElement jsonElement = new JsonParser().parse(data.getElementsByTag("body").text());
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jMarks = jsonArray.get(i).getAsJsonObject();
                String name = jMarks.get("author").getAsString();
                long time = jMarks.get("data").getAsLong();
                String text = jMarks.get("text").getAsString();
                String photo = "null";
                if (!jMarks.get("photo").isJsonNull()) photo = jMarks.get("photo").getAsString();
                setMessage(name, text, photo, time);


            }
        }
    }

    protected void setMessage(String name, String text, String photo, long time) {
        chat.add(0, new Message(name, text, photo, time));
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (chat == null)
            Toast.makeText(getView().getContext(), getView().getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_SHORT).show();

        ChatAdapter adapter = new ChatAdapter(getView().getContext(), chat);
        listView.setAdapter(adapter);


        int position;
        if (chat.size() - 30 * (ChatActivity.offset - 1) > 0)
            position = chat.size() - 30 * (ChatActivity.offset - 1);
        else position = 0;

        if (getRefreshLayout().isRefreshing()) listView.setSelection(position);

        listView.setEnabled(true);
        super.onPostExecute(aVoid);
    }


}
