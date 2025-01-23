package com.example.sikhoapp.presentation.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.rememberAsyncImagePainter
import com.example.sikhoapp.model.AnimDetailsResponse

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ShowAnimeDetail(
    response: AnimDetailsResponse?,
    isFullScreen: Boolean,
    updateFullScreen: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    val scrollState = rememberScrollState()

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        Column {
            val youtubeId = response?.data?.trailer?.youtubeId
            val title = response?.data?.title
            if (youtubeId != null) {
                WebViewWithFullScreen(
                    response.data.trailer.embedUrl.orEmpty(),
                    isFullScreen = isFullScreen,
                    title = title.orEmpty()
                ) {
                    updateFullScreen(it)
                }
            } else {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(
                        model = response?.data?.trailer?.images?.maximumImageUrl
                            ?: response?.data?.images?.webp?.largeImageUrl
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
fun WebViewWithFullScreen(
    videoUrl: String,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean,
    title: String,
    updateFullScreen: (Boolean) -> Unit,
) {
    modifier.fillMaxSize()
    val context = LocalContext.current
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
                            updateFullScreen(false)
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
                            updateFullScreen(true)
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
                        updateFullScreen(false)
                    }
                }

                //loadUrl(videoUrl)
                loadData(
                    "<iframe width=\"1100\" height=\"600\" src=\"$videoUrl\" title=\"$title\" frameborder=\"0\" allow=\"accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>",
                    "text/html",
                    "utf-8"
                )
            }
        },
        modifier = modifier
    )

    // Handle back press to exit full-screen mode
    BackHandler(enabled = isFullScreen) {
        updateFullScreen(false)
        (context as ComponentActivity).requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}