package com.busly.app.ui;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.busly.app.MainActivity;
import com.busly.app.R;
import com.busly.app.data.RepositorioMock;
import com.busly.app.databinding.FragmentHomeBinding;
import com.busly.app.model.Aluno;
import com.busly.app.model.Notificacao;
import com.busly.app.util.Chips;
import com.busly.app.util.Configuracoes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

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
    /** Minutos restantes para disparar o aviso de proximidade. */
    private static final int MINUTOS_AVISO = 3;

    private FragmentHomeBinding binding;
    private List<Aluno> alunos;
    private Aluno alunoAtual;
    private ValueAnimator animador;
    private int ultimoMinuto = -1;
    private boolean avisouProximidade;
    private boolean avisouChegada;

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

        binding.tvSaudacao.setText(getString(R.string.saudacao, RepositorioMock.RESPONSAVEL));
        binding.tvAvatar.setText(RepositorioMock.RESPONSAVEL_INICIAIS);
        binding.tvAvatar.setOnClickListener(v -> ((MainActivity) requireActivity()).abrirPerfil());

        binding.tvAluno1.setText(alunos.get(0).getRotulo());
        binding.tvAluno2.setText(alunos.get(1).getRotulo());
        binding.tvAluno1.setOnClickListener(v -> selecionarAluno(0));
        binding.tvAluno2.setOnClickListener(v -> selecionarAluno(1));

        binding.tvMotorista.setText(
                RepositorioMock.MOTORISTA_NOME + " · " + RepositorioMock.MOTORISTA_ONIBUS);
        binding.btnLigar.setOnClickListener(v -> ligarParaMotorista());
        binding.btnAusencia.setOnClickListener(v -> confirmarAusencia());

        selecionarAluno(0);
    }

    // ---------- Seleção de aluno ----------

    private void selecionarAluno(int indice) {
        alunoAtual = alunos.get(indice);
        Chips.estilizar(binding.tvAluno1, indice == 0);
        Chips.estilizar(binding.tvAluno2, indice == 1);
        atualizarBotaoAusencia();
        iniciarSimulacao(alunoAtual);
    }

    // ---------- Ligar para o motorista ----------

    private void ligarParaMotorista() {
        // ACTION_DIAL só abre o discador com o número; não exige permissão.
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + RepositorioMock.MOTORISTA_TELEFONE));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), R.string.ligar_indisponivel, Toast.LENGTH_SHORT).show();
        }
    }

    // ---------- Avisar ausência ----------

    private void atualizarBotaoAusencia() {
        boolean ausente = RepositorioMock.isAusente(alunoAtual.getNome());
        binding.btnAusencia.setEnabled(!ausente);
        binding.btnAusencia.setAlpha(ausente ? 0.6f : 1f);
        binding.btnAusencia.setText(ausente
                ? getString(R.string.ausencia_avisada, alunoAtual.getNome())
                : getString(R.string.avisar_ausencia));
    }

    private void confirmarAusencia() {
        Aluno aluno = alunoAtual;
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.ausencia_titulo)
                .setMessage(getString(R.string.ausencia_confirmar, aluno.getNome()))
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.confirmar, (dialog, which) -> {
                    RepositorioMock.registrarAusencia(aluno.getNome());
                    RepositorioMock.adicionarNotificacao(new Notificacao(
                            Notificacao.Tipo.AVISO, getString(R.string.ausencia_registrada),
                            aluno.getNome() + " não irá à escola hoje.", "Agora", true));
                    if (binding != null) {
                        atualizarBotaoAusencia();
                        atualizarBadge();
                        mostrarAviso(getString(R.string.ausencia_registrada));
                    }
                })
                .show();
    }

    // ---------- Simulação da rota ----------

    private void iniciarSimulacao(Aluno aluno) {
        cancelarSimulacao();
        ultimoMinuto = -1;
        avisouProximidade = false;
        avisouChegada = false;

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
                avisarChegada(aluno);
                return;
            }

            float restante = (1f - progresso) / (1f - PROGRESSO_INICIAL);
            int minutos = (int) Math.ceil(aluno.getMinutosParaChegada() * restante);
            if (minutos != ultimoMinuto) {
                ultimoMinuto = minutos;
                atualizarEta(minutos);
                if (minutos <= MINUTOS_AVISO) {
                    avisarProximidade(aluno, minutos);
                }
            }
        });
        animador.start();
    }

    private void avisarProximidade(Aluno aluno, int minutos) {
        if (avisouProximidade || RepositorioMock.isAusente(aluno.getNome())
                || !Configuracoes.avisoProximidade(requireContext())) {
            return;
        }
        avisouProximidade = true;
        String mensagem = "Chega em " + minutos + " min no ponto " + aluno.getProximoPonto() + ".";
        RepositorioMock.adicionarNotificacao(new Notificacao(
                Notificacao.Tipo.PROXIMIDADE, "Ônibus se aproximando", mensagem, "Agora", true));
        atualizarBadge();
        mostrarAviso("Ônibus se aproximando: " + mensagem);
    }

    private void avisarChegada(Aluno aluno) {
        if (avisouChegada || RepositorioMock.isAusente(aluno.getNome())
                || !Configuracoes.avisoChegada(requireContext())) {
            return;
        }
        avisouChegada = true;
        String mensagem = aluno.getNome() + " chegou à escola às " + horaAtual() + ".";
        RepositorioMock.adicionarNotificacao(new Notificacao(
                Notificacao.Tipo.DESEMBARQUE, "Desembarque na escola", mensagem,
                "Hoje, " + horaAtual(), true));
        atualizarBadge();
        mostrarAviso(mensagem);
    }

    /** Mostra "N min · HH:mm" com o horário previsto a partir de agora. */
    private void atualizarEta(int minutos) {
        Calendar previsao = Calendar.getInstance();
        previsao.add(Calendar.MINUTE, minutos);
        binding.tvEta.setText(minutos + " min · " + formatarHora(previsao));
    }

    private String horaAtual() {
        return formatarHora(Calendar.getInstance());
    }

    private String formatarHora(Calendar calendario) {
        return new SimpleDateFormat("HH:mm", Locale.forLanguageTag("pt-BR"))
                .format(calendario.getTime());
    }

    private void cancelarSimulacao() {
        if (animador != null) {
            animador.cancel();
            animador = null;
        }
    }

    // ---------- Auxiliares ----------

    private void atualizarBadge() {
        ((MainActivity) requireActivity()).atualizarBadge();
    }

    /** Snackbar posicionado acima da barra de navegação. */
    private void mostrarAviso(String texto) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), texto, Snackbar.LENGTH_LONG);
        View navegacao = requireActivity().findViewById(R.id.bottomNav);
        if (navegacao != null) {
            snackbar.setAnchorView(navegacao);
        }
        snackbar.show();
    }

    @Override
    public void onDestroyView() {
        cancelarSimulacao();
        super.onDestroyView();
        binding = null;
    }
}
