package com.fanok.mdpu24v1;

import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.nodes.Document;

public class ResponseBody {
    private Document res;
    private View view;
    private ProgressBar progressBar;


    public ResponseBody(Document res, View view) {
        this.res = res;
        this.view = view;
    }

    public ResponseBody(Document res, View view, ProgressBar progressBar) {
        this.res = res;
        this.view = view;
        this.progressBar = progressBar;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Document getRes() {
        return res;
    }

    public View getView() {
        return view;
    }
}
