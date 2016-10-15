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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.bean.PhotoStory;
import com.example.paul.snapchatdemo.utils.HttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentMemories extends Fragment implements View.OnClickListener {
    private View root;
    private final String TAG = "Memories";

    private Button chooseFromAlbum;
    private Button deleteFromAlbum;
    private Button socialSharePhoto;
    private Button createstory;
    private Button camera;
    private ImageView picImageView;
    private String absolutePath;
    private Bitmap bitmap;
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
        initMemories();
    }


    public void initMemories(){
        camera = (Button) root.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        picImageView = (ImageView) root.findViewById(R.id.View);
        chooseFromAlbum = (Button) root.findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(this);
        deleteFromAlbum = (Button) root.findViewById(R.id.delete_from_album);
        deleteFromAlbum.setOnClickListener(this);
        socialSharePhoto = (Button)root.findViewById(R.id.social_share_photo);
        socialSharePhoto.setOnClickListener(this);
        createstory = (Button)root.findViewById(R.id.create_story);
        createstory.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_from_album:
                selectPicture();
                break;
            case R.id.delete_from_album:
                deleteImage();
                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(absolutePath))));
                picImageView.setImageResource(R.drawable.ic_photo_loading);
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
                /*createstory();*/

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

    public void createstory2(){
        }

    public void createstory(){
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
    }

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
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == getActivity().RESULT_OK) {//从相册选择照片不裁切
//            try {
//                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
//                System.out.println("Uri:" + selectedImage);
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
        System.out.println("bitmap:"+bitmap);
        picImageView.setImageBitmap(bitmap);
    }


    public void deleteImage() {
        File fdelete = new File(absolutePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + absolutePath);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + absolutePath);
            }
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
