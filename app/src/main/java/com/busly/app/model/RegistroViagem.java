package com.busly.app.model;

/** Registro de embarque e desembarque de um dia. */
public class RegistroViagem {

    private final String data;
    private final String horaEmbarque;
    private final String horaDesembarque;

    public RegistroViagem(String data, String horaEmbarque, String horaDesembarque) {
        this.data = data;
        this.horaEmbarque = horaEmbarque;
        this.horaDesembarque = horaDesembarque;
    }

    public String getData() {
        return data;
    }

    public String getHoraEmbarque() {
        return horaEmbarque;
    }

    public String getHoraDesembarque() {
        return horaDesembarque;
    }

    /** Duração da viagem em minutos, calculada a partir de "HH:mm". */
    public int getDuracaoMinutos() {
        return paraMinutos(horaDesembarque) - paraMinutos(horaEmbarque);
    }

    private static int paraMinutos(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[0]) * 60 + Integer.parseInt(partes[1]);
    }
}
