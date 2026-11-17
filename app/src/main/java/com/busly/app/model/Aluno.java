package com.busly.app.model;

/** Aluno acompanhado pelo responsável. */
public class Aluno {

    private final String nome;
    private final String serie;
    private final int minutosParaChegada;
    private final String proximoPonto;

    public Aluno(String nome, String serie, int minutosParaChegada, String proximoPonto) {
        this.nome = nome;
        this.serie = serie;
        this.minutosParaChegada = minutosParaChegada;
        this.proximoPonto = proximoPonto;
    }

    public String getNome() {
        return nome;
    }

    public String getSerie() {
        return serie;
    }

    public int getMinutosParaChegada() {
        return minutosParaChegada;
    }

    public String getProximoPonto() {
        return proximoPonto;
    }

    /** Texto exibido no seletor, ex.: "Pedro · 4º ano". */
    public String getRotulo() {
        return nome + " · " + serie;
    }
}
