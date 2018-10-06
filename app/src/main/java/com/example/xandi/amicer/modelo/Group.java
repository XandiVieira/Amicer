package com.example.xandi.amicer.modelo;

import com.example.xandi.amicer.Localizacao;

public class Group {

    private String uid;
    private String nome;
    private String descricao;
    private int numParticipante;
    private Interesse categoria;
    private String criadorGrupo;
    private String userUID;
    private Localizacao localizacao;

    public void Group(String nome, String descricao, Interesse categoria){
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
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

    public String getCriadorGrupo() {
        return criadorGrupo;
    }

    public void setCriadorGrupo(String criadorGrupo) {
        this.criadorGrupo = criadorGrupo;
    }

    public Interesse getCategoria() {
        return categoria;
    }

    public void setCategoria(Interesse categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return nome;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }
}
