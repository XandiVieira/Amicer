package com.example.xandi.amicer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xandi.amicer.R;
import com.example.xandi.amicer.models.Chat;
import com.example.xandi.amicer.models.User;
import com.example.xandi.amicer.models.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Chat> elementos;

    ChatAdapter(@NonNull Context context, ArrayList<Chat> elementos) {
        super(context, R.layout.item_chat, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_chat, parent, false);

        TextView name = rowView.findViewById(R.id.name);
        TextView date = rowView.findViewById(R.id.date);
        TextView lastMessage = rowView.findViewById(R.id.lastMessages);
        ImageView photo = rowView.findViewById(R.id.friendPhoto);

        User friend = null;
        for (int i=0; i<elementos.get(position).getParticipants().size(); i++){
            if(!elementos.get(position).getParticipants().get(i).equals(Util.getUser())){
                friend = elementos.get(position).getParticipants().get(i);
            }
        }

        Date lastMessageDate = new Date(elementos.get(position).getTimestamp());
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

        name.setText(friend.getName());
        date.setText(formatDate.format(lastMessageDate));
        lastMessage.setText(elementos.get(position).getLastMessage());
        Glide.with(getApplicationContext()).load(friend.getPhotos().get(0)).into(photo);

        return rowView;
    }
}
