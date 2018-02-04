package com.cnbcyln.app.akordefterim.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfIstekler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;

@SuppressLint("SimpleDateFormat | Recycle")
@SuppressWarnings("ALL")
public class Veritabani extends SQLiteOpenHelper {

	private static final String VeritabaniAdi = "AkorDefterim";
	private static int Surum = 2;
	Context context;

	public Veritabani(Context context) {
		super(context, VeritabaniAdi, null, Surum);
		this.context = context;
		//this.Surum = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE tblListeler (id INTEGER PRIMARY KEY AUTOINCREMENT, ListeAdi TEXT);");
		db.execSQL("CREATE TABLE tblKategoriler (id INTEGER PRIMARY KEY AUTOINCREMENT, KategoriAdi TEXT);");
		db.execSQL("CREATE TABLE tblTarzlar (id INTEGER PRIMARY KEY AUTOINCREMENT, TarzAdi TEXT);");
		db.execSQL("CREATE TABLE tblSarkilar (id INTEGER PRIMARY KEY AUTOINCREMENT, ListeID INTEGER, KategoriID INTEGER, TarzID INTEGER, SanatciAdi TEXT, SarkiAdi TEXT, Icerik TEXT, EklenmeTarihi DATETIME, DuzenlenmeTarihi DATETIME DEFAULT CURRENT_TIMESTAMP);");
		db.execSQL("CREATE TABLE tblIstekler (id INTEGER PRIMARY KEY AUTOINCREMENT, SarkiID INTEGER, ClientID TEXT, ClientAdSoyad TEXT, ClientIP TEXT, ClientNot TEXT, IstekTarihi DATETIME);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE IF EXISTS tblListeler;");
		//db.execSQL("DROP TABLE IF EXISTS tblKategoriler;");
		//db.execSQL("DROP TABLE IF EXISTS tblTarzlar;");
		//db.execSQL("DROP TABLE IF EXISTS tblSarkilar;");
		//db.execSQL("DROP TABLE IF EXISTS tblIstekler;");
		//onCreate(db);

		//db.execSQL("CREATE TABLE tblIstekler (id INTEGER PRIMARY KEY AUTOINCREMENT, SarkiID INTEGER, ClientID TEXT, ClientAdSoyad TEXT, ClientIP TEXT, ClientNot TEXT, IstekTarihi DATETIME);");
	}

	public void VeritabaniSifirla() {
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("DROP TABLE IF EXISTS tblListeler;");
		db.execSQL("DROP TABLE IF EXISTS tblKategoriler;");
		db.execSQL("DROP TABLE IF EXISTS tblTarzlar;");
		db.execSQL("DROP TABLE IF EXISTS tblSarkilar;");
		db.execSQL("DROP TABLE IF EXISTS tblIstekler;");
		onCreate(db);
	}

	// ##### LİSTELER #####
	public boolean ListeEkle(String ListeAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("ListeAdi", ListeAdi);
			db.insert("tblListeler", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean RepGeriYukleme_ListeEkle(int id, String ListeAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("id", id);
			values.put("ListeAdi", ListeAdi);
			db.insert("tblListeler", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean ListeSil(int ListeID) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.delete("tblListeler", "id = ?", new String[]{String.valueOf(ListeID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean ListeDuzenle(int ListeID, String YeniListeAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ListeAdi", YeniListeAdi);

		try {
			db.update("tblListeler", values, "id = ?", new String[]{String.valueOf(ListeID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public List<SnfListeler> SnfListeGetir(String OturumTipi, Boolean TumuSecenegi) {
		List<SnfListeler> SnfListeler = new ArrayList<>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblListeler";
		SnfListeler Listeler;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			if(OturumTipi.equals("Cevrimici")) {
				Listeler = new SnfListeler();
				Listeler.setId(0);
				Listeler.setListeAdi(context.getString(R.string.uygulama_adi));
				SnfListeler.add(Listeler);

				while (cursor.moveToNext()) {
					Listeler = new SnfListeler();
					Listeler.setId(cursor.getInt(0));
					Listeler.setListeAdi(cursor.getString(1));
					SnfListeler.add(Listeler);
				}
			} else {
				if (TumuSecenegi) {
					Listeler = new SnfListeler();
					Listeler.setId(0);
					Listeler.setListeAdi(context.getString(R.string.tumu));
					SnfListeler.add(Listeler);
				}

				if(cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						Listeler = new SnfListeler();
						Listeler.setId(cursor.getInt(0));
						Listeler.setListeAdi(cursor.getString(1));
						SnfListeler.add(Listeler);
					}
				} else {
					Listeler = new SnfListeler();
					Listeler.setId(0);
					Listeler.setListeAdi("");
					SnfListeler.add(Listeler);
				}
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return SnfListeler;
		}
	}

	public List<SnfListeler> RepYedekleme_SnfListeGetir() {
		List<SnfListeler> SnfListeler = new ArrayList<>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblListeler Order By id ASC";
		SnfListeler Listeler;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				Listeler = new SnfListeler();
				Listeler.setId(cursor.getInt(0));
				Listeler.setListeAdi(cursor.getString(1));
				SnfListeler.add(Listeler);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return SnfListeler;
		}
	}

	public String lstJSONSTR_ListeGetir(int BaglangicNo, int Limit) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblListeler Order By id ASC Limit " + BaglangicNo + ", " + Limit;
		StringBuilder ListeID = new StringBuilder();
		StringBuilder ListeAdi = new StringBuilder();

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ListeID.append(cursor.getInt(0) + ",");
				ListeAdi.append(cursor.getString(1) + ",");
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return "{\"ListeID\":\"" + ListeID + "\", \"ListeAdi\":\"" + ListeAdi + "\"}";
		}
	}

	public String ListeAdiGetir(int ListeID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblListeler Where id = " + ListeID;
		String ListeAdi = "";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ListeAdi = cursor.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ListeAdi;
		}
	}

	public Boolean ListeyeAitSarkiVarmiKontrol(int ListeID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblSarkilar Where ListeID = " + ListeID;
		Boolean VarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				VarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return VarMi;
		}
	}

	public Boolean ListeVarmiKontrol(String ListeAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblListeler Where ListeAdi = '" + ListeAdi + "'";
		Boolean VarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				VarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return VarMi;
		}
	}

	public int ListeSayisiGetir() {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblListeler";
		int ToplamListeSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamListeSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamListeSayisi;
		}
	}

	public int ListeyeAitSarkiSayisiGetir(int ListeID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblSarkilar WHERE ListeID = " + ListeID;
		int ToplamSarkiSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamSarkiSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamSarkiSayisi;
		}
	}

	// ##### KATEGORİLER #####
	public boolean KategoriEkle(String KategoriAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("KategoriAdi", KategoriAdi);
			db.insert("tblKategoriler", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean RepGeriYukleme_KategoriEkle(int id, String KategoriAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("id", id);
			values.put("KategoriAdi", KategoriAdi);
			db.insert("tblKategoriler", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean KategoriSil(int KategoriID) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.delete("tblKategoriler", "id = ?", new String[]{String.valueOf(KategoriID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean KategoriDuzenle(int KategoriID, String YeniKategoriAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("KategoriAdi", YeniKategoriAdi);

		try {
			db.update("tblKategoriler", values, "id = ?", new String[]{String.valueOf(KategoriID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public List<SnfKategoriler> SnfKategoriGetir(boolean TumuSecenegi) {
		List<SnfKategoriler> SnfKategoriler = new ArrayList<SnfKategoriler>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblKategoriler";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			if(cursor.getCount() > 0) {
				if (TumuSecenegi) {
					SnfKategoriler Kategori = new SnfKategoriler();
					Kategori.setId(0);
					Kategori.setKategoriAdi(context.getString(R.string.tumu));
					SnfKategoriler.add(Kategori);
				}

				while (cursor.moveToNext()) {
					SnfKategoriler Kategori = new SnfKategoriler();
					Kategori.setId(cursor.getInt(0));
					Kategori.setKategoriAdi(cursor.getString(1));
					SnfKategoriler.add(Kategori);
				}
			} else {
				SnfKategoriler Kategori = new SnfKategoriler();
				Kategori.setId(0);
				Kategori.setKategoriAdi("");
				SnfKategoriler.add(Kategori);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return SnfKategoriler;
		}
	}

	public List<SnfKategoriler> RepYedekleme_SnfKategoriGetir() {
		List<SnfKategoriler> SnfKategoriler = new ArrayList<SnfKategoriler>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblKategoriler Order By id ASC";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				SnfKategoriler Kategori = new SnfKategoriler();
				Kategori.setId(cursor.getInt(0));
				Kategori.setKategoriAdi(cursor.getString(1));
				SnfKategoriler.add(Kategori);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return SnfKategoriler;
		}
	}

	public String lstJSONSTR_KategoriGetir(int BaglangicNo, int Limit) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblKategoriler Order By id ASC Limit " + BaglangicNo + ", " + Limit;
		StringBuilder KategoriID = new StringBuilder();
		StringBuilder KategoriAdi = new StringBuilder();

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				KategoriID.append(cursor.getInt(0) + ",");
				KategoriAdi.append(cursor.getString(1) + ",");
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return "{\"KategoriID\":\"" + KategoriID + "\", \"KategoriAdi\":\"" + KategoriAdi + "\"}";
		}
	}

	public String KategoriAdiGetir(int KategoriID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblKategoriler Where id = " + KategoriID;
		String KategoriAdi = "";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				KategoriAdi = cursor.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return KategoriAdi;
		}
	}

	public Boolean KategoriyeAitSarkiVarmiKontrol(int KategoriID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblSarkilar Where KategoriID = " + KategoriID;
		Boolean VarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				VarMi = true;
			}

		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return VarMi;
		}
	}

	public Boolean KategoriVarmiKontrol(String KategoriAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblKategoriler Where KategoriAdi = '" + KategoriAdi + "'";
		Boolean VarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				VarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return VarMi;
		}
	}

	public int KategoriSayisiGetir() {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblKategoriler";
		int ToplamKategoriSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamKategoriSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamKategoriSayisi;
		}
	}

	public int KategoriyeAitSarkiSayisiGetir(int KategoriID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblSarkilar WHERE KategoriID = " + KategoriID;
		int ToplamSarkiSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamSarkiSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamSarkiSayisi;
		}
	}

	// ##### TARZLAR #####
	public boolean TarzEkle(String TarzAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("TarzAdi", TarzAdi);
			db.insert("tblTarzlar", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean RepGeriYukleme_TarzEkle(int id, String TarzAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("id", id);
			values.put("TarzAdi", TarzAdi);
			db.insert("tblTarzlar", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean TarzSil(int TarzID) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.delete("tblTarzlar", "id = ?", new String[]{String.valueOf(TarzID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean TarzDuzenle(int TarzID, String YeniTarzAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("TarzAdi", YeniTarzAdi);

		try {
			db.update("tblTarzlar", values, "id = ?", new String[]{String.valueOf(TarzID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public List<SnfTarzlar> SnfTarzGetir(boolean TumuSecenegi) {
		List<SnfTarzlar> lstTarzlar = new ArrayList<SnfTarzlar>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblTarzlar";

		Cursor cursor = db.rawQuery(Sorgu, null);

		if(cursor.getCount() > 0) {
			if (TumuSecenegi) {
				SnfTarzlar Tarz = new SnfTarzlar();
				Tarz.setId(0);
				Tarz.setTarzAdi(context.getString(R.string.tumu));
				lstTarzlar.add(Tarz);
			}

			while (cursor.moveToNext()) {
				SnfTarzlar Tarz = new SnfTarzlar();
				Tarz.setId(cursor.getInt(0));
				Tarz.setTarzAdi(cursor.getString(1));
				lstTarzlar.add(Tarz);
			}
		} else {
			SnfTarzlar Tarz = new SnfTarzlar();
			Tarz.setId(0);
			Tarz.setTarzAdi("");
			lstTarzlar.add(Tarz);
		}

		return lstTarzlar;
	}

	public List<SnfTarzlar> RepYedekleme_SnfTarzGetir() {
		List<SnfTarzlar> lstTarzlar = new ArrayList<SnfTarzlar>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblTarzlar Order By id ASC";

		Cursor cursor = db.rawQuery(Sorgu, null);

		while (cursor.moveToNext()) {
			SnfTarzlar Tarz = new SnfTarzlar();
			Tarz.setId(cursor.getInt(0));
			Tarz.setTarzAdi(cursor.getString(1));
			lstTarzlar.add(Tarz);
		}

		return lstTarzlar;
	}

	public String lstJSONSTR_TarzGetir(int BaglangicNo, int Limit) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblTarzlar Order By id ASC Limit " + BaglangicNo + ", " + Limit;
		StringBuilder TarzID = new StringBuilder();
		StringBuilder TarzAdi = new StringBuilder();

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				TarzID.append(cursor.getInt(0) + ",");
				TarzAdi.append(cursor.getString(1) + ",");
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return "{\"TarzID\":\"" + TarzID + "\", \"TarzAdi\":\"" + TarzAdi + "\"}";
		}
	}

	public String TarzAdiGetir(int TarzID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblTarzlar Where id = " + TarzID;
		String TarzAdi = "";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				TarzAdi = cursor.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return TarzAdi;
		}
	}

	public Boolean TarzaAitSarkiVarmiKontrol(int TarzID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblSarkilar Where TarzID = " + TarzID;
		Boolean VarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				VarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return VarMi;
		}
	}

	public Boolean TarzVarmiKontrol(String TarzAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblTarzlar Where TarzAdi = '" + TarzAdi + "'";
		Boolean VarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				VarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return VarMi;
		}
	}

	public int TarzSayisiGetir() {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblTarzlar";
		int ToplamTarzSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamTarzSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamTarzSayisi;
		}
	}

	public int TarzaAitSarkiSayisiGetir(int TarzID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblSarkilar WHERE TarzID = " + TarzID;
		int ToplamSarkiSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamSarkiSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamSarkiSayisi;
		}
	}

	// ##### ŞARKILAR #####

	public boolean SarkiEkle(int ListeID, int KategoriID, int TarzID, String SarkiAdi, String SanatciAdi, String Icerik) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("ListeID", ListeID);
			values.put("KategoriID", KategoriID);
			values.put("TarzID", TarzID);
			values.put("SanatciAdi", SanatciAdi);
			values.put("SarkiAdi", SarkiAdi);
			values.put("Icerik", Icerik);
			values.put("EklenmeTarihi", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			values.put("DuzenlenmeTarihi", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

			db.insert("tblSarkilar", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean SarkiEkle(int id, int ListeID, int KategoriID, int TarzID, String SarkiAdi, String SanatciAdi, String Icerik, String EklenmeTarihi, String DuzenlenmeTarihi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("id", id);
			values.put("ListeID", ListeID);
			values.put("KategoriID", KategoriID);
			values.put("TarzID", TarzID);
			values.put("SanatciAdi", SanatciAdi);
			values.put("SarkiAdi", SarkiAdi);
			values.put("Icerik", Icerik);
			values.put("EklenmeTarihi", EklenmeTarihi);
			values.put("DuzenlenmeTarihi", DuzenlenmeTarihi);

			db.insert("tblSarkilar", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean SarkiSil(int SarkiID) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.delete("tblSarkilar", "id = ?", new String[]{String.valueOf(SarkiID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean SarkiDuzenle(int id, int ListeID, int KategoriID, int TarzID, String SanatciAdi, String SarkiAdi, String Icerik) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("ListeID", ListeID);
		values.put("KategoriID", KategoriID);
		values.put("TarzID", TarzID);
		values.put("SanatciAdi", SanatciAdi);
		values.put("SarkiAdi", SarkiAdi);
		values.put("Icerik", Icerik);
		values.put("DuzenlenmeTarihi", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		try {
			db.update("tblSarkilar", values, "id = ?", new String[]{String.valueOf(id)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public List<SnfSarkilar> SnfSarkiGetir(int ListeID, int KategoriID, int TarzID, int ListelemeTipi) {
		List<SnfSarkilar> snfSarkilarListesi = new ArrayList<>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = null;

		switch (ListelemeTipi) {
			case 0:
				Sorgu = "SELECT * FROM tblSarkilar WHERE" +
						(ListeID != 0 ? " ListeID = " + ListeID:"") +
						(KategoriID != 0 ? " AND KategoriID = " + KategoriID:"") +
						(TarzID != 0 ? " AND TarzID = " + TarzID:"") + " Order By SanatciAdi, SarkiAdi ASC";
				break;
			case 1:
				Sorgu = "SELECT * FROM tblSarkilar WHERE" +
						(ListeID != 0 ? " ListeID = " + ListeID:"") +
						(KategoriID != 0 ? " AND KategoriID = " + KategoriID:"") +
						(TarzID != 0 ? " AND TarzID = " + TarzID:"") + " Order By SarkiAdi ASC";
				break;
			case 2:
				Sorgu = "SELECT * FROM tblSarkilar WHERE" +
						(ListeID != 0 ? " ListeID = " + ListeID:"") +
						(KategoriID != 0 ? " AND KategoriID = " + KategoriID:"") +
						(TarzID != 0 ? " AND TarzID = " + TarzID:"") + " Order By id ASC";
				break;
		}

		if(Sorgu.substring(Sorgu.indexOf("WHERE") + 6, Sorgu.length()).startsWith("Order"))
			Sorgu = Sorgu.substring(0, Sorgu.indexOf("WHERE")) + Sorgu.substring(Sorgu.indexOf("WHERE") + 6, Sorgu.length());

		if(Sorgu.substring(Sorgu.indexOf("WHERE") + 6, Sorgu.length()).startsWith("AND"))
			Sorgu = Sorgu.substring(0, Sorgu.indexOf("WHERE") + 5) + Sorgu.substring(Sorgu.indexOf("WHERE") + 9, Sorgu.length());

		Cursor cursor = db.rawQuery(Sorgu, null);

		while (cursor.moveToNext()) {
			SnfSarkilar Sarkilar = new SnfSarkilar();
			Sarkilar.setId(cursor.getInt(0));
			Sarkilar.setListeID(cursor.getInt(1));
			Sarkilar.setKategoriID(cursor.getInt(2));
			Sarkilar.setTarzID(cursor.getInt(3));
			Sarkilar.setSanatciAdi(cursor.getString(4));
			Sarkilar.setSarkiAdi(cursor.getString(5));
			Sarkilar.setEklenmeTarihi(cursor.getString(8));
			Sarkilar.setDuzenlenmeTarihi(cursor.getString(9));
			snfSarkilarListesi.add(Sarkilar);
		}

		return snfSarkilarListesi;
	}

	public SnfSarkilar SnfSarkiGetir(int SarkiID) {
		SnfSarkilar snfSarki = new SnfSarkilar();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = null;

		Sorgu = "SELECT * FROM tblSarkilar WHERE id = " + SarkiID;

		Cursor cursor = db.rawQuery(Sorgu, null);

		while (cursor.moveToNext()) {
			snfSarki.setId(cursor.getInt(0));
			snfSarki.setListeID(cursor.getInt(1));
			snfSarki.setKategoriID(cursor.getInt(2));
			snfSarki.setTarzID(cursor.getInt(3));
			snfSarki.setSanatciAdi(cursor.getString(4));
			snfSarki.setSarkiAdi(cursor.getString(5));
			snfSarki.setEklenmeTarihi(cursor.getString(8));
			snfSarki.setDuzenlenmeTarihi(cursor.getString(9));
		}

		return snfSarki;
	}

	public List<SnfSarkilar> RepYedekleme_SnfSarkiGetir() {
		List<SnfSarkilar> snfSarkilarListesi = new ArrayList<>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = null;

		Sorgu = "SELECT * FROM tblSarkilar Order By id ASC";

		Cursor cursor = db.rawQuery(Sorgu, null);

		while (cursor.moveToNext()) {
			SnfSarkilar Sarkilar = new SnfSarkilar();
			Sarkilar.setId(cursor.getInt(0));
			Sarkilar.setListeID(cursor.getInt(1));
			Sarkilar.setKategoriID(cursor.getInt(2));
			Sarkilar.setTarzID(cursor.getInt(3));
			Sarkilar.setSanatciAdi(cursor.getString(4));
			Sarkilar.setSarkiAdi(cursor.getString(5));
			Sarkilar.setIcerik(cursor.getString(6));
			Sarkilar.setEklenmeTarihi(cursor.getString(7));
			Sarkilar.setDuzenlenmeTarihi(cursor.getString(8));
			snfSarkilarListesi.add(Sarkilar);
		}

		return snfSarkilarListesi;
	}

	public String lstJSONSTR_SarkiGetir(int BaglangicNo, int Limit, String SiralamaTipi) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT tblSarkilar.id, tblListeler.ListeAdi, tblKategoriler.KategoriAdi, tblTarzlar.TarzAdi, tblSarkilar.SanatciAdi, tblSarkilar.SarkiAdi, tblSarkilar.DuzenlenmeTarihi FROM tblSarkilar INNER JOIN tblListeler ON tblListeler.id = tblSarkilar.ListeID INNER JOIN tblKategoriler ON tblKategoriler.id = tblSarkilar.KategoriID INNER JOIN tblTarzlar ON tblTarzlar.id = tblSarkilar.TarzID Order By " + SiralamaTipi + " Limit " + BaglangicNo + ", " + Limit;

		StringBuilder ID = new StringBuilder();
		StringBuilder ListeAdi = new StringBuilder();
		StringBuilder KategoriAdi = new StringBuilder();
		StringBuilder TarzAdi = new StringBuilder();
		StringBuilder SarkiAdi = new StringBuilder();
		StringBuilder SanatciAdi = new StringBuilder();
		StringBuilder DuzenlenmeTarihi = new StringBuilder();

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ID.append(cursor.getInt(0) + ",");
				ListeAdi.append(cursor.getString(1) + ",");
				KategoriAdi.append(cursor.getString(2) + ",");
				TarzAdi.append(cursor.getString(3) + ",");
				SanatciAdi.append(cursor.getString(4) + ",");
				SarkiAdi.append(cursor.getString(5) + ",");
				DuzenlenmeTarihi.append(cursor.getString(6) + ",");
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return "{\"ID\":\"" + ID
					+ "\", \"ListeAdi\":\"" + ListeAdi
					+ "\", \"KategoriAdi\":\"" + KategoriAdi
					+ "\", \"TarzAdi\":\"" + TarzAdi
					+ "\", \"SanatciAdi\":\"" + SanatciAdi
					+ "\", \"SarkiAdi\":\"" + SarkiAdi
					+ "\", \"DuzenlenmeTarihi\":\"" + DuzenlenmeTarihi + "\"}";
		}
	}

	public String lstJSONSTR_FullSarkiGetir() {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT SanatciAdi, SarkiAdi FROM tblSarkilar";

		StringBuilder SarkiAdi = new StringBuilder();
		StringBuilder SanatciAdi = new StringBuilder();

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				if(SanatciAdi.indexOf(cursor.getString(0)) == -1) SanatciAdi.append(cursor.getString(0) + ",");
				if(SarkiAdi.indexOf(cursor.getString(1)) == -1) SarkiAdi.append(cursor.getString(1) + ",");
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return "{\"SanatciAdi\":\"" + SanatciAdi
					+ "\", \"SarkiAdi\":\"" + SarkiAdi + "\"}";
		}
	}

	public String SarkiGetir(int SarkiID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT id, ListeID, KategoriID, TarzID, SanatciAdi, SarkiAdi, Icerik FROM tblSarkilar Where id = " + SarkiID;
		int ID = 0;
		int ListeID = 0;
		int KategoriID = 0;
		int TarzID = 0;
		String SanatciAdi = "";
		String SarkiAdi = "";
		String Icerik = "";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ID = cursor.getInt(0);
				ListeID = cursor.getInt(1);
				KategoriID = cursor.getInt(2);
				TarzID = cursor.getInt(3);
				SanatciAdi = cursor.getString(4);
				SarkiAdi = cursor.getString(5);
				Icerik = cursor.getString(6).replace("\"","\\\"");
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return "{\"ID\":\"" + ID
					+ "\", \"ListeID\":\"" + ListeID
					+ "\", \"KategoriID\":\"" + KategoriID
					+ "\", \"TarzID\":\"" + TarzID
					+ "\", \"SanatciAdi\":\"" + SanatciAdi
					+ "\", \"SarkiAdi\":\"" + SarkiAdi
					+ "\", \"Icerik\":\"" + Icerik + "\"}";
		}
	}

	public String SarkiIcerikGetir(int SarkiID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Icerik FROM tblSarkilar Where id = " + SarkiID;
		String Icerik = "";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				Icerik = cursor.getString(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return Icerik;
		}
	}

	public int SarkiSayisiGetir() {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblSarkilar";
		int ToplamSarkiSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamSarkiSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamSarkiSayisi;
		}
	}

	public boolean SarkiVarMiKontrol(int SarkiID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT id FROM tblSarkilar WHERE id = " + SarkiID;
		Boolean KayitVarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				KayitVarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return KayitVarMi;
		}
	}

	public boolean CihazdaSecilenSarkiVarMi(String SanatciAdi, String SarkiAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT id FROM tblSarkilar WHERE SanatciAdi = '" + SanatciAdi + "' AND SarkiAdi = '" + SarkiAdi;
		Boolean KayitVarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				KayitVarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return KayitVarMi;
		}
	}

	public boolean CihazdaSecilenSarkiVarMi(int ListeID, String SanatciAdi, String SarkiAdi) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT id FROM tblSarkilar WHERE ListeID = " + ListeID + " AND SanatciAdi = '" + SanatciAdi + "' AND SarkiAdi = '" + SarkiAdi;
		Boolean KayitVarMi = false;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				KayitVarMi = true;
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return KayitVarMi;
		}
	}

	// ##### ISTEKLER #####
	public boolean IstekEkle(int SarkiID, String ClientID, String ClientAdSoyad, String ClientIP, String ClientNot, String IstekTarihi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put("SarkiID", SarkiID);
			values.put("ClientID", ClientID);
			values.put("ClientAdSoyad", ClientAdSoyad);
			values.put("ClientIP", ClientIP);
			values.put("ClientNot", ClientNot);
			values.put("IstekTarihi", IstekTarihi);
			db.insert("tblIstekler", null, values);
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public boolean IstekSil(int IstekID) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.delete("tblIstekler", "id = ?", new String[]{String.valueOf(IstekID)});
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
			return false;
		} finally {
			db.close();
			return true;
		}
	}

	public List<SnfIstekler> lst_IstekGetir() {
		List<SnfIstekler> lstIstekler = new ArrayList<SnfIstekler>();
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT tblIstekler.id, tblIstekler.SarkiID, tblSarkilar.SanatciAdi, tblSarkilar.SarkiAdi, tblIstekler.ClientID, tblIstekler.ClientAdSoyad, tblIstekler.ClientIP, tblIstekler.ClientNot, tblIstekler.IstekTarihi FROM tblIstekler INNER JOIN tblSarkilar ON tblSarkilar.id = tblIstekler.SarkiID ORDER BY tblIstekler.IstekTarihi DESC";

		Cursor cursor = db.rawQuery(Sorgu, null);

		while (cursor.moveToNext()) {
			SnfIstekler Istekler = new SnfIstekler();
			Istekler.setId(cursor.getInt(0));
			Istekler.setSarkiID(cursor.getInt(1));
			Istekler.setSanatciAdi(cursor.getString(2));
			Istekler.setSarkiAdi(cursor.getString(3));
			Istekler.setClientID(cursor.getString(4));
			Istekler.setClientAdSoyad(cursor.getString(5));
			Istekler.setClientIP(cursor.getString(6));
			Istekler.setClientNot(cursor.getString(7));
			Istekler.setIstekTarihi(cursor.getString(8));
			lstIstekler.add(Istekler);
		}

		return lstIstekler;
	}

	public String ClientIstekKontrol(String ClientID, String IstekTarihi, int IstekSuresiDakika) {
		SQLiteDatabase db = this.getWritableDatabase();
		int YeniIstekSuresiSaniye = 60 * IstekSuresiDakika;
		String Sorgu = "SELECT IstekTarihi FROM tblIstekler WHERE ClientID = '" + ClientID + "'";
		String Sonuc = "{\"Sonuc\":0, \"KalanSureSaniye\":" + YeniIstekSuresiSaniye + "}";

		/*
		Dönen sonuç değerleri;
		0 Client yeni istek yapabilir.
		1 Client spam filtresinden geçti. Yeni istek yapabilir.
		2 Client spam filtresine takıldı.
		 */

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				int ZamanFarkiSaniye = (int) Math.floor((format.parse(IstekTarihi).getTime() - format.parse(cursor.getString(0)).getTime()) / 1000);

				if(ZamanFarkiSaniye <= YeniIstekSuresiSaniye) Sonuc = "{\"Sonuc\":2, \"KalanSureSaniye\":\"" + (YeniIstekSuresiSaniye - ZamanFarkiSaniye) + "\"}";
				else Sonuc = "{\"Sonuc\":1, \"KalanSureSaniye\":" + YeniIstekSuresiSaniye + "}";
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return Sonuc;
		}
	}

	public String IstekGetir(int SarkiID) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT * FROM tblTarzlar Where id = " + SarkiID;
		String TarzAdi = "";

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				TarzAdi = cursor.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return TarzAdi;
		}
	}

	public int IstekSayisiGetir() {
		SQLiteDatabase db = this.getWritableDatabase();
		String Sorgu = "SELECT Count(*) FROM tblIstekler";
		int ToplamIstekSayisi = 0;

		try {
			Cursor cursor = db.rawQuery(Sorgu, null);

			while (cursor.moveToNext()) {
				ToplamIstekSayisi = cursor.getInt(0);
			}
		} catch (Exception e) {
			System.out.println("Bir hata oluştu: " + e);
		} finally {
			db.close();
			return ToplamIstekSayisi;
		}
	}

	public void IstekTablosunuBosalt() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS tblIstekler;");
		db.execSQL("CREATE TABLE tblIstekler (id INTEGER PRIMARY KEY AUTOINCREMENT, SarkiID INTEGER, ClientID TEXT, ClientAdSoyad TEXT, ClientIP TEXT, ClientNot TEXT, IstekTarihi DATETIME);");
	}
}