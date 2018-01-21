package com.poras.passionate.awemovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.poras.passionate.awemovies.R;
import com.poras.passionate.awemovies.data.model.Movie;
import com.poras.passionate.awemovies.utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by purus on 1/8/2018.
 */

public class GridRecyclerViewMovieAdapter extends RecyclerView.Adapter<GridRecyclerViewMovieAdapter.MovieHolder> {

    private final Context mContext;

    private final ArrayList<Movie> mMovieList = new ArrayList<>();

    private final OnMovieClicked callBack;


    public interface OnMovieClicked {
        void movieClicked(Movie selectedMovie);
    }

    public GridRecyclerViewMovieAdapter(Context context, OnMovieClicked handler) {
        mContext = context;
        callBack = handler;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Picasso.with(mContext).load(NetworkUtil.IMAGE_URL + mMovieList.get(position).posterPath).into(holder.sPoster);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView sPoster;

        public MovieHolder(View itemView) {
            super(itemView);
            sPoster = itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callBack.movieClicked(mMovieList.get(getAdapterPosition()));
        }
    }

    public void SwapMovieList(ArrayList<Movie> movieList) {

        if (movieList != null && !(mMovieList == movieList)) {
            mMovieList.addAll(movieList);
            notifyDataSetChanged();
        }
    }

    public void ClearList() {
        mMovieList.clear();
    }

    public ArrayList<Movie> getMovieList() {
        return mMovieList;
    }

}
