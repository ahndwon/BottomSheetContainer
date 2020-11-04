package com.ahndwon.bottomsheetcontainer

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import kotlinx.android.synthetic.main.fragment_webview.*
import kotlinx.android.synthetic.main.fragment_webview.view.*

/*
 *  WebViewFragment.kt
 *
 *  Created by Donghyun An on 2020/11/04
 *  Copyright © 2020 Shinhan Bank. All rights reserved.
 */

class WebViewFragment: BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_webview, container, false)

        val width = getDisplayWidth(requireContext())
        val height = getDisplayHeight(requireContext())
        var params: ViewGroup.LayoutParams? = view.webView.layoutParams

        if (params == null) {
            params = ViewGroup.LayoutParams(width, height)
        } else {
            params.width = width
            params.height = width
        }

        view.webView.layoutParams = params

        view.webView.getSettings().setJavaScriptEnabled(true)
        view.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        view.webView.getSettings().setDomStorageEnabled(true)
        view.webView.getSettings().setUseWideViewPort(true)
        view.webView.getSettings().setLoadWithOverviewMode(true)
//        view.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
        }
        view.webView.setHorizontalScrollBarEnabled(false)
//        webView.setWebViewClient(com.shinhan.spplatform.nativemain.account.SmartOfferPopupFragment.WebViewClientClass())
        view.webView.setWebChromeClient(WebChromeClient())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            view.webView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }

        view.webView.setBackgroundColor(0)
        view.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)


        view.webView.loadUrl("https://devimg3.shinhan.com/sbank/memo/main_offer_bs_a2.html")

        return view
    }

    fun getDisplayWidth(context: Context): Int {
        val dm = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getDisplayHeight(context: Context): Int {
        val dm = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }
}