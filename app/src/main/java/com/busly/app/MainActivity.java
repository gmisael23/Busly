package com.busly.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.busly.app.data.RepositorioMock;
import com.busly.app.databinding.ActivityMainBinding;
import com.busly.app.ui.AlertsFragment;
import com.busly.app.ui.HistoryFragment;
import com.busly.app.ui.HomeFragment;
import com.busly.app.ui.PerfilFragment;
import com.busly.app.util.Sessao;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/** Container das telas Início, Alertas, Histórico e Perfil, com navegação inferior. */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment;
            if (id == R.id.nav_inicio) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_alertas) {
                fragment = new AlertsFragment();
            } else if (id == R.id.nav_historico) {
                fragment = new HistoryFragment();
            } else {
                fragment = new PerfilFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            binding.bottomNav.setSelectedItemId(R.id.nav_inicio);
        }
        atualizarBadge();
    }

    /** Mostra, na aba Alertas, quantas notificações ainda não foram lidas. */
    public void atualizarBadge() {
        int naoLidas = RepositorioMock.naoLidas();
        if (naoLidas > 0) {
            BadgeDrawable badge = binding.bottomNav.getOrCreateBadge(R.id.nav_alertas);
            badge.setNumber(naoLidas);
            badge.setBackgroundColor(ContextCompat.getColor(this, R.color.busly_laranja));
            badge.setBadgeTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            binding.bottomNav.removeBadge(R.id.nav_alertas);
        }
    }

    public void abrirPerfil() {
        binding.bottomNav.setSelectedItemId(R.id.nav_perfil);
    }

    public void confirmarSaida() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sair_titulo)
                .setMessage(R.string.sair_mensagem)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.sair, (dialog, which) -> {
                    Sessao.sair(this);
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .show();
    }
}
