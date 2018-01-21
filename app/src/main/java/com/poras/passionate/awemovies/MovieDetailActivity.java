package com.poras.passionate.awemovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.poras.passionate.awemovies.adapter.ReviewAdapter;
import com.poras.passionate.awemovies.adapter.TrailerAdapter;
import com.poras.passionate.awemovies.data.MovieContract;
import com.poras.passionate.awemovies.data.model.Movie;
import com.poras.passionate.awemovies.data.model.Review;
import com.poras.passionate.awemovies.data.model.Trailer;
import com.poras.passionate.awemovies.utils.MovieUtil;
import com.poras.passionate.awemovies.utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, TrailerAdapter.TrailerOnClickInterface {
    private static final int TRAILER_LOADER_ID = 11;
    private static final int REVIEW_LOADER_ID = 22;

    private Movie mMovie;
    private Boolean isFavorite;
    private ArrayList<Trailer> mTrailerList = new ArrayList<>();
    private ArrayList<Review> mReviewList = new ArrayList<>();

    private FloatingActionButton favoriteButton;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private ProgressBar mProTrailer;
    private ProgressBar mProReview;
    private TextView mTrailerText;
    private TextView mReviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            if (receivedIntent.hasExtra("bundle")) {
                Bundle bundle = receivedIntent.getBundleExtra("bundle");
                mMovie = bundle.getParcelable("movie");
            }
        }
        initRecyclerViews();
        mProTrailer = findViewById(R.id.pb_trailer);
        mProReview = findViewById(R.id.pb_review);
        mTrailerText = findViewById(R.id.tv_no_internet_video);
        mReviewText = findViewById(R.id.tv_no_internet_review);
        ((RatingBar) findViewById(R.id.rb_movie)).setRating((float) mMovie.userRating / 2);
        ((TextView) findViewById(R.id.tv_release_date)).setText(mMovie.releaseDate);
        ((TextView) findViewById(R.id.tv_overview)).setText(mMovie.review);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolBar)).setTitle(mMovie.originalTitle);
        Picasso.with(this).load(NetworkUtil.IMAGE_URL + mMovie.posterPath).into(((ImageView) findViewById(R.id.poster)));

        favoriteButton = findViewById(R.id.favoriteButton);
        if (mMovie.mFavorite) {
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_pink));
            isFavorite = true;
        } else {
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_grey));
            isFavorite = false;
        }
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    removeAsFavorite();
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_grey));
                    isFavorite = false;
                } else {
                    addAsFavorite();
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);
            getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);
        } else {
            mReviewList = savedInstanceState.getParcelableArrayList(MovieUtil.REVIEWS_KEY);
            mTrailerList = savedInstanceState.getParcelableArrayList(MovieUtil.VIDEOS_KEY);
            loadTrailers(mTrailerList);
            loadReviews(mReviewList);
        }
    }


    private void initRecyclerViews() {
        mTrailerRecyclerView = this.findViewById(R.id.rv_videos);
        mReviewsRecyclerView = this.findViewById(R.id.rv_reviews);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mReviewAdapter = new ReviewAdapter(this);
        RecyclerView.LayoutManager trailerManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager reviewManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(trailerManager);
        mReviewsRecyclerView.setLayoutManager(reviewManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);
    }

    private void addAsFavorite() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.movieId);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, mMovie.originalTitle);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, mMovie.posterPath);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mMovie.userRating);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.releaseDate);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, mMovie.review);

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.buildMovieUriWithId(mMovie.movieId), values);
        if (uri != null && !uri.getPath().contains("2801")) {
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_pink));
            isFavorite = true;
        } else {
            MovieUtil.showAlertDialog(this);
        }
    }

    private void removeAsFavorite() {
        getContentResolver().delete(MovieContract.MovieEntry.buildMovieUriWithId(mMovie.movieId),
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{Integer.toString(mMovie.movieId)});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:
                shareTrailerLink();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareTrailerLink() {
        String key = mTrailerList.get(0).mKey;
        String trailerLink = null;
        try {
            trailerLink = NetworkUtil.buildYoutubeLink(key).toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, trailerLink);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MovieUtil.MOVIES, mMovie);
        ArrayList<Review> rList = mReviewAdapter.getReviewList();
        if (mTrailerAdapter.getList() != null && mTrailerAdapter.getList().size() > 0)
            outState.putParcelableArrayList(MovieUtil.REVIEWS_KEY, rList);

        ArrayList<Trailer> tList = mTrailerAdapter.getList();
        if (tList != null && tList.size() > 0)
            outState.putParcelableArrayList(MovieUtil.VIDEOS_KEY, tList);
    }

    @Override
    public void onTrailerClicked(int position) {
        Trailer trailer = mTrailerList.get(position);
        String videoId = trailer.mKey;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        intent.putExtra("VIDEO_ID", videoId);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {

        switch (id) {
            case TRAILER_LOADER_ID:
                return new AsyncTaskLoader<String>(this) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        mProTrailer.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        String data = null;
                        URL url = NetworkUtil.getTrailerReviewUrl(mMovie.movieId, MovieUtil.VIDEOS_KEY);
                        try {
                            data = NetworkUtil.getMovieTrailerReviewFromUrl(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (data != null) {
                            try {
                                mTrailerList = MovieUtil.getTrailerData(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return data;
                    }
                };
            case REVIEW_LOADER_ID:
                return new AsyncTaskLoader<String>(this) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        mProReview.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        String data = null;
                        URL url = NetworkUtil.getTrailerReviewUrl(mMovie.movieId, MovieUtil.REVIEWS_KEY);
                        try {
                            data = NetworkUtil.getMovieTrailerReviewFromUrl(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (data != null) {
                            try {
                                mReviewList = MovieUtil.getReviewData(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        return data;
                    }
                };
            default:
                throw new RuntimeException("Loader is not implemented " + id);
        }


    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (null != data) {
            switch (loader.getId()) {
                case TRAILER_LOADER_ID:
                    mProTrailer.setVisibility(View.GONE);
                    loadTrailers(mTrailerList);
                    break;
                case REVIEW_LOADER_ID:
                    mProReview.setVisibility(View.GONE);
                    loadReviews(mReviewList);
                    break;
            }
        }
    }

    private void loadReviews(ArrayList<Review> mReviewList) {
        if (mReviewList != null && mReviewList.size() > 0) {
            mReviewsRecyclerView.setVisibility(View.VISIBLE);
            mReviewAdapter.addReviews(mReviewList);
        } else {
            mReviewsRecyclerView.setVisibility(View.GONE);
            mReviewText.setVisibility(View.VISIBLE);
            mReviewText.setText(R.string.no_reviews);
        }
    }

    private void loadTrailers(ArrayList<Trailer> mTrailerList) {
        if (mTrailerList != null && mTrailerList.size() > 0) {
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
            mTrailerAdapter.addTrailers(mTrailerList);
        } else {
            mTrailerRecyclerView.setVisibility(View.GONE);
            mTrailerText.setVisibility(View.VISIBLE);
            mTrailerText.setText(R.string.no_trailers);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        mTrailerAdapter.addTrailers(null);
        mReviewAdapter.addReviews(null);
    }

}
