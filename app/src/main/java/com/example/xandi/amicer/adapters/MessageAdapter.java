package com.example.xandi.amicer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xandi.amicer.R;
import com.example.xandi.amicer.models.Message;
import com.example.xandi.amicer.models.Util;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private ArrayList<Message> mensagens;
    private String userUid;

    public MessageAdapter(Context c, ArrayList<Message> objects, String userUid) {
        super(c, 0, objects);

        this.context = c;
        this.mensagens = objects;
        this.userUid = userUid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (mensagens != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Message message = mensagens.get(position);

            if(mensagens.get(position).getSenderUid().equals(Util.getUser().getUid())){
                view = inflater.inflate(R.layout.item_message_sent, parent, false);
            }else{
                view = inflater.inflate(R.layout.item_message_received, parent, false);
            }

            TextView messageTextView = (TextView) view.findViewById(R.id.messageTextView);
            TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        }

        return view;
    }
}
