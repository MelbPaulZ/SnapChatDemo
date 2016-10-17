package com.example.paul.snapchatdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.ChatApi;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.bean.PhotoStory;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.chat.Token;
import com.example.paul.snapchatdemo.firebase.FirebaseStorageService;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.example.paul.snapchatdemo.utils.UserUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentMemories extends Fragment implements View.OnClickListener {
    private View root;
    private final String TAG = "Memories";

    private ImageButton chooseFromAlbum;
    private ImageButton deleteFromAlbum;
    private ImageButton socialSharePhoto;
    private ImageButton createstory;
    private ImageButton lockPhoto;
    private ImageButton camera;
    private ImageView picImageView;
    private String absolutePath;
    private Bitmap bitmap;
    private StorageReference mStorageRef;
    private String downloadUrl;
    public static final int PICK_PHOTO = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_memories, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        initMemories();
    }


    public void initMemories(){
        camera = (ImageButton) root.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        picImageView = (ImageView) root.findViewById(R.id.View);
        chooseFromAlbum = (ImageButton) root.findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(this);
        deleteFromAlbum = (ImageButton) root.findViewById(R.id.delete_from_album);
        deleteFromAlbum.setOnClickListener(this);
        socialSharePhoto = (ImageButton)root.findViewById(R.id.social_share_photo);
        socialSharePhoto.setOnClickListener(this);
        createstory = (ImageButton)root.findViewById(R.id.create_story);
        createstory.setOnClickListener(this);
        lockPhoto = (ImageButton)root.findViewById(R.id.lock_photo);
        lockPhoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_from_album:
                selectPicture();
                break;
            case R.id.delete_from_album:
                deleteImage();
                picImageView.setImageResource(R.drawable.ic_loading);
                break;
            case R.id.social_share_photo:
                Intent email = new Intent(android.content.Intent.ACTION_SEND);
                if (absolutePath != null) {//absolutePath 为图片的本地路径
                    email.setType("image/jpeg");
                    File file = new File(absolutePath);
                    Uri outputFileUri = Uri.fromFile(file);
                    email.putExtra(Intent.EXTRA_STREAM, outputFileUri);
                }
                email.setType("plain/text");
                email.putExtra(Intent.EXTRA_SUBJECT, "photo from snapchat");//邮件主题
                email.putExtra(Intent.EXTRA_TEXT, "photo sent by "+((MainActivity)getActivity()).getUsername());//邮件内容
                startActivity(email);
                break;
            case R.id.create_story:

                //String imageUrl=uploadImage();
                //((MainActivity)getActivity()).setImageUrl(imageUrl);
                uploadImage2();
                //((MainActivity)getActivity()).setAbsolutePath(absolutePath);
                ((MainActivity)getActivity()).fromMemoryToCreateStory();
                //createstory(imageUrl);

                break;
            case R.id.lock_photo:
                ((MainActivity)getActivity()).setAbsolutePath(absolutePath);
                //uploadImage2();
                ((MainActivity)getActivity()).fromMemoryToLockphoto();
                break;

            case R.id.camera:
                fromMainToCamera();
            default:
                break;
        }
    }

    private void fromMainToCamera(){
        ((MainActivity)getActivity()).fromMainToCamera();
    }

    public void createstory(String uploadImg){
        if(uploadImg==null){
            uploadImg="http://seeklogo.com/images/S/snapchat-ghost-logo-B618EE0704-seeklogo.com.png";
        }
        System.out.println("uploadImg:"+uploadImg);
        String id= ((MainActivity)getActivity()).getUserId();
        // get remote service
        UserApi userApi = HttpUtil.accessServer(UserApi.class);
        String isSecret="0";
        String story_text="Look at my story!";

        // this is for getting data back, asynchronous doing this task
        userApi.createstory(id, uploadImg, isSecret,story_text, C.methods.METHOD_CREATESTORY).enqueue(new Callback<ArrayList<PhotoStory>>() {
            @Override
            public void onResponse(Call<ArrayList<PhotoStory>> call, Response<ArrayList<PhotoStory>> response) {
                Log.i(TAG, "onResponse: " + response.body().toString());
                Toast.makeText(FragmentMemories.this.getActivity().getBaseContext(),
                        "create story successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ArrayList<PhotoStory>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + "userApi failure");
                Toast.makeText(FragmentMemories.this.getActivity().getBaseContext(),
                        "create story failed", Toast.LENGTH_SHORT).show();
            }
        });
        }

    public void uploadImage2(){
        String pathPrefix = "file://";
        String localImageFile=pathPrefix+absolutePath;
        String fileName = UUID.randomUUID().toString();
        final StorageReference serverPhotoRef = mStorageRef.child("photos").child(fileName);
        Uri fileUri = Uri.parse(localImageFile);
        serverPhotoRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            downloadUrl=downloadUri.toString();
                            System.out.println("Test downloadUrl:"+downloadUrl);
                            ((MainActivity)getActivity()).setImageUrl(downloadUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });

    }

    public String uploadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerToken();
            }
        }).start();
        String pathPrefix="file://";
        String localImageFile=pathPrefix+absolutePath;
        //String saveImgPath= "/storage/emulated/0/DCIM/100ANDRO/1.JPG";
        //boolean flag=compressBiamp(bitmap,saveImgPath,30);
        //String ImageFile=pathPrefix+saveImgPath;
        String urlDownload = FirebaseStorageService.uploadImage(localImageFile);
/*        while(urlDownload==null){
            urlDownload=FirebaseStorageService.uploadImage(localImageFile);
            if(urlDownload!=null){
                break;
            }
        }*/
        System.out.println("urlDownloadMemories:"+urlDownload);
        return urlDownload;
    }

    public void registerToken() {
        String token = Token.generateToken();
        Token.registerToken(token, UserUtil.getId());
    }

    /*public void createstory(){
        String myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
        System.out.println("myBase64Image.length():"+myBase64Image.length());
        String test="123456";
        String saveImgPath= "/storage/emulated/0/DCIM/100ANDRO/1.JPG";
        boolean flag=compressBiamp(bitmap,saveImgPath,100);
        System.out.println("flag "+flag);
        File uploadImg=new File(absolutePath);
        System.out.println("File"+uploadImg);

        //System.out.println("myBase64Image:"+myBase64Image);
        //Bitmap myBitmapAgain = decodeBase64(myBase64Image);
        //System.out.println("myBitmapAgain:"+myBitmapAgain);
        //picImageView.setImageBitmap(myBitmapAgain);
        String userid= ((MainActivity)getActivity()).getUserId();
                // get remote service
                UserApi userApi = HttpUtil.accessServer(UserApi.class);

                // this is for getting data back, asynchronous doing this task
                userApi.createstory(userid, uploadImg, C.methods.METHOD_CREATESTORY).enqueue(new Callback<ArrayList<PhotoStory>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PhotoStory>> call, Response<ArrayList<PhotoStory>> response) {
                        Log.i(TAG, "onResponse: " + response.body().toString());
                        Toast.makeText(FragmentMemories.this.getActivity().getBaseContext(),
                                "create story successfully", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<PhotoStory>> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + "userApi failure");
                        Toast.makeText(FragmentMemories.this.getActivity().getBaseContext(),
                                "create story failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    public static boolean compressBiamp(Bitmap bitmap, String compressPath, int quality) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(compressPath));

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);// (0-100)压缩文件

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private void selectPicture() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
        getActivity().startActivityForResult(intent, PICK_PHOTO);
    }

//    @Override
//    public void ActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == getActivity().RESULT_OK) {//从相册选择照片不裁切
//            try {
//                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
//                System.out.println("Uri:" + selectedImage);RR
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContext().getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                absolutePath = cursor.getString(columnIndex);  //获取照片路径
//                System.out.println("String:" + absolutePath);
//                cursor.close();
//                //Bitmap bitmap= BitmapFactory.decodeFile(absolutePath);
//                bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage));
//                System.out.println("bitmap:"+bitmap);
//                picImageView.setImageBitmap(bitmap);
//
//
//            } catch (Exception e) {
//                // TODO Auto-generatedcatch block
//                e.printStackTrace();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    public void getPhoto(Intent data){
        if(data!=null) {
            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
            System.out.println("Uri:" + selectedImage);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            absolutePath = cursor.getString(columnIndex);  //获取照片路径
            System.out.println("String:" + absolutePath);
            cursor.close();
            //Bitmap bitmap= BitmapFactory.decodeFile(absolutePath);
            try {
                bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("bitmap:" + bitmap);
            picImageView.setImageBitmap(bitmap);
        }
    }


    public void deleteImage() {
        if(absolutePath!=null) {
            File fdelete = new File(absolutePath);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Log.e("-->", "file Deleted :" + absolutePath);
                    callBroadCast();
                } else {
                    Log.e("-->", "file not Deleted :" + absolutePath);
                }
            }
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(absolutePath))));
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(getContext(), new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
