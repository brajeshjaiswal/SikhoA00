package com.example.sikhoapp.presentation.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.rememberAsyncImagePainter

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ShowAnimeDetail(id: Int?, mainActivityViewModel: MainActivityViewModel, onBack: () -> Unit) {
     BackHandler {
         onBack()
     }
    var isFullScreen by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    ShowLoader(loader = mainActivityViewModel.loader.value)
    LaunchedEffect(key1 = "anim") {
        mainActivityViewModel.callAnimeDetailApi(id)
    }
   // val response = mainActivityViewModel.animeDetailResponse.value

    val response = mainActivityViewModel.animeDetailResponse.value
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        Column {
            val youtubeId = response?.data?.trailer?.youtube_id
            val title = response?.data?.title
            if (youtubeId != null) {
                WebViewWithFullScreen(response?.data?.trailer?.embed_url.orEmpty(), viewModel = mainActivityViewModel)
                /*AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                mediaPlaybackRequiresUserGesture = false
                                useWideViewPort = true
                                loadWithOverviewMode = true
                                allowFileAccess = true
                                cacheMode = WebSettings.LOAD_DEFAULT
                                databaseEnabled = true
                            }

                            // To handle YouTube redirects and ensure proper playback
                            webViewClient = WebViewClient()

                            webChromeClient = object : WebChromeClient() {
                                private var customView: View? = null

                                override fun onShowCustomView(view: View, callback: CustomViewCallback?) {
                                    customView = view
                                    isFullScreen = true
                                    // Replace the WebView's parent layout with the custom full-screen view
                                    (parent as? View)?.visibility = View.GONE
                                    (view.parent as? View)?.apply {
                                        addView(view)
                                        visibility = View.VISIBLE
                                    }
                                }

                                override fun onHideCustomView() {
                                    // Restore the original WebView layout
                                    customView?.let {
                                        (parent as? View)?.apply {
                                            removeView(it)
                                            visibility = View.VISIBLE
                                        }
                                        customView = null
                                    }
                                    isFullScreen = false
                                }
                            }
                            loadData(
                                "<iframe width=\"1100\" height=\"900\" src=\"https://www.youtube.com/embed/ZEkwCGJ3o7M?enablejsapi=1&wmode=opaque&autoplay=1\" title=\"$title\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>",
                                "text/html",
                                "utf-8"
                            )
                            // loadUrl("https://www.youtube.com/embed/MewJ5bEM-5U")
                        }
                    },
                    update = { webView ->
                        webView.loadData(
                            "<iframe width=\"1100\" height=\"900\" src=\"https://www.youtube.com/embed/ZEkwCGJ3o7M?enablejsapi=1&wmode=opaque&autoplay=1\" title=\"$title\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>",
                            "text/html",
                            "utf-8"
                        );
                        // webView.loadUrl("https://www.youtube.com/embed/MewJ5bEM-5U")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                BackHandler(enabled = isFullScreen) {
                    isFullScreen = false
                }*/
            } else {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(
                        model = response?.data?.trailer?.images?.maximum_image_url
                            ?: response?.data?.images?.webp?.large_image_url
                    ),
                    contentDescription = ""
                )
            }


            Text(
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                text = response?.data?.title.orEmpty()
            )

            val mutableStrings = mutableListOf<String>()
            response?.data?.genres?.forEach {
                mutableStrings.add(it.name.orEmpty())
            }

            AnimInfo(label = "Genre: ", labelInfo = mutableStrings.joinToString(", "))
            AnimInfo(label = "Episodes: ", labelInfo = response?.data?.episodes.orEmpty())
            AnimInfo(label = "Rating: ", labelInfo = response?.data?.rating.orEmpty())
            AnimInfo(label = "Synopsis: ", labelInfo = response?.data?.synopsis.orEmpty())
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewWithFullScreen(videoUrl: String, modifier: Modifier = Modifier, viewModel: MainActivityViewModel) {
    modifier.fillMaxSize()
    val context = LocalContext.current
    val isFullScreen = viewModel.isFullScreen.value
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    useWideViewPort = true
                    loadWithOverviewMode = true
                }

                webViewClient = WebViewClient()

                webChromeClient = object : WebChromeClient() {
                    private var customView: View? = null
                    private var customViewCallback: CustomViewCallback? = null
                    private var originalOrientation: Int =
                        (context as ComponentActivity).requestedOrientation

                    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                        customView = view
                        customViewCallback = callback

                        // Switch to full-screen layout
                        if (isFullScreen) {
                            (context as ComponentActivity).requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            (parent as? View)?.visibility = View.VISIBLE
                            (context.window.decorView as? View)?.apply {
                                removeView(customView)
                                systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                            }
                            customView = null
                            customViewCallback?.onCustomViewHidden()
                            viewModel.isFullScreen.value = false
                        } else {
                            (context as ComponentActivity).requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                            (parent as? View)?.visibility = View.GONE
                            (context.window.decorView as? View)?.apply {
                                addView(view)
                                systemUiVisibility = (
                                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                        )
                            }
                            viewModel.isFullScreen.value = true
                        }


                    }

                    override fun onHideCustomView() {
                        customView?.let {
                            (context as ComponentActivity).requestedOrientation =
                                originalOrientation
                            (parent as? View)?.visibility = View.VISIBLE
                            (context.window.decorView as? View)?.apply {
                                removeView(it)
                                systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                            }
                            customView = null
                            customViewCallback?.onCustomViewHidden()
                        }
                        viewModel.isFullScreen.value = false
                    }
                }

                //loadUrl(videoUrl)
                loadData(
                    "<iframe width=\"1100\" height=\"500\" src=\"$videoUrl\" title=\"$title\" frameborder=\"0\" allow=\"accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>",
                    "text/html",
                    "utf-8"
                )
            }
        },
        modifier = modifier
    )

    // Handle back press to exit full-screen mode
    BackHandler(enabled = isFullScreen) {
        viewModel.isFullScreen.value = false
        (context as ComponentActivity).requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}