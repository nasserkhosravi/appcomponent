package com.nasserkhosravi.appcomponent.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.nasserkhosravi.appcomponent.R;

/**
 * Support to item click listener rows in RecycleView
 * This class is not intended to detect click on children of rows
 * How to use it: ItemClickSupportRecycleView.addTo(recycleView).setOnItemClickListener(this);
 */
public class ItemClickSupportRecycleView {

    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onRecycleViewItemClick(mRecyclerView, holder.getAdapterPosition(), v);

            }
        }
    };

    private ItemClickSupportRecycleView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.itemClickSupport, this);
        RecyclerView.OnChildAttachStateChangeListener mAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (mOnItemClickListener != null) {
                    view.setOnClickListener(mOnClickListener);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        };
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickSupportRecycleView addTo(RecyclerView view) {
        ItemClickSupportRecycleView support = (ItemClickSupportRecycleView) view.getTag(R.id.itemClickSupport);
        if (support == null) {
            support = new ItemClickSupportRecycleView(view);
        }
        return support;
    }

    public ItemClickSupportRecycleView setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public interface OnItemClickListener {

        void onRecycleViewItemClick(RecyclerView recyclerView, int position, View v);
    }

}