package com.cnbcyln.app.akordefterim.util.SlideNav.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.cnbcyln.app.akordefterim.util.SlideNav.SlidingRootNavLayout;

public class HiddenMenuClickConsumer extends View {

    private SlidingRootNavLayout menuHost;

    public HiddenMenuClickConsumer(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return menuHost.isMenuHidden();
    }

    public void setMenuHost(SlidingRootNavLayout layout) {
        this.menuHost = layout;
    }
}
