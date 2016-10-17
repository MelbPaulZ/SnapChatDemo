package com.example.paul.snapchatdemo.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirebaseStorageService {

    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public static String uploadImage(String filePath) {
        final String[] imageURLDownload = new String[1];

        // filename on the server
        String fileName = UUID.randomUUID().toString();

        final StorageReference serverPhotoRef = mStorageRef.child("photos").child(fileName);
        Uri fileUri = Uri.parse(filePath);
        serverPhotoRef.putFile(fileUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadURL = taskSnapshot.getMetadata().getDownloadUrl();
                imageURLDownload[0] = downloadURL.toString();

            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        Thread thread=  new Thread(){
                            @Override
                            public void run(){
                                try {
                                    synchronized(this){
                                        wait(3000);
                                    }
                                }
                                catch(InterruptedException ex){
                                }
                            }
                        };

                        thread.start();
//
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        System.out.println("Upload is " + progress + "% done");
                    }
                })
//        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                new CountDownTimer(3000, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                        // do nothing
//                    }
//                    public void onFinish() {
//                        // do nothin
//                    }
//                }.start();
//            }
//        })
        ;

        return imageURLDownload[0];
    }
}

