package edu.temple.composablebrowser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BrowserScreen()
                }
            }
        }
    }
}

fun parseUrl(inputUrl: String): String {
    val trimmed = inputUrl.trim()
    if (trimmed.isEmpty()) return ""

    return if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
        trimmed
    } else {
        "https://$trimmed"
    }
}

@Composable
fun BrowserScreen() {
    // State to track what the user is typing into the TextField
    var inputUrlText by remember { mutableStateOf("") }

    // State to track the active URL loaded inside the WebView
    var currentUrlToLoad by remember { mutableStateOf("https://www.google.com") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar Layout: Row containing the TextField and Go Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputUrlText,
                onValueChange = { inputUrlText = it },
                label = { Text("Enter URL") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Button(
                onClick = {
                    val formattedUrl = parseUrl(inputUrlText)
                    if (formattedUrl.isNotEmpty()) {
                        currentUrlToLoad = formattedUrl
                    }
                }
            ) {
                Text("Go")
            }
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(currentUrlToLoad)
                }
            },
            update = { webView ->
                webView.loadUrl(currentUrlToLoad)
            }
        )
    }
}