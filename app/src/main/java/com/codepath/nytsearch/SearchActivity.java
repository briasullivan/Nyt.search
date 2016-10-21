package com.codepath.nytsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.nytsearch.models.Article;
import com.codepath.nytsearch.models.NewYorkTimesResponse;
import com.codepath.nytsearch.models.NewYorkTimesService;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private static final String URL = "https://api.nytimes.com/";
    private static final String API_KEY = "14c035ebdb164b918ff7e28339fa8570";

    EditText etQuery;
    RecyclerView rvResults;
    Button btnSearch;

    ArrayList<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api-key", API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(requestInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(client.build())
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewYorkTimesService apiService = retrofit.create(NewYorkTimesService.class);
        Call<NewYorkTimesResponse> call = apiService.listArticles(query);
        call.enqueue(new Callback<NewYorkTimesResponse>() {
            @Override
            public void onResponse(Call<NewYorkTimesResponse> call, Response<NewYorkTimesResponse> response) {
                NewYorkTimesResponse nytResponse = response.body();
                Log.d("DEBUG", nytResponse.toString());
                articles.addAll(Article.fromDocList(nytResponse.getResponse().getDocs()));
            }

            @Override
            public void onFailure(Call<NewYorkTimesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
      }
}
