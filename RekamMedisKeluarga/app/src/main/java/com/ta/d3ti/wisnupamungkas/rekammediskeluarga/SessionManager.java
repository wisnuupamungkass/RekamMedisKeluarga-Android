package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
	//Shared Preferences
	SharedPreferences pref;
	//Editor for Shared preferences
	Editor editor;
	//context
	Context _context;
	//shared pref mode
	int PRIVATE_MODE = 0;
	//nama sharepreference
	private static final String PREF_NAME = "Sesi";
	// All Shared Preferences Keys
	private static final String IS_LOGIN_PETUGAS = "IsLoggedInPetugas";
	private static final String IS_LOGIN_USER = "IsLoggedInUser";
    private static final String IS_TOKEN = "IsLoggedInToken";

	public static final String KEY_EMAIL = "email";
	public static final String KEY_KTP = "no_ktp";
	public static final String KEY_PETUGAS = "id_petugas";
	public static final String KEY_ID = "id_token";
	public static final String KEY_PASS = "password";
	public static final String KEY_STATUS = "status";
	public static final String KEY_JK = "jk";
	//constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
//Create Login Session Petugas
public void createLoginPetugas(String email, String password, String id_petugas, String status){
	//Storing Login value as TRUE
	editor.putBoolean(IS_LOGIN_PETUGAS, true);
	editor.putString(KEY_EMAIL, email);
	editor.putString(KEY_PASS, password);
	editor.putString(KEY_PETUGAS, id_petugas);
	editor.putString(KEY_STATUS, status);
	editor.commit();
}

	//Create Login Session User
	public void createLoginUser(String email, String no_ktp, String jk){
		//Storing Login value as TRUE
		editor.putBoolean(IS_LOGIN_USER, true);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_KTP, no_ktp);
		editor.putString(KEY_JK, jk);
		editor.commit();
	}

    //Create Login Session Token
    public void createLoginToken(String id_token){
        //Storing Login value as TRUE
        editor.putBoolean(IS_TOKEN, true);
        editor.putString(KEY_ID, id_token);
        editor.commit();
    }

	public void checkLoginPetugas(){
	// Check Login status
		if(!this.isLoggedInPetugas()){
			Intent i = new Intent(_context, LoginPetugasActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
	}

	public void checkLoginPria(){
		// Check Login status
		if(!this.isLoggedInUser()){
			Intent i = new Intent(_context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
	}

	public void checkLoginPerempuan(){
		// Check Login status
		if(!this.isLoggedInUser()){
			Intent i = new Intent(_context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
	}

    public void checkLoginToken(){
        // Check Login status
        if(!this.isLoggedInToken()){
            Intent i = new Intent(_context, MainPetugasActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }
	/**
	 * Get stored session data
	 * */
	public HashMap<String,String> getUserDetails(){
		HashMap<String,String> user = new HashMap<String,String>();
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_KTP, pref.getString(KEY_KTP, null));
		user.put(KEY_PASS, pref.getString(KEY_PASS, null));
		return user;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String,String> getPetugasDetails(){
		HashMap<String,String> petugas = new HashMap<String,String>();
		petugas.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		petugas.put(KEY_PETUGAS, pref.getString(KEY_PETUGAS, null));
		petugas.put(KEY_PASS, pref.getString(KEY_PASS, null));
		return petugas;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String,String> getTokenDetails(){
		HashMap<String,String> token = new HashMap<String,String>();
		token.put(KEY_ID, pref.getString(KEY_ID, null));
		return token;
	}

	/**
	 * Clear session details
	 * */
	public void logoutPetugas(){
	// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		Intent i = new Intent(_context, LoginPetugasActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}

	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		Intent i = new Intent(_context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}


	public boolean isLoggedInPetugas(){
		return pref.getBoolean(IS_LOGIN_PETUGAS, false);
	}
	public boolean isLoggedInUser(){
		return pref.getBoolean(IS_LOGIN_USER, false);
	}
    public boolean isLoggedInToken() {
        return this.pref.getBoolean(IS_TOKEN, false);
    }

}