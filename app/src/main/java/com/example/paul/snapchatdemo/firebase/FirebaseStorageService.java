package com.example.paul.snapchatdemo.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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

    public static String uploadStoryImage(String filePath){
        // File or Blob
        Uri file = Uri.parse(filePath);

// Create the file metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
// Listen for state changes, errors, and completion of the upload.
        mStorageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                imageURLDownload=downloadUrl.toString();

            }
        });
        return imageURLDownload;

    }
}

