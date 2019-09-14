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
import com.example.xandi.amicer.models.User;
import com.example.xandi.amicer.objects.Tag;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CardAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<User> elements;
    private List<ImageView> imageViewList = new ArrayList<>();
    private TextView compatibility;
    private TextView nameAndAge;
    private TextView distance;
    private TextView aboutName;
    private TextView aboutInfo;
    private TextView moreAbout;
    private ChipsInput chipsInput;
    private ChipsInput moreChipsInput;
    private List<Tag> tags = new ArrayList<>();
    private List<Tag> moreTags = new ArrayList<>();

    CardAdapter(@NonNull Context context, ArrayList<User> elements) {
        super(context, R.layout.item_card, elements);
        this.context = context;
        this.elements = elements;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_card, parent, false);

        User user = elements.get(position);

        imageViewList.add(rowView.findViewById(R.id.photo1));
        imageViewList.add(rowView.findViewById(R.id.photo2));
        imageViewList.add(rowView.findViewById(R.id.photo3));
        nameAndAge = rowView.findViewById(R.id.nameAndAge);
        distance = rowView.findViewById(R.id.distance);
        aboutName = rowView.findViewById(R.id.aboutName);
        aboutInfo = rowView.findViewById(R.id.aboutInfo);
        moreAbout = rowView.findViewById(R.id.moreAbout);

        for (int i=0; i<user.getPhotos().size(); i++){
            Glide.with(getApplicationContext()).load(user.getPhotos().get(i)).into(imageViewList.get(i));
        }
        nameAndAge.setText(user.getName() + ", " + user.getAge());
        aboutName.setText("Sobre" + user.getName());
        aboutInfo.setText(user.getDescription());
        for (int i=0; i<user.getInterests().get(0).getTags().size(); i++){
            tags.add(new Tag(user.getInterests().get(0).getTags().get(i).getLabel()));
            chipsInput.addChip(tags.get(i));
        }
        for (int i=0; i<user.getInterests().get(1).getTags().size(); i++){
            moreTags.add(new Tag(user.getInterests().get(1).getTags().get(i).getLabel()));
            moreChipsInput.addChip(moreTags.get(i));
        }

        return rowView;
    }
}
