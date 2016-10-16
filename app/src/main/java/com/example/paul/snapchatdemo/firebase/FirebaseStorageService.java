package com.example.paul.snapchatdemo.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirebaseStorageService {

    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private static String imageURLDownload;

    public static String uploadImage(String filePath) {

        // filename on the server
        String fileName = UUID.randomUUID().toString();

        final StorageReference serverPhotoRef = mStorageRef.child("photos").child(fileName);
        Uri fileUri = Uri.parse(filePath);
        serverPhotoRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadURL = taskSnapshot.getMetadata().getDownloadUrl();
                imageURLDownload = downloadURL.toString();

            }
        });

        return imageURLDownload;
    }
}

