package com.nn.hh;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    private AdView bannerAdView;
    private RewardedAd rewardedAd;
    private Handler handler;
    private Runnable adRunnable;
    private static final int AD_INTERVAL = 5 * 60 * 1000; // 5 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup WebView
        WebView webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://nawty.netlify.app");

        // Initialize AdMob
        MobileAds.initialize(this, initializationStatus -> {});

        // Load Banner Ad
        bannerAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

        // Load and show Rewarded Ad on app open
        loadRewardedAd(() -> showRewardedAd());

        // Schedule Rewarded Ads every 5 minutes
        handler = new Handler();
        adRunnable = new Runnable() {
            @Override
            public void run() {
                loadRewardedAd(() -> showRewardedAd());
                handler.postDelayed(this, AD_INTERVAL);
            }
        };
        handler.postDelayed(adRunnable, AD_INTERVAL);
    }

    private void loadRewardedAd(Runnable onLoaded) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this,
                "ca-app-pub-3940256099942544/5224354917", // Test Rewarded Ad Unit ID
                adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        rewardedAd = ad;
                        if (onLoaded != null) onLoaded.run();
                    }
                });
    }

    private void showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd.show
