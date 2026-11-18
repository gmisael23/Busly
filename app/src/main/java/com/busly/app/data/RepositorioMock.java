package com.busly.app.data;

import com.busly.app.model.Aluno;
import com.busly.app.model.Notificacao;
import com.busly.app.model.Notificacao.Tipo;
import com.busly.app.model.RegistroViagem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Fonte de dados simulada, igual ao protótipo navegável do banner.
 * Os alertas e as ausências ficam guardados em memória enquanto o app está aberto.
 * Próximo passo do projeto: trocar por dados reais (GPS + banco de dados).
 */
public final class RepositorioMock {

    public static final String RESPONSAVEL = "Ana";
    public static final String RESPONSAVEL_INICIAIS = "AC";

    // Dados fictícios do motorista. Troque pelo telefone real quando houver backend.
    public static final String MOTORISTA_NOME = "Carlos";
    public static final String MOTORISTA_ONIBUS = "Ônibus 12";
    public static final String MOTORISTA_TELEFONE = "11900000000";

    private static final List<Notificacao> NOTIFICACOES = criarNotificacoes();
    private static final Set<String> AUSENTES = new HashSet<>();

    private RepositorioMock() {
    }

    // ---------- Alunos ----------

    public static List<Aluno> alunos() {
        List<Aluno> lista = new ArrayList<>();
        lista.add(new Aluno("Pedro", "4º ano", 8, "Rua das Flores"));
        lista.add(new Aluno("Julia", "1º ano", 12, "Av. Central"));
        return lista;
    }

    public static boolean isAusente(String nomeAluno) {
        return AUSENTES.contains(nomeAluno);
    }

    public static void registrarAusencia(String nomeAluno) {
        AUSENTES.add(nomeAluno);
    }

    // ---------- Notificações ----------

    /** Lista viva: alterações aparecem na tela de Alertas. */
    public static List<Notificacao> notificacoes() {
        return NOTIFICACOES;
    }

    public static void adicionarNotificacao(Notificacao notificacao) {
        NOTIFICACOES.add(0, notificacao);
    }

    public static int naoLidas() {
        int total = 0;
        for (Notificacao n : NOTIFICACOES) {
            if (n.isNaoLida()) {
                total++;
            }
        }
        return total;
    }

    public static void marcarTodasComoLidas() {
        for (Notificacao n : NOTIFICACOES) {
            n.setNaoLida(false);
        }
    }

    private static List<Notificacao> criarNotificacoes() {
        List<Notificacao> lista = new ArrayList<>();
        lista.add(new Notificacao(Tipo.PROXIMIDADE, "Ônibus se aproximando",
                "Chega em 3 minutos no ponto Rua das Flores.", "Agora", true));
        lista.add(new Notificacao(Tipo.EMBARQUE, "Embarque confirmado",
                "Pedro embarcou às 07:38 na Rua das Flores.", "Hoje, 07:38", true));
        lista.add(new Notificacao(Tipo.DESEMBARQUE, "Desembarque na escola",
                "Pedro chegou à escola às 08:05.", "Hoje, 08:05", false));
        lista.add(new Notificacao(Tipo.ATRASO, "Atraso na rota",
                "Trânsito na Av. Central, atraso de 5 min.", "Ontem, 07:30", false));
        return lista;
    }

    // ---------- Histórico ----------

    public static List<RegistroViagem> historico(String nomeAluno) {
        List<RegistroViagem> lista = new ArrayList<>();
        if ("Julia".equals(nomeAluno)) {
            lista.add(new RegistroViagem("Hoje, 26 set", "07:50", "08:12"));
            lista.add(new RegistroViagem("Ontem, 25 set", "07:48", "08:15"));
            lista.add(new RegistroViagem("24 set", "07:52", "08:09"));
        } else {
            lista.add(new RegistroViagem("Hoje, 26 set", "07:38", "08:05"));
            lista.add(new RegistroViagem("Ontem, 25 set", "07:35", "08:10"));
            lista.add(new RegistroViagem("24 set", "07:40", "08:02"));
        }
        return lista;
    }
}
