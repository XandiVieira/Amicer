package com.example.xandi.amicer.objects;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

public class Tag implements ChipInterface {

    String label;

    public Tag(){}

    public Tag(String label) {
        this.label = label;
    }

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getInfo() {
        return null;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
