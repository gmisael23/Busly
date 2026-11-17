package com.busly.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/** Guarda localmente se o usuário está logado (sem backend por enquanto). */
public final class Sessao {

    private static final String ARQUIVO = "busly_prefs";
    private static final String CHAVE_LOGADO = "logado";

    private Sessao() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
    }

    public static boolean isLogado(Context context) {
        return prefs(context).getBoolean(CHAVE_LOGADO, false);
    }

    public static void setLogado(Context context, boolean logado) {
        prefs(context).edit().putBoolean(CHAVE_LOGADO, logado).apply();
    }
}
