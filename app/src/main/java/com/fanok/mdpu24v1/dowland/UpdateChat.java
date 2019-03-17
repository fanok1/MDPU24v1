package com.fanok.mdpu24v1.dowland;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.fanok.mdpu24v1.Message;

public class UpdateChat extends DowlandChat {
    public UpdateChat(@NonNull View view, @NonNull String url, @NonNull ListView listView) {
        super(view, url, listView);
    }

    @Override
    protected void setMessage(String name, String text, String photo, long time) {
        getChat().add(new Message(name, text, photo, time));
        NotificationManager notificationManager = (NotificationManager) getView().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(0);
        }
    }
}
