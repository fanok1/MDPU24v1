package com.fanok.mdpu24v1.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfn);

        Bundle arguments = getIntent().getExtras();
        String site = null;
        String title = null;
        int activity = 0;

        if (arguments != null) {
            site = arguments.getString("url");
            title = arguments.getString("title");
            activity = arguments.getInt("type");
        }

        setTitle(title);


        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME,
                StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", activity);
        editor.apply();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        }


        ProgressBar progressBar = findViewById(R.id.progressBar);
        SwipeRefreshLayout refresh = findViewById(R.id.refresh);
        webView = findViewById(R.id.web);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressBar.getVisibility() == ProgressBar.INVISIBLE) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    refresh.setRefreshing(false);
                }
            }
        });
        webView.loadUrl(site);
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        refresh.setOnRefreshListener(webView::reload);

        webView.setDownloadListener(
                (url, userAgent, contentDisposition, mimetype, contentLength) -> {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimetype);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Загрузка...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,
                            "");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    if (dm != null) {
                        dm.enqueue(request);
                        Toast.makeText(this, "Загрузка...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
            case R.id.refresh:
                webView.reload();
                return true;
            case R.id.undo:
                webView.goForward();
                return true;
            case R.id.pc:
                item.setChecked(!item.isChecked());
                setDesktopMode(webView, item.isChecked());
                return true;
            case R.id.open_crome:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
                startActivity(browserIntent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_view_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.find);
        SearchView searchView = (SearchView) searchItem.getActionView();
        TypedValue tv = new TypedValue();

        ActionBar.LayoutParams navButtonsParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);
        navButtonsParams.rightMargin = 30;

        ImageButton btnNext = new ImageButton(this);
        btnNext.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
        btnNext.setBackgroundColor(Color.TRANSPARENT);
        btnNext.setOnClickListener(view -> webView.findNext(true));

        ImageButton btnPrev = new ImageButton(this);
        btnPrev.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
        btnPrev.setBackgroundColor(Color.TRANSPARENT);
        btnPrev.setOnClickListener(view -> webView.findNext(false));


        TextView searchStats = new TextView(this);
        searchStats.setGravity(Gravity.CENTER_VERTICAL);
        searchStats.setTextColor(Color.WHITE);

        webView.setFindListener((i, i1, b) -> {
            String s;
            if (i1 > 0) {
                s = (i + 1) + "/" + i1;
            } else {
                s = "0/0";
            }
            searchStats.setText(s);
        });

        ((LinearLayout) searchView.getChildAt(0)).addView(searchStats, navButtonsParams);
        ((LinearLayout) searchView.getChildAt(0)).addView(btnPrev, navButtonsParams);
        ((LinearLayout) searchView.getChildAt(0)).addView(btnNext, navButtonsParams);

        ((LinearLayout) searchView.getChildAt(0)).setGravity(Gravity.CENTER_VERTICAL);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                webView.findAllAsync(newText);
                return false;
            }
        });
        return true;
    }

    private void setDesktopMode(WebView webView, boolean enabled) {
        String newUserAgent;
        if (enabled) {
            newUserAgent =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like "
                            + "Gecko) Chrome/74.0.3729.169 Safari/537.36";
        } else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(enabled);
        webView.getSettings().setLoadWithOverviewMode(enabled);
        webView.getSettings().setSupportZoom(enabled);

        webView.reload();
    }
}

class MyWebViewClient extends WebViewClient {
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }


}


