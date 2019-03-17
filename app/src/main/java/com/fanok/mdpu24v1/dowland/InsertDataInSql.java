package com.fanok.mdpu24v1.dowland;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import org.jsoup.nodes.Document;

public class InsertDataInSql extends DowladParent {

    private String result;


    public InsertDataInSql(@NonNull View view, @NonNull String url) {
        super(view, url);
    }

    @Override
    protected void parce(Document data) {
        result = data.getElementsByTag("body").text();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (result == null)
            Toast.makeText(getView().getContext(), "Ошибка", Toast.LENGTH_LONG).show();

        if (!result.equals(""))
            Toast.makeText(getView().getContext(), result, Toast.LENGTH_LONG).show();
        super.onPostExecute(aVoid);
    }
}
