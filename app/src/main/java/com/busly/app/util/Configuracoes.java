package com.busly.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/** Preferências de avisos do responsável. Ambas começam ligadas. */
public final class Configuracoes {

    private static final String ARQUIVO = "busly_prefs";
    private static final String CHAVE_PROXIMIDADE = "aviso_proximidade";
    private static final String CHAVE_CHEGADA = "aviso_chegada";

    private Configuracoes() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
    }

    public static boolean avisoProximidade(Context context) {
        return prefs(context).getBoolean(CHAVE_PROXIMIDADE, true);
    }

    public static void setAvisoProximidade(Context context, boolean ligado) {
        prefs(context).edit().putBoolean(CHAVE_PROXIMIDADE, ligado).apply();
    }

    public static boolean avisoChegada(Context context) {
        return prefs(context).getBoolean(CHAVE_CHEGADA, true);
    }

    public static void setAvisoChegada(Context context, boolean ligado) {
        prefs(context).edit().putBoolean(CHAVE_CHEGADA, ligado).apply();
    }
}
