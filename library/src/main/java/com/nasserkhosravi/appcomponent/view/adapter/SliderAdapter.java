package com.nasserkhosravi.appcomponent.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.nasserkhosravi.appcomponent.R;
import com.nasserkhosravi.appcomponent.Res;
import com.nasserkhosravi.appcomponent.ResType;
import com.nasserkhosravi.appcomponent.data.ImageResource;
import com.nasserkhosravi.appcomponent.view.widget.TouchImageView;

import java.util.List;

/**
 * An adapter for creating Image slider
 */
public class SliderAdapter extends PagerAdapter {

    public @ResType
    int resourceType = Res.WEB;

    private LayoutInflater mLayoutInflater;
    private List<ImageResource> uriList;
    private Context context;
    private boolean isEnableZooming = false;
    private SliderItemClickListener itemClickListener;
    private int width = -1;
    private int height = -1;

    public SliderAdapter() {
    }

    public void setItems(List<ImageResource> uriList) {
        this.uriList = uriList;
    }

    @Override
    public int getCount() {
        return uriList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        if (context == null) {
            this.context = container.getContext();
        }
        if (mLayoutInflater == null) {
            mLayoutInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        assert mLayoutInflater != null;
        final ImageView imageView = getImageImpl();
        if (width > -1) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = width;
        }
        if (height > -1) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = height;
        }
        uriList.get(position).loadInto(imageView);
        if (itemClickListener != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(imageView, position);
                }
            });
        }
        container.addView(imageView);
        return imageView;
    }

    /**
     * Unit of width and height is pixel
     */
    public void setCustomSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isEnableZooming() {
        return isEnableZooming;
    }

    public void setEnableZooming(boolean enableZooming) {
        isEnableZooming = enableZooming;
    }

    public void setItemClickListener(SliderItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private ImageView getImageImpl() {
        ImageView img;
        if (!isEnableZooming) {
            img = new ImageView(context);
        } else {
            img = new TouchImageView(context);
        }
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        img.setId(R.id.imgSlider);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return img;
    }

    public interface SliderItemClickListener {
        void onItemClickListener(View view, int position);
    }

}