package com.nasserkhosravi.appcomponent.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nasserkhosravi.appcomponent.R;

public class NoConnectionView {

    private final ViewGroup view;
    private ViewGroup container;

    public NoConnectionView(ViewGroup container) {
        view = (ViewGroup) LayoutInflater.from(container.getContext()).inflate(R.layout.layout_no_network, container, false);
        this.container = container;
    }

    public void show() {
        if (view.getParent() == null) {
            container.addView(view);
        }
    }

    public void hide() {
        if (view.getParent() != null) {
            container.removeView(view);
        }
    }

    public NoConnectionView setOnTryAgainClickListener(View.OnClickListener listener) {
        view.findViewById(R.id.btnTryAgain).setOnClickListener(listener);
        return this;
    }

}
