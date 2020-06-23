package cfwz.skiti.go4lunch.ui.restaurant_profile;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.R;


public class WebViewActivity extends AppCompatActivity {
    @BindView(R.id.simple_toolbar) Toolbar mToolbar;
    @BindView(R.id.webview_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.webview) WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        this.configureToolbar();
        this.displayWebView();
        this.configureSwipeRefreshLayout();
    }

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();

    }

    private void configureSwipeRefreshLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(this::displayWebView);
    }

    private void displayWebView(){
        String url = getIntent().getStringExtra("Website");
        if (url != null){
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.loadUrl(url);
            mWebView.setWebViewClient(new WebViewClient());
            mSwipeRefreshLayout.setRefreshing(false);
        }else{
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
