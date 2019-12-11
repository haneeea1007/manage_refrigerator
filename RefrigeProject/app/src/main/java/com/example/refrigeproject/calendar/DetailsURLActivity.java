package com.example.refrigeproject.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeproject.R;
import com.r0adkll.slidr.Slidr;

public class DetailsURLActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_url);
        Slidr.attach(this);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        webView = findViewById(R.id.webView);

        // setting WebView
        webView.getSettings().setJavaScriptEnabled(true);
        // load url
        webView.loadUrl(url);
        // chrome browser
        webView.setWebChromeClient(new WebChromeClient());
        // no more new window when click
        webView.setWebViewClient(new WebViewClientClass());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
        } else if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String strUrl) {
            view.loadUrl(strUrl);
            return true;
        }


    }
}
