package com.example.xandi.amicer.modelo;

import java.util.List;

public class Group {

    private String uid;
    private String nome;
    private String descricao;
    private int numParticipante;
    private List<String> interesses;
    private String criadorGrupo;

    public void Group(String nome, String descricao, List<String> interesses){
        this.nome = nome;
        this.descricao = descricao;
        this.interesses = interesses;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNumParticipante() {
        return numParticipante;
    }

    public void setNumParticipante(int numParticipante) {
        this.numParticipante = numParticipante;
    }

    public List<String> getInteresses() {
        return interesses;
    }

    public void setInteresses(List<String> interesses) {
        this.interesses = interesses;
    }

    public String getCriadorGrupo() {
        return criadorGrupo;
    }

    public void setCriadorGrupo(String criadorGrupo) {
        this.criadorGrupo = criadorGrupo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
