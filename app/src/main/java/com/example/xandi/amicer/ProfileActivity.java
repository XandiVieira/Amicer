package com.example.xandi.amicer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.xandi.amicer.models.Interest;
import com.example.xandi.amicer.models.User;
import com.example.xandi.amicer.models.Util;
import com.example.xandi.amicer.objects.Tag;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private StorageReference gsReference;
    private List<ChipsInput> inputChips;
    private List<Tag> tagsSuggestions;
    private ArrayAdapter adapter;
    private ImageButton close1;
    private ImageButton close2;
    private ImageButton close3;
    private ImageButton add1;
    private ImageButton add2;
    private ImageButton add3;
    private List<ImageView> photoList = new ArrayList<>();
    private TextView caracCounter;
    private ImageView imageView;
    private int imageViewIndex;
    private EditText descrInput;
    private List<Spinner> spinnerList;
    private List<String> photoPathList = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        close1 = findViewById(R.id.close1);
        close2 = findViewById(R.id.close2);
        close3 = findViewById(R.id.close3);
        add1 = findViewById(R.id.add1);
        add2 = findViewById(R.id.add2);
        add3 = findViewById(R.id.add3);
        photoList.add(findViewById(R.id.photo1));
        photoList.add(findViewById(R.id.photo2));
        photoList.add(findViewById(R.id.photo3));
        descrInput = findViewById(R.id.description);
        caracCounter = findViewById(R.id.caracCounter);
        Spinner categories1 = findViewById(R.id.categories1);
        Spinner categories2 = findViewById(R.id.categories2);
        inputChips = new ArrayList<>();
        spinnerList = new ArrayList<>();
        inputChips.add(findViewById(R.id.chipsInput1));
        inputChips.add(findViewById(R.id.chipsInput2));
        spinnerList.add(findViewById(R.id.categories1));
        spinnerList.add(findViewById(R.id.categories2));
        ImageButton back = findViewById(R.id.btUpdate);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        gsReference = storage.getReferenceFromUrl("gs://amicer-d193a.appspot.com/images/");

        photoPathList.add("");
        photoPathList.add("");
        photoPathList.add("");

        user = Util.getUser();

        if (user.getPhotos() != null) {
            for (int i = 0; i < user.getPhotos().size(); i++) {
                photoPathList.set(i, user.getPhotos().get(i));
            }
        }

        back.setOnClickListener(view -> updateProfile());

        TypedArray categoriesRes = getResources().obtainTypedArray(R.array.categories);
        String[] categories = new String[categoriesRes.length()];
        for (int i = 0; i < categoriesRes.length(); i++) {
            categories[i] = categoriesRes.getString(i);
        }
        categoriesRes.recycle();

        tagsSuggestions = new ArrayList<>();
        Util.getmTagsDatabaseRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    tagsSuggestions.add(snap.getValue(Tag.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < inputChips.size(); i++) {
            inputChips.get(i).setFilterableList(tagsSuggestions);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);
        // Apply the adapter to the spinner
        categories1.setAdapter(adapter);
        categories2.setAdapter(adapter);

        setUserProfile();

        descrInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                caracCounter.setText(String.valueOf(500 - descrInput.getText().length()));
            }
        });

        close1.setOnClickListener(view -> {
            photoList.get(0).setImageBitmap(null);
            add1.setVisibility(View.VISIBLE);
            close1.setVisibility(View.GONE);
        });

        close2.setOnClickListener(view -> {
            photoList.get(1).setImageBitmap(null);
            add2.setVisibility(View.VISIBLE);
            close2.setVisibility(View.GONE);
        });

        close3.setOnClickListener(view -> {
            photoList.get(2).setImageBitmap(null);
            add3.setVisibility(View.VISIBLE);
            close3.setVisibility(View.GONE);
        });

        photoList.get(0).setOnClickListener(view -> {
            chooseImage(photoList.get(0));
            close1.setVisibility(View.VISIBLE);
            add1.setVisibility(View.GONE);
            imageViewIndex = 0;
        });

        photoList.get(1).setOnClickListener(view -> {
            chooseImage(photoList.get(1));
            close2.setVisibility(View.VISIBLE);
            add2.setVisibility(View.GONE);
            imageViewIndex = 1;
        });

        photoList.get(2).setOnClickListener(view -> {
            chooseImage(photoList.get(2));
            close3.setVisibility(View.VISIBLE);
            add3.setVisibility(View.GONE);
            imageViewIndex = 2;
        });

        add1.setOnClickListener(view -> {
            chooseImage(photoList.get(0));
            close1.setVisibility(View.VISIBLE);
            add1.setVisibility(View.GONE);
            imageViewIndex = 0;
        });

        add2.setOnClickListener(view -> {
            chooseImage(photoList.get(1));
            close2.setVisibility(View.VISIBLE);
            add2.setVisibility(View.GONE);
            imageViewIndex = 1;
        });

        add3.setOnClickListener(view -> {
            chooseImage(photoList.get(2));
            close3.setVisibility(View.VISIBLE);
            add3.setVisibility(View.GONE);
            imageViewIndex = 2;
        });

        //Handle input chips
        for (int i = 0; i < inputChips.size(); i++) {
            final int finalI = i;
            inputChips.get(i).addChipsListener(new ChipsInput.ChipsListener() {
                @Override
                public void onChipAdded(ChipInterface chip, int newSize) {
                }

                @Override
                public void onChipRemoved(ChipInterface chip, int newSize) {
                }

                @Override
                public void onTextChanged(CharSequence text) {
                    if (text.length() > 0) {
                        if (text.charAt(text.length() - 1) == ' ') {
                            String texto = text.toString();
                            if (!texto.trim().isEmpty())
                                inputChips.get(finalI).addChip(texto.trim(), null);
                        }
                    }
                }
            });
        }
    }

    private void setUserProfile() {
        descrInput.setText(user.getDescription());

        if(user.getPhotos() != null){
            for (int i=0; i<user.getPhotos().size(); i++){

                int finalI = i;
                gsReference.child(user.getPhotos().get(i)).getDownloadUrl().addOnSuccessListener(uri -> Glide.with(getApplicationContext())
                        .load(uri)
                        .into(photoList.get(finalI)));
            }
        }

        if (user.getInterests() != null && user.getInterests().size() > 0 && user.getInterests().get(0).getTags() != null) {
            for (int i = 0; i < user.getInterests().get(0).getTags().size(); i++) {
                inputChips.get(0).addChip(user.getInterests().get(0).getTags().get(i));
            }
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, spinnerList.get(0));
            spinnerList.get(0).setSelection(adapter.getPosition(user.getInterests().get(0).getCategory()));
        }
        if (user.getInterests() != null && user.getInterests().size() > 1 && user.getInterests().get(1).getTags() != null) {
            for (int i = 0; i < user.getInterests().get(1).getTags().size(); i++) {
                inputChips.get(1).addChip(user.getInterests().get(1).getTags().get(i));
            }
        }
    }

    private void chooseImage(ImageView photo) {
        imageView = photo;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Glide.with(this)
                    .load(data.getData())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                    .into(imageView);

            photoPathList.set(imageViewIndex, String.valueOf(data.getData()));
        }
    }

    private void updateProfile() {

        handleInputChips();
        user.setPhotos(photoPathList);
        user.setDescription(descrInput.getText().toString());

        for (int i = 0; i < user.getPhotos().size(); i++) {
            if (!user.getPhotos().get(i).equals("")) {
                uploadImage(user.getPhotos().get(i), i);
            }
        }

        user.setPhotos(photoPathList);
        Util.getmUserDatabaseRef().child(user.getUid()).setValue(user);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void handleInputChips() {
        List<Interest> interests = new ArrayList<>();
        interests.add(new Interest());
        interests.add(new Interest());
        if (!spinnerList.get(0).getSelectedItem().equals(spinnerList.get(0).getItemAtPosition(0)) && inputChips.get(0).getSelectedChipList().size() > 0) {
            List<Tag> tags = (List<Tag>) inputChips.get(0).getSelectedChipList();
            Interest interest = new Interest();
            interest.setCategory(spinnerList.get(0).getSelectedItem().toString());
            interest.setTags(tags);
            interests.set(0, interest);
        }
        if (!spinnerList.get(1).getSelectedItem().equals(spinnerList.get(1).getItemAtPosition(0)) && inputChips.get(1).getSelectedChipList().size() > 0) {
            List<Tag> tags = (List<Tag>) inputChips.get(1).getSelectedChipList();
            Interest interest = new Interest();
            interest.setCategory(spinnerList.get(1).getSelectedItem().toString());
            interest.setTags(tags);
            interests.set(1, interest);
        }
        user.setInterests(interests);
    }

    private void uploadImage(String filePath, int i) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String name = UUID.randomUUID().toString();
            StorageReference ref = gsReference.child(name);
            photoPathList.set(i, name);
            ref.putFile(Uri.parse(filePath))
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //logout the user from application
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    //Go to login activity
    public void goLoginScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
