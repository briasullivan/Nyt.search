package com.codepath.nytsearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

    public static final int REQUEST_CODE = 20;
    private static final String URL = "https://api.nytimes.com/";
    private static final String API_KEY = "14c035ebdb164b918ff7e28339fa8570";
    private static final String SORT_TYPE_NEWEST = "newest";
    private static final String SORT_TYPE_OLDEST = "oldest";
    private static final String NEWS_DESK_PREFIX = "news_desk:(";
    private static final String NEWS_DESK_SUFFIX = ")";

    RecyclerView rvResults;

    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;
    int curPage = 0;
    String curQuery = null;
    String sortType = SORT_TYPE_NEWEST;
    String beginDate;
    boolean filterByBeginDate = false;
    String newsDeskQuery;
    boolean filterByNewsDeskValues = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(curQuery != null) {
                    fetchArticles(curQuery, page);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Use a custom search icon for the SearchView in AppBar
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search);
        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                prepareQueryAndRequest(query);
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
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            Intent i = new Intent(this, FilterSettingsActivity.class);

            if (filterByBeginDate && beginDate != null) {
                i.putExtra("beginDate", beginDate);
            }
            if (filterByNewsDeskValues && newsDeskQuery != null) {
                i.putExtra("newsDeskValues", newsDeskQuery);
            }
            i.putExtra("sortType", (sortType == SORT_TYPE_NEWEST) ? 0 : 1);
            startActivityForResult(i, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String beginDateResult = data.getExtras().getString("beginDate");
            int sortResult = data.getExtras().getInt("sortType");
            String newsDeskResult = data.getExtras().getString("newsDeskValues");

            filterByBeginDate = (beginDateResult != null);
            beginDate = beginDateResult;
            sortType = (sortResult == 0) ? SORT_TYPE_NEWEST : SORT_TYPE_OLDEST;
            filterByNewsDeskValues = (newsDeskResult != null);
            newsDeskQuery = newsDeskResult;

            prepareQueryAndRequest(curQuery);
        }
    }

    private void prepareQueryAndRequest(String query) {
        if (query != null) {
            curPage = 0;
            curQuery = query;
            int curSize = articleAdapter.getItemCount();
            articles.clear();
            articleAdapter.notifyItemRangeRemoved(0, curSize);
            fetchArticles(query, 0);
        }
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
        Call<NewYorkTimesResponse> call;
        if (filterByBeginDate && filterByNewsDeskValues) {
            call = apiService.listArticlesWithFilterQueryAndBeginDate(
                    query,
                    NEWS_DESK_PREFIX + newsDeskQuery + NEWS_DESK_SUFFIX,
                    page,
                    sortType,
                    beginDate);
        } else if (filterByNewsDeskValues) {
            call = apiService.listArticlesWithFilterQuery(
                    query,
                    NEWS_DESK_PREFIX + newsDeskQuery + NEWS_DESK_SUFFIX,
                    page,
                    sortType);
        } else if (filterByBeginDate) {
            call = apiService.listArticlesWithBeginDate(query, page, sortType, beginDate);
        } else {
            call = apiService.listArticles(query, page, sortType);
        }

        if (isNetworkAvailable()) {
            call.enqueue(new Callback<NewYorkTimesResponse>() {
                @Override
                public void onResponse(Call<NewYorkTimesResponse> call, Response<NewYorkTimesResponse> response) {
                    NewYorkTimesResponse nytResponse = response.body();
                    if (nytResponse != null) {
                        List<Article> newArticles = Article.fromDocList(nytResponse.getResponse().getDocs());
                        int cursize = articles.size();
                        articles.addAll(newArticles);
                        articleAdapter.notifyItemRangeInserted(cursize, newArticles.size());
                    }
                }

                @Override
                public void onFailure(Call<NewYorkTimesResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }
      }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
