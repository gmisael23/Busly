package com.busly.app.util;

import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.busly.app.R;

/** Estilo dos botões de seleção (Pedro / Julia) usados em mais de uma tela. */
public final class Chips {

    private Chips() {
    }

    public static void estilizar(TextView chip, boolean ativo) {
        chip.setBackgroundResource(ativo ? R.drawable.bg_chip_ativo : R.drawable.bg_chip_inativo);
        chip.setTextColor(ContextCompat.getColor(chip.getContext(),
                ativo ? R.color.white : R.color.busly_primary));
    }
}
