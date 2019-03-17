package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.adapter.ScreenSlidePagerAdapter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DowlandNewsPage extends DowladParent {

    private String text;
    @SuppressLint("StaticFieldLeak")
    private List<String> imagesUrl;
    private FragmentManager fragmentManager;

    public DowlandNewsPage(View view, String url, FragmentManager fragmentManager) {
        super(view, url);
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void parce(Document data) {
        imagesUrl = new ArrayList<>();
        Element main = data.getElementById("main");
        Element article = main.getElementsByTag("article").first();
        Elements p = article.getElementsByClass("entry-content").first().getElementsByTag("p");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < p.size(); i++) {
            stringBuilder.append(p.get(i).text());
            stringBuilder.append("\n\n");
        }
        text = stringBuilder.toString();

        Element gallery = data.getElementById("gallery-2");
        if (!(gallery == null)) {
            Elements images = gallery.getElementsByTag("figure");
            for (int i = 0; i < images.size(); i++) {
                imagesUrl.add(images.get(i).getElementsByTag("a").first().attr("href"));
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        TextView textView = getView().findViewById(R.id.arcticl);
        textView.setText(text);

        ViewPager pager = getView().findViewById(R.id.pager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        pagerAdapter.addAll(imagesUrl);
        pager.setAdapter(pagerAdapter);


        super.onPostExecute(aVoid);
    }
}