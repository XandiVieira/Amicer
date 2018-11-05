package com.example.xandi.amicer;

import android.support.annotation.NonNull;

import com.example.xandi.amicer.modelo.Group;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Brain {

    private List<Interesse> interesseList = Util.user.getCategorias();

    Query query = Util.getmDatabaseRef().child("Esportes");

}
