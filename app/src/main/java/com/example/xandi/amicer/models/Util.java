package com.example.xandi.amicer.models;

import com.example.xandi.amicer.objects.Tag;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Util {

    private static List<Integer> distance = new ArrayList<>();
    private static List<Integer> age = new ArrayList<>();

    public static FirebaseUser fbUser;
    public static DatabaseReference mDatabaseRef;
    public static DatabaseReference mUserDatabaseRef, mTagsDatabaseRef;

    public static GoogleApiClient googleApiClient;

    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private static GoogleSignInOptions gso;
    private static double longitude;
    public static double latitude;
    public static User user;
    private static List<Tag> tagsSugestions;

    public Util(GoogleSignInOptions gso, FirebaseUser fbUser, DatabaseReference mDatabaseRef, DatabaseReference mUserDatabaseRef, GoogleApiClient googleApiClient, FirebaseAuth mFirebaseAuth, FirebaseAuth.AuthStateListener mFirebaseAuthListener, double latitude, double longitude, User user, List<Tag> tagsSugestoes, List<String> listaCategorias) {
        Util.gso = gso;
        Util.fbUser = fbUser;
        Util.mDatabaseRef = mDatabaseRef;
        Util.mUserDatabaseRef = mUserDatabaseRef;
        Util.googleApiClient = googleApiClient;
        Util.mFirebaseAuth = mFirebaseAuth;
        Util.mFirebaseAuthListener = mFirebaseAuthListener;
        distance.add(0);
        distance.add(0);
        distance.add(0);
        distance.add(0);
        age.add(0);
        age.add(0);
        age.add(0);
        age.add(0);
        Util.latitude = latitude;
        Util.longitude = longitude;
        Util.user = user;
        tagsSugestions = tagsSugestoes;
    }

    public Util(GoogleSignInOptions gso, FirebaseUser fbUser, DatabaseReference mDatabaseRef, DatabaseReference mUserDatabaseRef, GoogleApiClient googleApiClient, FirebaseAuth mFirebaseAuth, FirebaseAuth.AuthStateListener mFirebaseAuthListener, double latitude, double longitude, User user) {
    }

    public static List<Integer> getDistance() {
        return distance;
    }

    public static void setDistance(List<Integer> distance) {
        Util.distance = distance;
    }

    public static List<Integer> getAge() {
        return age;
    }

    public static void setAge(List<Integer> age) {
        Util.age = age;
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

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        Util.longitude = longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        Util.latitude = latitude;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Util.user = user;
    }

    public static List<Tag> getTagsSugestions() {
        return tagsSugestions;
    }

    public static void setTagsSugestions(List<Tag> tagsSugestions) {
        Util.tagsSugestions = tagsSugestions;
    }

    public static DatabaseReference getmTagsDatabaseRef() {
        return mTagsDatabaseRef;
    }

    public static void setmTagsDatabaseRef(DatabaseReference mTagsDatabaseRef) {
        Util.mTagsDatabaseRef = mTagsDatabaseRef;
    }
}
