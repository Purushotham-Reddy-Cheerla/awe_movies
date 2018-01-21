package com.poras.passionate.awemovies;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poras.passionate.awemovies.adapter.GridRecyclerViewMovieAdapter;
import com.poras.passionate.awemovies.adapter.MovieRecyclerView;
import com.poras.passionate.awemovies.adapter.ScrollListener;
import com.poras.passionate.awemovies.data.model.Movie;
import com.poras.passionate.awemovies.utils.MovieUtil;
import com.poras.passionate.awemovies.utils.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, ScrollListener.LoadNextPage, GridRecyclerViewMovieAdapter.OnMovieClicked {


    private String mSort;
    private static final int LOADER_ID = 33;
    private int mPage = 1;

    private MovieRecyclerView recyclerView;
    private TextView mTextNoInternet;
    private GridRecyclerViewMovieAdapter mAdapter;

    private static final int NO_INTERNET = 0;
    private static final int NO_FAVORITES = 1;

    public MovieFragment() {
    }

    public static MovieFragment newInstance(String sort) {
        Bundle bundle = new Bundle();
        bundle.putString(MovieUtil.SORT_KEY, sort);
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSort = bundle.getString(MovieUtil.SORT_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }


    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.movieRecyclerView);
        mTextNoInternet = view.findViewById(R.id.tv_no_internet);
        mAdapter = new GridRecyclerViewMovieAdapter(getContext(), this);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new ScrollListener(manager, this));
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            ArrayList<Movie> mMovieList = savedInstanceState.getParcelableArrayList(MovieUtil.MOVIES);
            savedInstanceState.clear();
            mAdapter.SwapMovieList(mMovieList);
        } else {
            loadViews();
        }
    }

    private void loadViews() {
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            showMovies();
            if (!mSort.equals(MovieUtil.FAVORITE_KEY)) {
                getLoaderManager().restartLoader(LOADER_ID, getLoaderBundle(), this);
            } else {
                showFavorites();
            }
        } else {
            if (!mSort.equals(MovieUtil.FAVORITE_KEY)) {
                showNoInternet(NO_INTERNET);
            } else {
                showFavorites();
            }
        }
    }

    private void showFavorites() {
        mAdapter.ClearList();
        ArrayList<Movie> list = MovieUtil.getFavoriteMovieList(getContext());
        if (null != list && list.size() > 0) {
            showMovies();
            mAdapter.SwapMovieList(list);
        } else {
            showNoInternet(NO_FAVORITES);
        }
    }

    private void showNoInternet(int reason) {
        recyclerView.setVisibility(View.GONE);
        mTextNoInternet.setVisibility(View.VISIBLE);
        switch (reason) {
            case NO_INTERNET:
                mTextNoInternet.setText(getResources().getString(R.string.network_alert));
                break;
            case NO_FAVORITES:
                mTextNoInternet.setText(R.string.no_favorites);
                break;
        }
    }

    private void showMovies() {
        mTextNoInternet.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    private Bundle getLoaderBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(MovieUtil.SORT_KEY, mSort);
        return bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadViews();
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {

        switch (id) {
            case LOADER_ID:
                return new AsyncTaskLoader<ArrayList<Movie>>(getActivity()) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        forceLoad();
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        String pSort = args.getString(MovieUtil.SORT_KEY);
                        ArrayList<Movie> list = new ArrayList<>();
                        URL pUrl = null;
                        if (null == pSort || TextUtils.isEmpty(pSort)) {
                            return null;
                        }

                        if (pSort.equals(MovieUtil.POPULAR_KEY) || pSort.equals(MovieUtil.TOP_RATED_KEY)) {
                            pUrl = NetworkUtil.buildUrl(pSort, mPage);
                            try {
                                String pData = NetworkUtil.getMovieTrailerReviewFromUrl(pUrl);
                                if (pData != null) {
                                    list = MovieUtil.getMovieData(pData);
                                } else {
                                    return null;
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        return list;
                    }
                };
            default:
                throw new RuntimeException("Loader is not implemented" + id);
        }

    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if (data != null) {
            mAdapter.SwapMovieList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        mAdapter.SwapMovieList(null);
    }

    @Override
    public void loadNext() {
        if (!mSort.equals(MovieUtil.FAVORITE_KEY)) {
            mPage++;
            getLoaderManager().restartLoader(LOADER_ID, getLoaderBundle(), this);
        }
    }

    @Override
    public void movieClicked(Movie selectedMovie) {
        Bundle data = new Bundle();
        data.putParcelable("movie", selectedMovie);
        Intent newIntent = new Intent(getContext(), MovieDetailActivity.class);
        newIntent.putExtra("bundle", data);
        startActivity(newIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MovieUtil.MOVIES, mAdapter.getMovieList());
        super.onSaveInstanceState(outState);
    }

}
