package com.example.xandi.amicer.modelo;

import android.media.Image;
import android.net.Uri;

import java.util.HashMap;
import java.util.List;

public class User {

    private String uid;
    private String nome;
    private String email;
    private int idade;
    private List<Interesse> interessesList;
    private HashMap<String, Boolean> listGroups;
    private Uri fotoPerfil;
    private String frase;
    private String descricao;

    public User(){
    }

    public HashMap<String, Boolean> getListGroups() {
        return listGroups;
    }

    public void setListGroups(HashMap<String, Boolean> listGroups) {
        this.listGroups = listGroups;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public List<Interesse> getInteressesList() {
        return interessesList;
    }

    public void setInteressesList(List<Interesse> interessesList) {
        this.interessesList = interessesList;
    }

    public Uri getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Uri fotoPerfil) {
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
}
