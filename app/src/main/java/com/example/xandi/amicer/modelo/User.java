package com.example.xandi.amicer.modelo;

import com.example.xandi.amicer.Localizacao;

import java.util.HashMap;
import java.util.List;

public class User {

    private String uid;
    private String nome;
    private String email;
    private int idade;
    private List<Interesse> categorias;
    private HashMap<String, Boolean> listGroups;
    private String fotoPerfil;
    private String frase;
    private String descricao;
    private Localizacao localizacao;

    public User(){
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public List<Interesse> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Interesse> categorias) {
        this.categorias = categorias;
    }

    public HashMap<String, Boolean> getListGroups() {
        return listGroups;
    }

    public void setListGroups(HashMap<String, Boolean> listGroups) {
        this.listGroups = listGroups;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }
}
