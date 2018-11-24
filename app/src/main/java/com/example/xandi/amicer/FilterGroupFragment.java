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

import com.example.xandi.amicer.modelo.Group;
import com.example.xandi.amicer.modelo.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilterGroupFragment extends Fragment {

    private ArrayList<Group> listaInteresses = new ArrayList<Group>();
    private ArrayAdapter adapter;
    private ListView listViewInteresseGroup;

    public FilterGroupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filter_group, container, false);
        listViewInteresseGroup = rootView.findViewById(R.id.listaInteressesGroup);

        Query query = Util.mDatabaseRef.child("user").child(Util.fbUser.getUid()).child("listGroups");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaInteresses.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    String groupUID = snap.getKey();
                    Util.mDatabaseRef.child("group").child(groupUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnap) {
                            Group group = dataSnap.getValue(Group.class);
                            if(group!=null && group.getUserUID()!=null) {
                                if (group.getUserUID().equals(Util.getUser().getUid())) {
                                    listaInteresses.add(group);
                                }
                            }
                            if (getActivity() != null && !listaInteresses.isEmpty()) {
                                adapter = new InteresseGroupAdapter(getActivity(), listaInteresses);
                                listViewInteresseGroup.setAdapter(adapter);
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

        return rootView;
    }

    private void eventoDatabase() {

    }
}
