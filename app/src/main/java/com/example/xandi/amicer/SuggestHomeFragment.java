package com.example.xandi.amicer;

import android.app.AlertDialog;
import android.content.Intent;
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

public class SuggestHomeFragment extends Fragment {

    private ArrayList<Group> groupList = new ArrayList<>();

    private DatabaseReference mDatabaseReference;
    private ListView listaGrupos;
    private FirebaseUser fbUser;
    private ArrayList<Group> groupListAux;
    private AlertDialog alerta;

    public SuggestHomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suggest_home, container, false);

        listaGrupos = rootView.findViewById(R.id.listViewGruposSugeridos);
        startFirebase();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        fbUser = mFirebaseAuth.getCurrentUser();
        readData();
        listaGrupos.setOnItemClickListener((adapterView, view, i, l) -> dialog(groupListAux.get(i).getUid(), groupListAux.get(i).getNome(), groupListAux.get(i).getUserUID()));

        return rootView;
    }

    private void filtrar() {

        //lista categorias usuário
        List<Interesse> listaCategoriasUser = Util.getUser().getCategorias();
        //lista de grupos do usuário
        List<String> listaGruposUser = new ArrayList<>(Util.getUser().getListGroups().keySet());
        groupListAux = new ArrayList<>();

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
                    List<String> listaTagsGrupo = new ArrayList<>();
                    List<String> listaTagsUser = new ArrayList<>();
                    for (int k = 0; k < groupList.get(i).getCategoria().getTags().size(); k++) {
                        listaTagsGrupo.add(groupList.get(i).getCategoria().getTags().get(k).getLabel());
                    }
                    for (int l = 0; l < listaCategoriasUser.get(j).getTags().size(); l++) {
                        listaTagsUser.add(listaCategoriasUser.get(j).getTags().get(l).getLabel());
                    }
                    int cont = 0;
                    for (int m = 0; m < listaTagsUser.size(); m++) {
                        for(int n=0; n<listaTagsGrupo.size(); n++){
                            try {
                                int similarity = (int) checkSimilarity(listaTagsUser.get(m), listaTagsGrupo.get(n));
                                if (similarity > 75) {
                                    cont++;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!listaGruposUser.contains(groupList.get(i).getUid())) {
                        double distanciaKM = distance(groupList.get(i).getLocalizacao().getLatitude(), groupList.get(i).getLocalizacao().getLongitude(),
                                Util.getUser().getLocalizacao().getLatitude(), Util.getUser().getLocalizacao().getLongitude());

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
                                HashMap<String, Boolean> mapUserGroups;
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
        }
        if (getActivity() != null && !groupListAux.isEmpty()) {
            ArrayAdapter adapter = new GroupAdapter(getActivity(), groupListAux);
            listaGrupos.setAdapter(adapter);
        }
    }

    protected static float checkSimilarity(String sString1, String sString2) throws Exception {

        // Se as strings têm tamanho distinto, obtêm a similaridade de todas as
        // combinações em que tantos caracteres quanto a diferença entre elas são
        // inseridos na string de menor tamanho. Retorna a similaridade máxima
        // entre todas as combinações, descontando um percentual que representa
        // a diferença em número de caracteres.
        sString1 = sString1.toUpperCase();
        sString2 = sString2.toUpperCase();
        if(sString1.length() != sString2.length()) {
            int iDiff = Math.abs(sString1.length() - sString2.length());
            int iLen = Math.max(sString1.length(), sString2.length());
            String sBigger, sSmaller, sAux;

            if(iLen == sString1.length()) {
                sBigger = sString1;
                sSmaller = sString2;
            }
            else {
                sBigger = sString2;
                sSmaller = sString1;
            }

            float fSim, fMaxSimilarity = Float.MIN_VALUE;
            for(int i = 0; i <= sSmaller.length(); i++) {
                sAux = sSmaller.substring(0, i) + sBigger.substring(i, i+iDiff) + sSmaller.substring(i);
                fSim = checkSimilaritySameSize(sBigger,  sAux);
                if(fSim > fMaxSimilarity)
                    fMaxSimilarity = fSim;
            }
            return (fMaxSimilarity - (1f * iDiff) / iLen);

            // Se as strings têm o mesmo tamanho, simplesmente compara-as caractere
            // a caractere. A similaridade advém das diferenças em cada posição.
        } else
            return checkSimilaritySameSize(sString1, sString2);
    }

    protected static float checkSimilaritySameSize(String sString1, String sString2) throws Exception {

        if(sString1.length() != sString2.length())
            throw new Exception("Strings devem ter o mesmo tamanho!");

        int iLen = sString1.length();
        int iDiffs = 0;

        // Conta as diferenças entre as strings
        for(int i = 0; i < iLen; i++)
            if(sString1.charAt(i) != sString2.charAt(i))
                iDiffs++;

        // Calcula um percentual entre 0 e 1, sendo 0 completamente diferente e
        // 1 completamente igual
        return (1f - (float) iDiffs / iLen) * 100;
    }

    private double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = deg2rad(lat2 - lat1);
        Double lonDistance = deg2rad(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = (double) 0 - (double) 0;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    public void readData() {

        mDatabaseReference.child("group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                for (DataSnapshot snap : dataSnap.getChildren()) {
                    Group group = snap.getValue(Group.class);
                    groupList.add(group);
                }
                filtrar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialog(String grupoUID, String nome, String userUID) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //define o titulo
        builder.setTitle("Deseja ingressar no grupo?");
        //define a mensagem
        //builder.setMessage("Qualifique este software");
        //define um botão como positivo
        builder.setPositiveButton("Sim", (arg0, arg1) -> {

            Intent intent = new Intent(getActivity(), InsideGroup.class);
            intent.putExtra("uid", grupoUID);
            intent.putExtra("nome", nome);
            intent.putExtra("userUid", userUID);

            HashMap<String, Boolean> mapUserGroups;
            mapUserGroups = Util.getUser().getListGroups();
            mapUserGroups.put(grupoUID, true);
            mDatabaseReference.child("user").child(fbUser.getUid()).child("listGroups").setValue(mapUserGroups);
            startActivity(intent);
            alerta.closeOptionsMenu();
        });
        //define um botão como negativo.
        builder.setNegativeButton("Cancelar", (arg0, arg1) -> alerta.closeOptionsMenu());
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}