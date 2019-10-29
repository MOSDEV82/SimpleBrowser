package org.mosdev.simplebrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is the main activity for the simple browser. It allows to enter an url and navigate between pages.
 * It is possible to tap on "enter/return" or click on "Go" button to visit an url.
 */
public class MainActivity extends AppCompatActivity {

    // Declare elements used in activity_main.xml to get a reference of them
    private ImageButton backImageButton;
    private ImageButton forwardImageButton;
    private EditText urlEditText;
    private ImageButton goImageButton;
    private WebView webView;

    private static final String START_URL = "https://google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.backImageButton = findViewById(R.id.backImageButton);
        this.forwardImageButton = findViewById(R.id.forwardImageButton);
        this.urlEditText = findViewById(R.id.urlEditText);
        this.goImageButton = findViewById(R.id.goImageButton);
        this.webView = findViewById(R.id.webView);

        // config webView
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            /**
             * Allows to use web content on this app, instead to load default browser!
             * @param view
             * @param request
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                urlEditText.setText(view.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }

            /**
             * Update urlEditText to current url
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                urlEditText.setText(url);
            }
        });

        // Add buttons listener
        this.backImageButton.setOnClickListener(view -> {
            if (this.webView.canGoBack())
                this.webView.goBack();

        });

        this.forwardImageButton.setOnClickListener(view -> {
            if (this.webView.canGoForward())
                this.webView.goForward();
        });

        this.goImageButton.setOnClickListener(view -> {
            visitURL(this.urlEditText.getText().toString());
        });

        // make urlEditText go to webseite on click "Enter"
        this.urlEditText.setOnKeyListener((view, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                visitURL(this.urlEditText.getText().toString());
                return true;
            }
            return false;
        });

        // Go to startpage
        visitURL(START_URL);
    }

    /**
     * Open the webpage for url
     * @param urlString
     */
    private void visitURL(String urlString) {
        try {
            // Get text in EditText and remove spaces
            urlString = urlString.trim();
            // If url doesn't contain "://" to add "https://" to the entered text
            if (!urlString.contains("://")) urlString = "https://" + urlString;
            URL url = new URL(urlString);
            this.webView.loadUrl(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
