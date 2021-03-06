package com.cnbcyln.app.akordefterim.SlideNav.transform;

import android.view.View;

import com.cnbcyln.app.akordefterim.SlideNav.util.SideNavUtils;

public class YTranslationTransformation implements RootTransformation {

    private static final float START_TRANSLATION = 0f;

    private final float endTranslation;

    public YTranslationTransformation(float endTranslation) {
        this.endTranslation = endTranslation;
    }

    @Override
    public void transform(float dragProgress, View rootView) {
        float translation = SideNavUtils.evaluate(dragProgress, START_TRANSLATION, endTranslation);
        rootView.setTranslationY(translation);
    }
}
