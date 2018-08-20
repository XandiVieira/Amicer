package com.example.xandi.amicer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xandi.amicer.modelo.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

            if(mensagens.get(position).getUserUid().equals(userUid)){
                view = inflater.inflate(R.layout.item_message_sent, parent, false);
            }else{
                view = inflater.inflate(R.layout.item_message, parent, false);
            }

            ImageView photoImageView = (ImageView) view.findViewById(R.id.photoImageView);
            TextView messageTextView = (TextView) view.findViewById(R.id.messageTextView);
            TextView authorTextView = (TextView) view.findViewById(R.id.nameTextView);
            TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);

            boolean isPhoto = message.getPhotoUrl() != null;
            if (isPhoto) {
                messageTextView.setVisibility(View.GONE);
                photoImageView.setVisibility(View.VISIBLE);
                Glide.with(photoImageView.getContext())
                        .load(message.getPhotoUrl())
                        .into(photoImageView);
            } else {
                messageTextView.setVisibility(View.VISIBLE);
                photoImageView.setVisibility(View.GONE);
                messageTextView.setText(message.getText());
                timeTextView.setText(message.getTime());
            }
            authorTextView.setText(message.getName());

        }

        return view;
    }
}
