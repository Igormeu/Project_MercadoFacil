package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SessaoManager {

    private static final String PREF_NAME = "MyAppSessao";

    // Chaves
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessaoManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public void criarSessaoLogin(String email) {

        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }


    public boolean estaLogado() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getEmailUsuario() {
        return pref.getString(KEY_USER_EMAIL, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

    }
}