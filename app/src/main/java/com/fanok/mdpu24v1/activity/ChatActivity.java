package com.fanok.mdpu24v1.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.fanok.mdpu24v1.Message;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.adapter.ChatAdapter;
import com.fanok.mdpu24v1.dowland.DowlandChat;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.fanok.mdpu24v1.dowland.UpdateChat;
import com.r0adkll.slidr.Slidr;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    public static final String ACTION = "updateData";
    public static int offset;
    private TextInputEditText editText;
    private ListView listView;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DowlandChat.clear();

        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", 5);
        editor.apply();

        setContentView(R.layout.activity_chat);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        offset = 0;

        editText = findViewById(R.id.edit);
        ImageButton button = findViewById(R.id.button);
        /*button.setEnabled(false);
        editText.setOnKeyListener((view, i, keyEvent) -> {
            if(editText.getText().toString().isEmpty()) button.setEnabled(false);
            else button.setEnabled(true);
            return false;
        });*/

        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                onClick(textView);
                return true;
            }
            return false;
        });

        button.setOnClickListener(this::onClick);

        listView = findViewById(R.id.listChat);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            dowland(button, refreshLayout, listView);
        });
        dowland(button, refreshLayout, listView);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String url = getResources().getString(R.string.server_api) + "chat.php";
                UpdateChat updateChat = new UpdateChat(button, url, listView);
                SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
                String login = mPref.getString("login", "");
                String level = String.valueOf(mPref.getInt("level", 0));
                if (updateChat.isOnline()) {
                    Message message = DowlandChat.getChat().get(DowlandChat.getChat().size() - 1);
                    long time = message.getDate().getTime();
                    updateChat.setRefreshLayout(refreshLayout);
                    updateChat.setData("action", "select");
                    updateChat.setData("author", login);
                    updateChat.setData("level", level);
                    updateChat.setData("group", TypeTimeTable.getGroup());
                    updateChat.setData("data", String.valueOf(time));
                    updateChat.execute();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            }
        };

        IntentFilter intFilt = new IntentFilter(ACTION);
        registerReceiver(br, intFilt);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private void dowland(View view, SwipeRefreshLayout refreshLayout, ListView listView) {
        String url = getResources().getString(R.string.server_api) + "chat.php";
        DowlandChat dowlandChat = new DowlandChat(view, url, listView);
        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        String login = mPref.getString("login", "");
        String level = String.valueOf(mPref.getInt("level", 0));
        if (dowlandChat.isOnline()) {
            dowlandChat.setRefreshLayout(refreshLayout);
            dowlandChat.setData("offset", String.valueOf(offset));
            dowlandChat.setData("action", "select");
            dowlandChat.setData("author", login);
            dowlandChat.setData("level", level);
            Bundle arguments = getIntent().getExtras();
            if (arguments != null && arguments.get("group") != null) {
                dowlandChat.setData("group", Objects.requireNonNull(arguments.get("group")).toString());
            } else {
                dowlandChat.setData("group", TypeTimeTable.getGroup());
            }

            offset++;
            dowlandChat.execute();
        } else {
            Toast.makeText(view.getContext(), getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }

    private void onClick(View view) {
        String url = getResources().getString(R.string.server_api) + "chat.php";
        InsertDataInSql inSql = new InsertDataInSql(view, url);
        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        String login = mPref.getString("login", "");
        String level = String.valueOf(mPref.getInt("level", 0));
        String name = String.valueOf(mPref.getString("name", ""));
        String photo = String.valueOf(mPref.getString("photo", ""));
        MediaPlayer mp = MediaPlayer.create(this, R.raw.send);
        if (inSql.isOnline()) {
            inSql.setData("action", "insert");
            inSql.setData("author", login);
            inSql.setData("level", level);
            inSql.setData("text", editText.getText().toString());
            inSql.setData("group", TypeTimeTable.getGroup());
            inSql.execute();
            DowlandChat.getChat().add(new Message(name, editText.getText().toString(), photo, System.currentTimeMillis()));
            ChatAdapter adapter = new ChatAdapter(view.getContext(), DowlandChat.getChat());
            listView.setAdapter(adapter);
            editText.setText("");
            mp.start();
        } else
            Toast.makeText(view.getContext(), "Не удалось отправить сообщение", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
