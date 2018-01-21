package com.poras.passionate.awemovies.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by purus on 12/10/2017.
 */

@SuppressWarnings("ALL")
public class ScrollListener extends RecyclerView.OnScrollListener {

    private final LoadNextPage mListener;
    private final GridLayoutManager mLayoutManager;


    public ScrollListener(GridLayoutManager manager, LoadNextPage listener) {
        mLayoutManager = manager;
        mListener = listener;
    }

    public interface LoadNextPage {
        void loadNext();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int totalCount = mLayoutManager.getItemCount();
        int itemPosition = mLayoutManager.findLastVisibleItemPosition();
        if (itemPosition > totalCount - 2) {
            mListener.loadNext();
        }

    }
}
