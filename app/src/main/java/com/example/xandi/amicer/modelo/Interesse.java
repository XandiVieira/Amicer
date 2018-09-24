package com.example.xandi.amicer.modelo;

import android.support.annotation.Nullable;

import com.example.xandi.amicer.Chip;

import java.util.List;

public class Interesse {

    private List<Chip> tags;
    String categoria;
    Boolean ativado;
    Boolean distanciaAtivada = false;
    Boolean idadeAtivada = false;
    int distancia;
    int idade;

    public Interesse(){}

    public Interesse(List<Chip> tags, String categoria, Boolean ativado, Boolean distanciaAtivada, Boolean idadeAtivada, @Nullable int distancia, @Nullable int idade) {
        this.tags = tags;
        this.categoria = categoria;
        this.ativado = ativado;
        this.distanciaAtivada = distanciaAtivada;
        this.idadeAtivada = idadeAtivada;
        this.distancia = distancia;
        this.idade = idade;
    }

    public Interesse(List<Chip> tags, String categoria) {
        this.tags = tags;
        this.categoria = categoria;
    }

    public List<Chip> getTags() {
        return tags;
    }

    public void setTags(List<Chip> tags) {
        this.tags = tags;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
