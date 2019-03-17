package com.fanok.mdpu24v1.dowland;

import android.support.annotation.NonNull;
import android.view.View;

import org.jsoup.nodes.Document;

public class DowlandJsonSettings extends DowladParent {

    private String result;

    public DowlandJsonSettings(@NonNull View view, @NonNull String url) {
        super(view, url);
    }


    @Override
    protected void parce(Document data) {
        result = data.getElementsByTag("body").text();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ParceJsonSettings parceJson = new ParceJsonSettings(result, getView());
        parceJson.setProgressBar(getProgressBar());
        parceJson.execute();

    }
}
