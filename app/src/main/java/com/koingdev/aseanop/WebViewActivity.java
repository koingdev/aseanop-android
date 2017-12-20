package com.koingdev.aseanop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wang.avi.AVLoadingIndicatorView;


/**
 * Created by SSK on 01-Jun-17.
 */

public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_white_24dp);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        final AVLoadingIndicatorView indicator = (AVLoadingIndicatorView) findViewById(R.id.avi);
        indicator.show();

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        if (!Constants.webViewURL.isEmpty()) {
            webView.loadUrl(Constants.webViewURL);
        }

        /**
         * Hide loading indicator when page is loading
         */
        webView.setWebViewClient(new WebViewClient(){
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                indicator.hide();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
