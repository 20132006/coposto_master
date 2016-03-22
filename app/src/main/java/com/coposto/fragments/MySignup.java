package com.coposto.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.coposto.R;

/**
 * Created by netlab on 16. 1. 13.
 */
public class MySignup extends Fragment {
    public static final int RESULT_LOAD_IMAGE=1;
    EditText UploadProfilePic;
    EditText UploadPassportPic;
    int passport = 0;
    int profile = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        UploadProfilePic = (EditText) v.findViewById(R.id.profile_pic);
        UploadPassportPic = (EditText) v.findViewById(R.id.passport_pic);
        UploadProfilePic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                profile = 1;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        UploadPassportPic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passport = 1;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && requestCode == 1 && data != null && passport == 1){
            Uri selectedImage = data.getData();
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.icon);
            imageView.setImageURI(selectedImage);
            passport = 0;
        }
        else if (requestCode == RESULT_LOAD_IMAGE && requestCode == 1 && data != null && profile == 1){
            Uri selectedImage = data.getData();
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.icon);
            imageView.setImageURI(selectedImage);
            profile = 0;
        }
    }
}
