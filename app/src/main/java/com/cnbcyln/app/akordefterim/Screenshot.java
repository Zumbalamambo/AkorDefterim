package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

@SuppressWarnings("ALL")
public class Screenshot extends AppCompatActivity implements OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	Typeface YaziFontu;

	ConstraintLayout constraintLayout;
	Button btnIptal, btnCek;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screenshot);

        activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		constraintLayout = findViewById(R.id.constraintLayout);

        btnIptal = findViewById(R.id.btnIptal);
        btnIptal.setTypeface(YaziFontu, Typeface.NORMAL);
        btnIptal.setOnClickListener(this);

        btnCek = findViewById(R.id.btnCek);
        btnCek.setTypeface(YaziFontu, Typeface.NORMAL);
        btnCek.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnIptal:
				finish();
				break;
            case R.id.btnCek:

                break;
		}
	}
}