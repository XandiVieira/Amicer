package com.example.xandi.amicer.modelo;

import android.media.Image;
import android.net.Uri;

import com.example.xandi.amicer.Chip;

import java.util.HashMap;
import java.util.List;

public class User {

    private String uid;
    private String nome;
    private String email;
    private int idade;
    private List<List<Chip>> tags;
    private List<String> categorias;
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

    public List<List<Chip>> getTags() {
        return tags;
    }

    public void setTags(List<List<Chip>> tags) {
        this.tags = tags;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
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
