package com.example.xandi.amicer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.xandi.amicer.modelo.Group;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupsSeeFragment extends Fragment {

    private ArrayList<Group> groupList = new ArrayList<Group>();
    private ArrayAdapter<Group> arrayAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private ListView meusGrupos;
    private ArrayAdapter adapter;

    public GroupsSeeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_groups, container, false);

        meusGrupos = rootView.findViewById(R.id.meusGrupos);
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        startFirebase();
        eventoDatabase();

        meusGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("uid", groupList.get(i).getUid());
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void eventoDatabase() {
        mDatabaseReference.child("group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Group group = objSnapshot.getValue(Group.class);
                    groupList.add(group);
                }
                if (getActivity() != null) {
                    adapter = new GroupAdatper(getActivity(), groupList);
                    meusGrupos.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
