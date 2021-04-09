package com.oakraw.library;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface OnTransformersScrollListener {
    void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState);

    void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy);
}