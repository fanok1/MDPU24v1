package com.fanok.mdpu24v1;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

public class NonFocusingScrollViewHoresontal extends HorizontalScrollView {

    public NonFocusingScrollViewHoresontal(Context context) {
        super(context);
    }

    public NonFocusingScrollViewHoresontal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonFocusingScrollViewHoresontal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NonFocusingScrollViewHoresontal(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
    }

    @Override
    public ArrayList<View> getFocusables(int direction) {
        return new ArrayList<>();
    }
}
