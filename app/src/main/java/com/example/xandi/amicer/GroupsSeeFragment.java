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
import android.widget.ListView;

import com.example.xandi.amicer.modelo.Group;
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

public class GroupsSeeFragment extends Fragment {

    private ArrayList<Group> groupList = new ArrayList<Group>();
    private ArrayList<String> itensInteresse = new ArrayList<String>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private ListView meusGrupos;
    private ArrayAdapter adapter;
    private FirebaseUser fbUser;

    public GroupsSeeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_groups, container, false);
        View itemView = inflater.inflate(R.layout.item_group, container, false);

        meusGrupos = rootView.findViewById(R.id.meusGrupos);
        startFirebase();
        mFirebaseAuth = FirebaseAuth.getInstance();
        fbUser = mFirebaseAuth.getCurrentUser();
        eventoDatabase();

        meusGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("uid", groupList.get(i).getUid());
                intent.putExtra("nome", groupList.get(i).getNome());
                intent.putExtra("userUid", fbUser.getUid());
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void eventoDatabase() {

        Query query = mDatabaseReference.child("user").child(fbUser.getUid()).child("listGroups");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    String groupUID = snap.getKey();
                    mDatabaseReference.child("group").child(groupUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnap) {
                            Group group = dataSnap.getValue(Group.class);
                            groupList.add(group);
                            /*if(group.getInteresses()!=null) {
                                for (int i = 0; i < group.getInteresses().size(); i++) {
                                    itensInteresse.add(group.getInteresses().get(i));
                                }
                            }*/
                            if (getActivity() != null) {
                                //adapter2 = new ItemAdapter(getActivity(), itensInteresse);
                                //listInteressesGroup.setAdapter(adapter2);
                                adapter = new GroupAdapter(getActivity(), groupList);
                                meusGrupos.setAdapter(adapter);
                                itensInteresse.clear();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
