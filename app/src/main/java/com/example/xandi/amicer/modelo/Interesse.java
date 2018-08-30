package com.example.xandi.amicer.modelo;

import android.support.annotation.Nullable;

public class Interesse {

    String nome;
    Boolean ativado;
    Boolean distanciaAtivada;
    Boolean idadeAtivada;
    int distancia;
    int idade;

    public Interesse(){}

    public Interesse(String nome, Boolean ativado, Boolean distanciaAtivada, Boolean idadeAtivada, @Nullable int distancia, @Nullable int idade) {
        this.nome = nome;
        this.ativado = ativado;
        this.distanciaAtivada = distanciaAtivada;
        this.idadeAtivada = idadeAtivada;
        this.distancia = distancia;
        this.idade = idade;
    }

    public Interesse(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivado() {
        return ativado;
    }

    public void setAtivado(Boolean ativado) {
        this.ativado = ativado;
    }

    public Boolean getDistanciaAtivada() {
        return distanciaAtivada;
    }

    public void setDistanciaAtivada(Boolean distanciaAtivada) {
        this.distanciaAtivada = distanciaAtivada;
    }

    public Boolean getIdadeAtivada() {
        return idadeAtivada;
    }

    public void setIdadeAtivada(Boolean idadeAtivada) {
        this.idadeAtivada = idadeAtivada;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}
