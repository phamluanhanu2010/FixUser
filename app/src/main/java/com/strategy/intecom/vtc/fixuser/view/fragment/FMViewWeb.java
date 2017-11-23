package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/19/16.
 */
@SuppressLint("ValidFragment")
public class FMViewWeb extends AppCore implements SwipeRefreshLayout.OnRefreshListener {

    private View viewRoot;

    private Callback callback;

    public static String sURL = "";
    public static String sTitle = "";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView btn_back;
    private TextView tv_title;

    public static boolean sIsShowing = false;

    @SuppressLint("ValidFragment")
    public FMViewWeb(Callback callback) {
        this.callback = callback;
    }

    public FMViewWeb() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if(savedInstanceState != null){
            sURL = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_URL);
            sTitle = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_TITLE);
        }
        return inflater.inflate(R.layout.ui_view_web, container,
                false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mWebView = (WebView) view.findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mProgressBar.setVisibility(View.VISIBLE);

                // Return the app name after finish loading
                if (progress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.clearCache(false);
        mWebView.clearHistory();
        mWebView.loadUrl(sURL);
        mWebView.requestFocus();

        tv_title.setText(sTitle);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    cmdBack();
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        mWebView.clearCache(false);
        mWebView.clearHistory();
        mWebView.loadUrl(sURL);
        mWebView.requestFocus();
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            //mProgressBar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //mProgressBar.setVisibility(View.GONE);
        }
    }


//        @Override
//        public void cmdTitleBackImage() {
//
//            if (mWebView.canGoBack()) {
//                mWebView.goBack();
//            } else {
//                super.cmdTitleBackImage();
//            }
//        }


    @Override
    public void onDestroy() {
        sIsShowing = false;
        super.onDestroy();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }*/

}
