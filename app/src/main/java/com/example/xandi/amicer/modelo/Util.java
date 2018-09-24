package com.example.xandi.amicer.modelo;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<Integer> distancia = new ArrayList<Integer>();
    public static List<Integer> idade = new ArrayList<Integer>();

    public static FirebaseUser fbUser;
    public static DatabaseReference mDatabaseRef;
    public static DatabaseReference mUserDatabaseRef;

    public static GoogleApiClient googleApiClient;

    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    public static GoogleSignInOptions gso;

    public Util(GoogleSignInOptions gso, FirebaseUser fbUser, DatabaseReference mDatabaseRef, DatabaseReference mUserDatabaseRef, GoogleApiClient googleApiClient, FirebaseAuth mFirebaseAuth, FirebaseAuth.AuthStateListener mFirebaseAuthListener) {
        this.gso = gso;
        this.fbUser = fbUser;
        this.mDatabaseRef = mDatabaseRef;
        this.mUserDatabaseRef = mUserDatabaseRef;
        this.googleApiClient = googleApiClient;
        this.mFirebaseAuth = mFirebaseAuth;
        this.mFirebaseAuthListener = mFirebaseAuthListener;
        distancia.add(0);
        distancia.add(0);
        distancia.add(0);
        distancia.add(0);
        idade.add(0);
        idade.add(0);
        idade.add(0);
        idade.add(0);
    }

    public static List<Integer> getDistancia() {
        return distancia;
    }

    public static void setDistancia(List<Integer> distancia) {
        Util.distancia = distancia;
    }

    public static List<Integer> getIdade() {
        return idade;
    }

    public static void setIdade(List<Integer> idade) {
        Util.idade = idade;
    }

    public static FirebaseUser getFbUser() {
        return fbUser;
    }

    public static void setFbUser(FirebaseUser fbUser) {
        Util.fbUser = fbUser;
    }

    public static DatabaseReference getmDatabaseRef() {
        return mDatabaseRef;
    }

    public static void setmDatabaseRef(DatabaseReference mDatabaseRef) {
        Util.mDatabaseRef = mDatabaseRef;
    }

    public static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public static void setGoogleApiClient(GoogleApiClient googleApiClient) {
        Util.googleApiClient = googleApiClient;
    }

    public static FirebaseAuth getmFirebaseAuth() {
        return mFirebaseAuth;
    }

    public static void setmFirebaseAuth(FirebaseAuth mFirebaseAuth) {
        Util.mFirebaseAuth = mFirebaseAuth;
    }

    public static FirebaseAuth.AuthStateListener getmFirebaseAuthListener() {
        return mFirebaseAuthListener;
    }

    public static void setmFirebaseAuthListener(FirebaseAuth.AuthStateListener mFirebaseAuthListener) {
        Util.mFirebaseAuthListener = mFirebaseAuthListener;
    }

    public static DatabaseReference getmUserDatabaseRef() {
        return mUserDatabaseRef;
    }

    public static void setmUserDatabaseRef(DatabaseReference mUserDatabaseRef) {
        Util.mUserDatabaseRef = mUserDatabaseRef;
    }

    public static GoogleSignInOptions getGso() {
        return gso;
    }

    public static void setGso(GoogleSignInOptions gso) {
        Util.gso = gso;
    }
}