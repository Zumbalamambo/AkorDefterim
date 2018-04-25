package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

@SuppressWarnings("ALL")
public class Frg_BosSayfa extends Fragment {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_frg_bossayfa, container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
	}

	@Override
	public void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
		AkorDefterimSys.SharePrefAyarlarınıUygula();
	}
}