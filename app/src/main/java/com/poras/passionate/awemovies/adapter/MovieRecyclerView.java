package com.poras.passionate.awemovies.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

/**
 * Created by purus on 1/8/2018.
 */

public class MovieRecyclerView extends RecyclerView {

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof GridLayoutManager)
            super.setLayoutManager(layout);
        else
            throw new ClassCastException("Should use GridLayoutManager for RecyclerView");
    }

    public MovieRecyclerView(Context context) {
        super(context);
    }

    public MovieRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        if (getAdapter() != null && getLayoutManager() instanceof GridLayoutManager) {

            GridLayoutAnimationController.AnimationParameters animParams =
                    (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

            if (animParams == null) {
                animParams = new GridLayoutAnimationController.AnimationParameters();
                params.layoutAnimationParameters = animParams;
            }
            int totalColumns = ((GridLayoutManager) getLayoutManager()).getSpanCount();
            animParams.count = count;
            animParams.index = index;
            animParams.columnsCount = totalColumns;
            animParams.rowsCount = count / totalColumns;
            final int reverseIndex = count - 1 - index;
            animParams.column = totalColumns - 1 - (reverseIndex % totalColumns);
            animParams.row = animParams.rowsCount - 1 - reverseIndex / totalColumns;

        } else {
            super.attachLayoutAnimationParameters(child, params, index, count);
        }
    }
}
