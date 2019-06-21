package org.fitzeng.zzchat.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import org.fitzeng.zzchat.R;

public class SlideLayout extends FrameLayout{

    private Context context;

    public SlideLayout(@NonNull Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
    }

    private void initViews() {
        this.addView(LayoutInflater.from(context).inflate(R.layout.layout_slide, null));
    }
}
