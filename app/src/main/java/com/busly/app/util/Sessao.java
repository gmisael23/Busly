package com.busly.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/** Guarda localmente o login do usuário (sem backend por enquanto). */
public final class Sessao {

    private static final String ARQUIVO = "busly_prefs";
    private static final String CHAVE_LOGADO = "logado";
    private static final String CHAVE_EMAIL = "email";

    private Sessao() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
    }

    public static boolean isLogado(Context context) {
        return prefs(context).getBoolean(CHAVE_LOGADO, false);
    }

    public static void entrar(Context context, String email) {
        prefs(context).edit()
                .putBoolean(CHAVE_LOGADO, true)
                .putString(CHAVE_EMAIL, email)
                .apply();
    }

    public static void sair(Context context) {
        prefs(context).edit()
                .putBoolean(CHAVE_LOGADO, false)
                .remove(CHAVE_EMAIL)
                .apply();
    }

    public static String getEmail(Context context) {
        return prefs(context).getString(CHAVE_EMAIL, "");
    }
}
