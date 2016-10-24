package com.codepath.nytsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.nytsearch.models.Article;
import com.codepath.nytsearch.models.NewYorkTimesResponse;
import com.codepath.nytsearch.models.NewYorkTimesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    RecyclerView rvResults;

    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;
    int curPage = 0;
    String curQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articles);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        rvResults.setAdapter(articleAdapter);
        rvResults.addItemDecoration(new SpacesItemDecoration(16));
/*        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(curQuery != null) {
                    fetchArticles(curQuery, page);
                }
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                curPage = 0;
                curQuery = query;
                int curSize = articleAdapter.getItemCount();
                articles.clear();
                articleAdapter.notifyItemRangeRemoved(0, curSize);
                fetchArticles(query, 0);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            Intent i = new Intent(this, FilterSettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchArticles(String query, int page) {
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
        Call<NewYorkTimesResponse> call = apiService.listArticles(query, page);
        call.enqueue(new Callback<NewYorkTimesResponse>() {
            @Override
            public void onResponse(Call<NewYorkTimesResponse> call, Response<NewYorkTimesResponse> response) {
                NewYorkTimesResponse nytResponse = response.body();
                Log.d("DEBUG", nytResponse.toString());
                List<Article> newArticles = Article.fromDocList(nytResponse.getResponse().getDocs());
                articles.addAll(newArticles);
                articleAdapter.notifyItemRangeInserted(0, newArticles.size());
            }

            @Override
            public void onFailure(Call<NewYorkTimesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
      }
}
