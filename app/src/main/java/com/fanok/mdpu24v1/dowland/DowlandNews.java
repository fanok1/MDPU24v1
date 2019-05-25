package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.fanok.mdpu24v1.Article;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.activity.NewsActivity;
import com.fanok.mdpu24v1.adapter.CastomAdapter;
import com.fanok.mdpu24v1.fragment.FragmentNewsUniversity;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DowlandNews extends DowladParent {
    private static ArrayList<Article> articleList = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static ListView listView;

    public DowlandNews(@NonNull View view, @NonNull String url, @NonNull ListView listView) {
        super(view, url);
        DowlandNews.listView = listView;
        setData("action", "load_more");
        setData("post_style", "timeline");
        setData("eael_show_image", "1");
        setData("image_size", "medium");
        setData("eael_show_title", "1");
        setData("eael_show_excerpt", "0");
        setData("eael_excerpt_length", "10");
        setData("post_type", "post");
        setData("posts_per_page", "10");
    }

    public void clear() {
        articleList.clear();
    }

    @Override
    protected void onPreExecute() {
        listView.setEnabled(false);
        super.onPreExecute();
    }

    @Override
    protected void parce(Document data) {
        Elements articles = data.getElementsByTag("article");
        for (int i = 1; i < articles.size(); i++) {
            Article article1Item = new Article();
            article1Item.setUrlArticle(articles.get(i).getElementsByTag("a").get(0).attr("href"));
            article1Item.setTitle(articles.get(i).getElementsByTag("a").get(0).attr("title"));
            article1Item.setDate(articles.get(i).getElementsByTag("time").get(0).attr("datetime"));
            String bg = articles.get(i).getElementsByClass("eael-timeline-post-image").get(0).attr("style");
            if (bg.contains("http")) {
                bg = bg.substring(bg.indexOf("http"), bg.indexOf("')"));

            } else bg = "";
            article1Item.setImage(bg);
            articleList.add(article1Item);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        int position;
        if (articleList == null)
            Toast.makeText(getView().getContext(), getView().getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_SHORT).show();

        CastomAdapter adapter = new CastomAdapter(getView().getContext(), articleList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Article article = (Article) adapter.getItem(i);
            String url = article.getUrlArticle();
            String title = article.getTitle();
            Intent intent = new Intent(view.getContext(), NewsActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("title", title);
            view.getContext().startActivity(intent);
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (adapter.getCount() - 1)) {

                    if (new InsertDataInSql(view, "").isOnline()) {
                        DowlandNews dowlandNews = new DowlandNews(getView(), getUrl(), listView);
                        dowlandNews.setProgressBar(getView().findViewById(R.id.progressBarBotom));
                        dowlandNews.setData("offset", String.valueOf(FragmentNewsUniversity.offset));
                        FragmentNewsUniversity.offset += 10;
                        dowlandNews.execute();
                    } else
                        Toast.makeText(view.getContext(), view.getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setSelection(adapter.getCount() - 11);

        listView.setEnabled(true);

        super.onPostExecute(aVoid);
    }


}
