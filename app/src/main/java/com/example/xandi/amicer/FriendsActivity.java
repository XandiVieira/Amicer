package com.example.xandi.amicer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.xandi.amicer.objects.Tag;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private ImageButton tabProfile;
    private ImageButton tabHome;
    private List<ChipsInput> inputChips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        List<Tag> contactList = new ArrayList<>();
        contactList.add(new Tag("rudinei"));
        contactList.add(new Tag("armando"));

// pass the ContactChip list
        inputChips = new ArrayList<>();
        inputChips.add(findViewById(R.id.chipsInput1));
        inputChips.add(findViewById(R.id.chipsInput2));
        inputChips.get(0).setFilterableList(contactList);
        inputChips.get(1).setFilterableList(contactList);

        /*tabProfile = findViewById(R.id.profileTab);
        tabHome = findViewById(R.id.homeTab);

        tabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
