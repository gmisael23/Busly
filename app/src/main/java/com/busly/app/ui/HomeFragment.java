package com.busly.app.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.busly.app.LoginActivity;
import com.busly.app.R;
import com.busly.app.data.RepositorioMock;
import com.busly.app.databinding.FragmentHomeBinding;
import com.busly.app.model.Aluno;
import com.busly.app.util.Sessao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Tela "Início": acompanhamento da rota em tempo real.
 * A posição do ônibus é simulada com um ValueAnimator até que haja dados reais de GPS.
 */
public class HomeFragment extends Fragment {

    /** Posição inicial do ônibus na rota (0 a 1). */
    private static final float PROGRESSO_INICIAL = 0.45f;
    /** Duração da simulação, em milissegundos. */
    private static final long DURACAO_SIMULACAO_MS = 60_000L;

    private FragmentHomeBinding binding;
    private List<Aluno> alunos;
    private ValueAnimator animador;
    private int ultimoMinuto = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alunos = RepositorioMock.alunos();
        String responsavel = RepositorioMock.RESPONSAVEL;

        binding.tvSaudacao.setText(getString(R.string.saudacao, responsavel));
        binding.tvAvatar.setText(RepositorioMock.RESPONSAVEL_INICIAIS);
        binding.tvAvatar.setOnClickListener(v -> confirmarSaida());

        binding.tvAluno1.setText(alunos.get(0).getRotulo());
        binding.tvAluno2.setText(alunos.get(1).getRotulo());
        binding.tvAluno1.setOnClickListener(v -> selecionarAluno(0));
        binding.tvAluno2.setOnClickListener(v -> selecionarAluno(1));

        selecionarAluno(0);
    }

    private void selecionarAluno(int indice) {
        estilizarChip(binding.tvAluno1, indice == 0);
        estilizarChip(binding.tvAluno2, indice == 1);
        iniciarSimulacao(alunos.get(indice));
    }

    private void estilizarChip(TextView chip, boolean ativo) {
        chip.setBackgroundResource(ativo ? R.drawable.bg_chip_ativo : R.drawable.bg_chip_inativo);
        chip.setTextColor(ContextCompat.getColor(requireContext(),
                ativo ? R.color.white : R.color.busly_primary));
    }

    private void iniciarSimulacao(Aluno aluno) {
        cancelarSimulacao();
        ultimoMinuto = -1;

        binding.tvStatus.setText(R.string.status_em_rota);
        binding.tvProximoPonto.setText(aluno.getProximoPonto());
        binding.routeMap.setProgresso(PROGRESSO_INICIAL);
        atualizarEta(aluno.getMinutosParaChegada());

        animador = ValueAnimator.ofFloat(PROGRESSO_INICIAL, 1f);
        animador.setDuration(DURACAO_SIMULACAO_MS);
        animador.setInterpolator(new LinearInterpolator());
        animador.addUpdateListener(a -> {
            if (binding == null) {
                return;
            }
            float progresso = (float) a.getAnimatedValue();
            binding.routeMap.setProgresso(progresso);

            if (progresso >= 1f) {
                binding.tvStatus.setText(R.string.status_chegou);
                binding.tvEta.setText("0 min");
                return;
            }

            float restante = (1f - progresso) / (1f - PROGRESSO_INICIAL);
            int minutos = (int) Math.ceil(aluno.getMinutosParaChegada() * restante);
            if (minutos != ultimoMinuto) {
                ultimoMinuto = minutos;
                atualizarEta(minutos);
            }
        });
        animador.start();
    }

    /** Mostra "N min · HH:mm" com o horário previsto a partir de agora. */
    private void atualizarEta(int minutos) {
        Calendar previsao = Calendar.getInstance();
        previsao.add(Calendar.MINUTE, minutos);
        String hora = new SimpleDateFormat("HH:mm", Locale.forLanguageTag("pt-BR"))
                .format(previsao.getTime());
        binding.tvEta.setText(minutos + " min · " + hora);
    }

    private void cancelarSimulacao() {
        if (animador != null) {
            animador.cancel();
            animador = null;
        }
    }

    private void confirmarSaida() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.sair_titulo)
                .setMessage(R.string.sair_mensagem)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.sair, (dialog, which) -> {
                    Sessao.setLogado(requireContext(), false);
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        cancelarSimulacao();
        super.onDestroyView();
        binding = null;
    }
}
