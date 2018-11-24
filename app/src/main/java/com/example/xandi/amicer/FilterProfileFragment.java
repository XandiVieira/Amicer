package com.example.xandi.amicer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.xandi.amicer.modelo.Interesse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilterProfileFragment extends Fragment {

    private ListView listViewInteresses;
    private ArrayList<Interesse> listaInteresses = new ArrayList<Interesse>();
    private FirebaseUser fbUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private ArrayAdapter adapter;

    public FilterProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter_profile, container, false);
        View itemView = inflater.inflate(R.layout.item_interesse_usuario, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fbUser = mFirebaseAuth.getCurrentUser();
        startFirebase();

        listViewInteresses = rootView.findViewById(R.id.listaInteresseUser);

        eventoDatabase();

        return rootView;
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void eventoDatabase() {
        listaInteresses.clear();
        Query query = mDatabaseReference.child("user").child(fbUser.getUid()).child("categorias");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Interesse interesse = snap.getValue(Interesse.class);
                        listaInteresses.add(interesse);
                    }
                if (getActivity() != null) {
                    adapter = new InteresseAdapter(getActivity(), listaInteresses);
                    listViewInteresses.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
