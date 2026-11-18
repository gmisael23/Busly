package com.busly.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.busly.app.MainActivity;
import com.busly.app.data.RepositorioMock;
import com.busly.app.databinding.FragmentAlertsBinding;

/** Tela "Alertas": toque em um alerta para marcá-lo como lido. */
public class AlertsFragment extends Fragment {

    private FragmentAlertsBinding binding;
    private NotificacaoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAlertsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new NotificacaoAdapter(RepositorioMock.notificacoes(), (notificacao, posicao) -> {
            if (notificacao.isNaoLida()) {
                notificacao.setNaoLida(false);
                adapter.notifyItemChanged(posicao);
                atualizarBadge();
            }
        });
        binding.rvNotificacoes.setAdapter(adapter);

        binding.tvMarcarTodas.setOnClickListener(v -> {
            RepositorioMock.marcarTodasComoLidas();
            adapter.notifyDataSetChanged();
            atualizarBadge();
        });
    }

    private void atualizarBadge() {
        ((MainActivity) requireActivity()).atualizarBadge();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
