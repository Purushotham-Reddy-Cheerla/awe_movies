package com.poras.passionate.awemovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poras.passionate.awemovies.R;
import com.poras.passionate.awemovies.data.model.Review;

import java.util.ArrayList;

/**
 * Created by purus on 1/10/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private ArrayList<Review> mReviewList = new ArrayList<>();
    private final Context mContext;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item_layout, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.pReview.setText(mReviewList.get(position).mComment);
        holder.pAuthor.setText(String.format("- %s", mReviewList.get(position).mAuthor));
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        final TextView pReview;
        final TextView pAuthor;

        public ReviewHolder(View itemView) {
            super(itemView);
            pReview = itemView.findViewById(R.id.tv_review);
            pAuthor = itemView.findViewById(R.id.tv_author);
        }
    }

    public void addReviews(ArrayList<Review> reviews) {
        mReviewList = reviews;
        notifyDataSetChanged();
    }

    public ArrayList<Review> getReviewList() {
        return mReviewList;
    }
}
