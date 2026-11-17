package com.busly.app.data;

import com.busly.app.model.Aluno;
import com.busly.app.model.Notificacao;
import com.busly.app.model.Notificacao.Tipo;
import com.busly.app.model.RegistroViagem;

import java.util.ArrayList;
import java.util.List;

/**
 * Fonte de dados simulada, igual ao protótipo navegável do banner.
 * Próximo passo do projeto: trocar por dados reais (GPS + banco de dados).
 */
public final class RepositorioMock {

    public static final String RESPONSAVEL = "Ana";
    public static final String RESPONSAVEL_INICIAIS = "AC";

    private RepositorioMock() {
    }

    public static List<Aluno> alunos() {
        List<Aluno> lista = new ArrayList<>();
        lista.add(new Aluno("Pedro", "4º ano", 8, "Rua das Flores"));
        lista.add(new Aluno("Julia", "1º ano", 12, "Av. Central"));
        return lista;
    }

    public static List<Notificacao> notificacoes() {
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

    public static List<RegistroViagem> historico() {
        List<RegistroViagem> lista = new ArrayList<>();
        lista.add(new RegistroViagem("Hoje, 26 set", "07:38", "08:05"));
        lista.add(new RegistroViagem("Ontem, 25 set", "07:35", "08:10"));
        lista.add(new RegistroViagem("24 set", "07:40", "08:02"));
        return lista;
    }
}
