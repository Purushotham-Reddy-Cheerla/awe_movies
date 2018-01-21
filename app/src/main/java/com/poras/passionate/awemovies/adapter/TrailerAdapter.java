package com.poras.passionate.awemovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poras.passionate.awemovies.R;
import com.poras.passionate.awemovies.data.model.Trailer;
import com.poras.passionate.awemovies.utils.NetworkUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by purus on 1/10/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private ArrayList<Trailer> mList = new ArrayList<>();
    private final Context context;
    private final TrailerOnClickInterface clickHandler;

    public TrailerAdapter(Context context, TrailerOnClickInterface handler) {
        this.context = context;
        this.clickHandler = handler;
    }

    public interface TrailerOnClickInterface {
        void onTrailerClicked(int position);
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item_layout, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerHolder holder, int position) {
        String thumbPoster = NetworkUtil.getTrailerThumbPath(mList.get(position).mKey);
        final Trailer trailer = mList.get(position);
        Target picassoTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.trailerName.setText(trailer.mName);
                holder.trailerThumb.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
                ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(bitmap.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.trailerName.setLayoutParams(layoutParams);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(context).load(thumbPoster).into(picassoTarget);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView trailerThumb;
        private final TextView trailerName;

        public TrailerHolder(View itemView) {
            super(itemView);
            trailerThumb = itemView.findViewById(R.id.iv_trailer_thumb);
            trailerName = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onTrailerClicked(getAdapterPosition());
        }
    }

    public void addTrailers(ArrayList<Trailer> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public ArrayList<Trailer> getList() {
        return mList;
    }
}
