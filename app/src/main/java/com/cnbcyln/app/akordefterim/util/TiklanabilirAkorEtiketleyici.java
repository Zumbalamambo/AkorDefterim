package com.cnbcyln.app.akordefterim.util;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

@SuppressWarnings("deprecation")
class TiklanabilirAkorEtiketleyici extends ClickableSpan {
    private Activity activity;
    private AkorDefterimSys AkorDefterimSys;
    private String Icerik;
    private int startIndex, endIndex;
    private int YaziRengi;

    TiklanabilirAkorEtiketleyici(Activity activity, String Icerik, int YaziRengi, int startIndex, int endIndex) {
        this.activity = activity;
        this.Icerik = Icerik;
        this.YaziRengi = YaziRengi;
        this.startIndex = startIndex;
        this.endIndex = endIndex;

        AkorDefterimSys = new AkorDefterimSys(activity);
    }

    @Override
    public void onClick(View widget) {
        AkorDefterimSys.AkorCetveli(activity, Icerik.substring(startIndex, endIndex));
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        //ds.setTypeface(Typeface.create(ds.getTypeface(), Typeface.BOLD));
        ds.setColor(activity.getResources().getColor(YaziRengi));
    }
}

