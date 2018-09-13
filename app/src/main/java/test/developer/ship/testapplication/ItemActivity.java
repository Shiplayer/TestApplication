package test.developer.ship.testapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import test.developer.ship.testapplication.entity.Offer;

/**
 * Created by Shiplayer on 13.09.18.
 */
public class ItemActivity extends AppCompatActivity {
    private final static String TAG = ItemActivity.class.getSimpleName();
    private TextView title;
    private ImageView logoView;
    private TextView shortDescription;
    private TextView fullDescription;
    private Button button;
    private WebView webView;
    private ImageView errorImage;
    private TextView errorText;
    private ScrollView content;
    private View errorBrowserLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        title = findViewById(R.id.tv_title);
        logoView = findViewById(R.id.logo_view_item);
        shortDescription = findViewById(R.id.tv_description);
        fullDescription = findViewById(R.id.tv_full_desc);
        webView = findViewById(R.id.webView_browser);
        button = findViewById(R.id.button);
        errorImage = findViewById(R.id.iv_error_item);
        errorText = findViewById(R.id.tv_error_item);
        content = findViewById(R.id.sv_content);
        errorBrowserLayout = findViewById(R.id.error_browser_layout);
        ActionBar bar = getActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
        }
        Picasso.get().load(R.drawable.ic_error_black_24dp).fit().centerCrop().into(errorImage);

        if(getIntent() != null){
            Intent intent = getIntent();
            if(intent.hasExtra("offer")){
                showContent();
                final Offer offer = intent.getParcelableExtra("offer");
                Log.i(TAG, offer.toString());
                title.setText(offer.getName());
                Picasso.get().load(offer.getLogo()).fit().centerCrop().into(logoView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.drawable.ic_error_black_24dp).fit().centerCrop().into(logoView);
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                        logoView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v, "Произошла ошибка при загрузке логотипа", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                    }
                });
                shortDescription.setText(offer.getDes());
                fullDescription.setText(offer.getDesc_full());
                if(offer.getBtn2() == null){
                    if(offer.getBtn() != null){
                        button.setText(offer.getBtn());
                    } else
                        button.setVisibility(View.GONE);
                } else
                    button.setText(offer.getBtn2());
                if(button.getVisibility() != View.GONE){
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(offer.isBrowser()){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(offer.getUrl()));
                                startActivity(intent);
                            } else {
                                showWebView();
                                webView.setVisibility(View.VISIBLE);
                                webView.loadUrl(offer.getUrl());
                            }
                        }
                    });
                }
            } else{
                showError();
                errorText.setText("Ошибка при получении оффера");
            }
        } else {
            showError();
            errorText.setText("Ошибка при передачи оффера");
        }
    }

    public void showError(){
        content.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        errorBrowserLayout.setVisibility(View.VISIBLE);
        errorImage.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

    public void showWebView(){
        content.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        errorBrowserLayout.setVisibility(View.VISIBLE);
        errorImage.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    public void showContent(){
        webView.setVisibility(View.GONE);
        errorBrowserLayout.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(webView.getVisibility() == View.VISIBLE){
                showContent();
                return false;
            } else
                this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }
}
