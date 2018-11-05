package com.example.xandi.amicer;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.Util;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotifsHomeFragment extends Fragment {

    private ArrayList<Group> groupList = new ArrayList<Group>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private ListView listaGrupos;
    private ArrayAdapter adapter;
    private FirebaseUser fbUser;
    private List<Interesse> listaCategoriasUser;
    private ArrayList<Group> groupListAux;
    private AlertDialog alerta;

    public NotifsHomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifs_home, container, false);

        listaGrupos = rootView.findViewById(R.id.listViewGruposSugeridos);
        startFirebase();
        mFirebaseAuth = FirebaseAuth.getInstance();
        fbUser = mFirebaseAuth.getCurrentUser();
        //eventoDatabase();
        readData(new MyCallbackGroup() {
            @Override
            public void onCallback(Group value) {

            }
        }, new MyCallbackInteresse() {
            @Override
            public void onCallback(Interesse value) {

            }
        });

        listaGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog(groupList.get(i).getUid(), groupList.get(i).getNome());
            }
        });

        return rootView;
    }

    private void filtrar() {

        //lista categorias usuário
        listaCategoriasUser = Util.getUser().getCategorias();
        //lista de grupos do usuário
        List<String> listaGruposUser = new ArrayList<String>();
        for (String key : Util.getUser().getListGroups().keySet()) {
            listaGruposUser.add(key);
        }
        groupListAux = new ArrayList<Group>();

        //Remove grupos que não são das categorias do usuário
        for (int i = 0; i < groupList.size(); i++) {
            boolean contem = false;
            for (int j = 0; j < listaCategoriasUser.size(); j++) {
                if (groupList.get(i).getCategoria().getCategoria().equals(listaCategoriasUser.get(j).getCategoria())) {
                    contem = true;
                }
            }
            if (!contem) {
                groupList.remove(groupList.get(i));
            }
        }

        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < listaCategoriasUser.size(); j++) {
                if (groupList.get(i).getCategoria().getCategoria().equals(listaCategoriasUser.get(j).getCategoria())) {
                    List<String> listaTagsGrupo = new ArrayList<String>();
                    List<String> listaTagsUser = new ArrayList<String>();
                    for (int k = 0; k < groupList.get(i).getCategoria().getTags().size(); k++) {
                        listaTagsGrupo.add(groupList.get(i).getCategoria().getTags().get(k).getLabel());
                    }
                    for (int l = 0; l < listaCategoriasUser.get(j).getTags().size(); l++) {
                        listaTagsUser.add(listaCategoriasUser.get(j).getTags().get(l).getLabel());
                    }
                    int cont = 0;
                    for (int m = 0; m < listaTagsGrupo.size(); m++) {
                        if (listaTagsUser.contains(listaTagsGrupo.get(m))) {
                            cont++;
                        }
                    }
                    if (!listaGruposUser.contains(groupList.get(i).getUid())) {
                        double distanciaKM = distance(groupList.get(i).getLocalizacao().getLatitude(), groupList.get(i).getLocalizacao().getLongitude(), Util.getUser().getLocalizacao().getLatitude(), Util.getUser().getLocalizacao().getLongitude(), 0, 0);

                        if (groupList.get(i).getCategoria().getDistanciaAtivada()) {
                            if (cont >= 3) {
                                if (distanciaKM <= groupList.get(i).getCategoria().getDistanciaMax()) {
                                    //Dispara Notificação
                                    HashMap<String, Boolean> mapUserGroups = new HashMap<>();
                                    mapUserGroups.put(groupList.get(i).getUid(), true);
                                    mDatabaseReference.child("user").child(fbUser.getUid()).child("listGroups").setValue(mapUserGroups);
                                }
                            } else {
                                if (cont >= 1) {
                                    if (distanciaKM <= groupList.get(i).getCategoria().getDistanciaMax()) {
                                        groupListAux.add(groupList.get(i));
                                    }
                                }
                            }
                        } else {
                            if (cont >= 3) {
                                //Dispara Notificação
                                HashMap<String, Boolean> mapUserGroups = new HashMap<>();
                                mapUserGroups = Util.getUser().getListGroups();
                                mapUserGroups.put(groupList.get(i).getUid(), true);
                                mDatabaseReference.child("user").child(fbUser.getUid()).child("listGroups").setValue(mapUserGroups);
                            } else {
                                if (cont >= 1) {
                                    groupListAux.add(groupList.get(i));
                                }
                            }
                        }

                    }
                }
            }
            if (getActivity() != null && !groupListAux.isEmpty()) {
                adapter = new GroupAdapter(getActivity(), groupListAux);
                listaGrupos.setAdapter(adapter);
            }
        }
        /*listaCategoriasUser.clear();
        groupList.clear();
        groupListAux.clear();*/
    }

    private double distance(double lat1, double lat2, double lon1, double lon2,
                            double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = deg2rad(lat2 - lat1);
        Double lonDistance = deg2rad(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    public void readData(MyCallbackGroup myCallbackGroup, MyCallbackInteresse myCallbackInteresse) {

        mDatabaseReference.child("group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnap) {
                for (DataSnapshot snap : dataSnap.getChildren()) {
                    Group group = snap.getValue(Group.class);
                    groupList.add(group);
                    myCallbackGroup.onCallback(group);
                }
                filtrar();
                /*for(int i=0; i<groupListAux.size(); i++){
                    mDatabaseReference.child("group").child(groupListAux.get(i).getUid()).child("categoria").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Interesse interesse = dataSnapshot.getValue(Interesse.class);
                            myCallbackInteresse.onCallback(interesse);
                            if(interesse!=null) {
                                int j = 0;
                                for (j=0; j < Util.getUser().getCategorias().size(); j++) {
                                    if (!listaUID.contains(groupListAux.get().getUid()) && interesse.getCategoria().equals(Util.getUser().getCategorias().get(j).getCategoria())) {
                                        groupList.add(groupListAux.get(j));
                                    }
                                }
                            }
                            if(i==Util.getUser().getCategorias().size()-1){
                                filtrar();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }*/
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void dialog(String uid, String nome) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //define o titulo
        builder.setTitle("Deseja ingressar no grupo?");
        //define a mensagem
        //builder.setMessage("Qualifique este software");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("uid", uid);
                intent.putExtra("nome", nome);
                intent.putExtra("userUid", fbUser.getUid());

                HashMap<String, Boolean> mapUserGroups = new HashMap<>();
                mapUserGroups = Util.getUser().getListGroups();
                mapUserGroups.put(uid, true);
                mDatabaseReference.child("user").child(fbUser.getUid()).child("listGroups").setValue(mapUserGroups);
                startActivity(intent);
                alerta.closeOptionsMenu();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.closeOptionsMenu();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
