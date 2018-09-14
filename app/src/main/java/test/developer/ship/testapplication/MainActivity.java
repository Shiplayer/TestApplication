package test.developer.ship.testapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import test.developer.ship.testapplication.entity.Offer;
import test.developer.ship.testapplication.entity.ResponseEntity;
import test.developer.ship.testapplication.model.ApplicationModel;

public class MainActivity extends AppCompatActivity {
    private ApplicationModel mViewModel;
    private ImageView errorImage;
    private TextView errorText;
    private RecyclerView recyclerView;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(ApplicationModel.class);
        recyclerView = findViewById(R.id.rv_content);
        errorImage = findViewById(R.id.iv_error_main);
        errorText = findViewById(R.id.tv_error_main);
        webView = findViewById(R.id.wv_main);
        progressBar = findViewById(R.id.progressBar);
        BrowserCaller caller = new BrowserCaller() {
            @Override
            public void callBrowser(String url, boolean browser) {
                if(browser){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
                }
            }

            @Override
            public void startOfferActivity(Offer offer) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("offer", offer);
                startActivity(intent);
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final OfferAdapter offerAdapter = new OfferAdapter(caller);
        recyclerView.setAdapter(offerAdapter);
        mViewModel.getOffers().observe(this, new Observer<ResponseEntity>() {
            @Override
            public void onChanged(@Nullable ResponseEntity responseEntity) {
                if(responseEntity != null && responseEntity.isSuccessful()){
                    showContent();
                    progressBar.setVisibility(View.GONE);
                    List<Offer> offerList = new ArrayList<>();
                    for(Offer offer : responseEntity.getList())
                        if(offer.isEnabled())
                            offerList.add(offer);
                    offerAdapter.setOfferList(offerList);
                } else if(responseEntity != null){
                    showError();
                    errorImage.setImageResource(R.drawable.ic_error_black_24dp);
                    errorText.setText(responseEntity.getThrowable().getMessage());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            hiddenError();
            progressBar.setVisibility(View.VISIBLE);
            mViewModel.updateOffers();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showContent(){
        recyclerView.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);
    }

    private void showError(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorImage.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

    private void hiddenError(){
        recyclerView.setVisibility(View.VISIBLE);
        errorImage.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    private final static int BROWSER = 0;
    private final static int WEB_VIEW = 1;
    private final static int START_ACTIVITY = 2;

    @IntDef({BROWSER, WEB_VIEW, START_ACTIVITY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypeIntent{}

    public interface BrowserCaller{
        void callBrowser(String url, boolean browser);
        void startOfferActivity(Offer offer);
    }
}
