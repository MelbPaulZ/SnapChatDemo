package com.example.paul.snapchatdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.imageeditor.CanvasView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FragmentImageEditor extends Fragment {
    /**
     * Initialize fragment
     */
    private View root;
    LayoutInflater inflater;
    Bundle savedInstanceState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root= inflater.inflate(R.layout.fragment_image_editor, container, false);
        this.savedInstanceState = savedInstanceState;
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
    }

    public static String imageCanvasBackgroundPath = "/storage/emulated/0/WhatsApp/Media/WallPaper/Dandenong.jpg";

    /**
     * Initialize image editor
     */

    ViewGroup imageEditorRoot;
    CanvasView imageCanvas;
    EditText imageInputText;

    // board to pick emoji
    GridLayout emojiGrid;

    // list of image icon that being put on canvas
    Map<String, ImageView> imageIcons = new HashMap<String, ImageView>();

    // buttons
    ImageButton cancelEditImageButton;
    ImageButton emojiEditImageButton;
    ImageButton textEditImageButton;
    ImageButton handDrawEditImageButton;
    ImageButton undoDrawEditImageButton;
    ImageButton timerButton;
    ImageButton downloadImageButton;
    ImageButton addToStoryButton;
    ImageButton sendToFriendsButton;

    // set image timer
//    TextView timerText;
    // Spinner element
    Spinner timerSpinner;

    // temporary to track user's touch movement
    int _xDelta;
    int _yDelta;

    // to scale image icons
    float scale;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        timerText = (TextView) root.findViewById(R.id.timerText);
//        timerSpinner = (Spinner) root.findViewById(R.id.timerSpinner);
//        String [] values = {"1","2","3","4","5","6","7","8","9","10"};
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        timerSpinner.setAdapter(adapter);
//        int spinnerPosition = adapter.getPosition("3");
//        timerSpinner.setSelection(spinnerPosition);



        scale = getResources().getDisplayMetrics().density;

        // init the root layout
        imageEditorRoot = (ViewGroup) root.findViewById(R.id.imageEditorRoot);

        // init the emoji picker board
        initEmojiGrid();

        // init the image background canvas
        // text, image, and free hand drawing will be rendered on this image
        imageCanvas = (CanvasView) root.findViewById(R.id.imageCanvas);
        imageCanvas.setDrawingCacheEnabled(true);

        // TODO : still using static image
        Bitmap workingBitmap = BitmapFactory.decodeFile(imageCanvasBackgroundPath);
        BitmapDrawable ob = new BitmapDrawable(getResources(), workingBitmap);
        imageCanvas.setBackground(ob);

        // init input text
        imageInputText = (EditText) root.findViewById(R.id.imageInputText);
        imageInputText.setHorizontallyScrolling(false);
        imageInputText.setMaxLines(Integer.MAX_VALUE);

        imageInputText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.topMargin = Y - _yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                imageEditorRoot.invalidate();
                return false;
            }
        });

        imageInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // remove edittext when user delete all text in it
                    if(imageInputText.getText()==null) {
                        imageInputText.setVisibility(View.GONE);
                    }
                    else {
                        String inputText = imageInputText.getText().toString();
                        if (inputText.isEmpty()) {
                            imageInputText.setVisibility(View.GONE);
                        }
                    }

                    // remove focus from text
                    imageInputText.setFocusable(false);

                    // has to manually hide keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    handled = true;
                }
                return handled;
            }
        });

        // init buttons
        cancelEditImageButton = (ImageButton)root.findViewById(R.id.cancelEditImageButton);
        cancelEditImageButton.setOnClickListener(cancelEditImage());

        emojiEditImageButton = (ImageButton)root.findViewById(R.id.emojiEditImageButton);
        emojiEditImageButton.setOnClickListener(setEmojiMode());

        textEditImageButton = (ImageButton)root.findViewById(R.id.textEditImageButton);
        textEditImageButton.setOnClickListener(setTextEditMode());

        handDrawEditImageButton = (ImageButton)root.findViewById(R.id.handDrawEditImageButton);
        handDrawEditImageButton.setOnClickListener(setHandDrawMode());

        undoDrawEditImageButton = (ImageButton)root.findViewById(R.id.undoDrawEditImageButton);
        undoDrawEditImageButton.setOnClickListener(undoHandDraw());

        timerButton = (ImageButton)root.findViewById(R.id.timerButton);
        timerButton.setOnClickListener(showPickTimer());

        downloadImageButton = (ImageButton) root.findViewById(R.id.downloadImageButton);
        downloadImageButton.setOnClickListener(saveImage());

        addToStoryButton = (ImageButton) root.findViewById(R.id.addToStoryButton);
        addToStoryButton.setOnClickListener(sendImageToStory());

        sendToFriendsButton = (ImageButton) root.findViewById(R.id.sendToFriendsButton);
        sendToFriendsButton.setOnClickListener(sendImageToFriend());
    }

    private void initEmojiGrid() {
        // init emoji board
        emojiGrid = (GridLayout) root.findViewById(R.id.emojiGrid);
        int emojiMaxIdx = 17;
        int imageIconSize = (int)(60 * scale);

        for (int i=1; i<emojiMaxIdx; i++) {

            // create icon's ImageView
            ImageView imageIcon = new ImageView(root.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageIconSize, imageIconSize);
            imageIcon.setLayoutParams(layoutParams);

            // set the image
            String resourceName = "st_"+i;
            int drawableResourceId = this.getResources().getIdentifier(resourceName, "drawable", root.getContext().getPackageName());
            Picasso.with(root.getContext()).load(drawableResourceId).into(imageIcon);
            imageIcon.setTag(drawableResourceId);

            // add listener
            imageIcon.setOnClickListener(generateImageIcon());

            // put on the emoji board
            emojiGrid.addView(imageIcon);
        }
    }

    /**
     * put image icon on the canvas
     * @return
     */
    private View.OnClickListener generateImageIcon() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create icon's ImageView
                ImageView imageIcon = new ImageView(root.getContext());
                int imageIconSize = (int)(60 * scale);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(imageIconSize, imageIconSize);
                layoutParams.gravity = Gravity.CENTER;
                imageIcon.setLayoutParams(layoutParams);
                imageIcon.setVisibility(View.VISIBLE);

                // set the resource
                int imageIconId = (Integer)view.getTag();
                Picasso.with(root.getContext()).load(imageIconId).into(imageIcon);
                imageIcon.setTag(imageIconId);

                // add listener
                imageIcon.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                _xDelta = X - lParams.leftMargin;
                                _yDelta = Y - lParams.topMargin;
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = X - _xDelta;
                                layoutParams.topMargin = Y - _yDelta;
                                layoutParams.rightMargin = -250;
                                layoutParams.bottomMargin = -250;
                                view.setLayoutParams(layoutParams);
                                // TODO: detect delete region
//                                // user is deleting this image
//                                if (layoutParams.leftMargin==0 && layoutParams.leftMargin==0) {
//                                    imageEditorRoot.removeView(view);
//                                }

                                break;
                        }
                        imageEditorRoot.invalidate();
                        return true;
                    }
                });

                // add image icon to the list, so it can be rendered during download
                int id = View.generateViewId();
                imageIcon.setId(id);
                imageIcons.put(""+id,imageIcon);

                // add image icon to the canvas
                imageEditorRoot.addView(imageIcon);

                // hide the emoji board after puttin image on canvas
                disableEmojiMode();
            }
        };
    }

    private View.OnClickListener setHandDrawMode() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CanvasView content = (CanvasView)root.findViewById(R.id.imageCanvas);
                if (content.enableCanvas) {
                    disableHandDrawMode();
                }
                else {
                    // disable other mode
                    disableEmojiMode();
                    disableTextEditMode();

                    // enable hand draw mode
                    enableHandDrawMode();
                }
            }
        };
    }

    private View.OnClickListener undoHandDraw() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CanvasView content = (CanvasView)root.findViewById(R.id.imageCanvas);
                content.undo();
            }
        };
    }

    private void enableHandDrawMode() {
        CanvasView content = (CanvasView)root.findViewById(R.id.imageCanvas);
        content.enableCanvas = true;
        handDrawEditImageButton.setBackgroundColor(Color.RED);
        undoDrawEditImageButton.setVisibility(View.VISIBLE);
    }

    private void disableHandDrawMode() {
        CanvasView content = (CanvasView)root.findViewById(R.id.imageCanvas);
        content.enableCanvas = false;

        handDrawEditImageButton.setBackground(null);
        undoDrawEditImageButton.setVisibility(View.GONE);
    }

    private View.OnClickListener setEmojiMode() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView emojiGridLayout = (ScrollView) root.findViewById(R.id.emojiGridLayout);
                if (emojiGridLayout.getVisibility()==View.GONE) {
                    // disable other mode
                    disableHandDrawMode();
                    disableTextEditMode();

                    // enable emoji mode
                    enableEmojiMode();
                }
                else {
                    // disable emoji mode
                    disableEmojiMode();
                }
            }
        };
    }

    private void enableEmojiMode() {
        ScrollView emojiGridLayout = (ScrollView) root.findViewById(R.id.emojiGridLayout);
        emojiGridLayout.setVisibility(View.VISIBLE);
        emojiGridLayout.bringToFront();
        root.invalidate();
    }

    private void disableEmojiMode() {
        ScrollView emojiGridLayout = (ScrollView) root.findViewById(R.id.emojiGridLayout);
        emojiGridLayout.setVisibility(View.GONE);
    }

    private View.OnClickListener setTextEditMode() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageInputText.getVisibility()==View.GONE) {
                    imageInputText.setVisibility(View.VISIBLE);

                }
                else {
                    if (imageInputText.isFocusable()==false) {
                        // disable other mode
                        disableHandDrawMode();
                        disableEmojiMode();

                        // enable text edit mode
                        enableTextEditMode();
                    }
                    else {
                        // disable text edit mode
                        disableTextEditMode();
                    }
                }

            }
        };
    }

    private void enableTextEditMode() {
        imageInputText.setFocusableInTouchMode(true);
    }

    private void disableTextEditMode() {
        // remove focus from text
        imageInputText.setFocusable(false);

        // has to manually hide keyboard
        InputMethodManager imm = (InputMethodManager) imageInputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(imageInputText.getWindowToken(), 0);
    }

    private View.OnClickListener saveImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // draw components to canvas
                CanvasView content = (CanvasView)root.findViewById(R.id.imageCanvas);
                content.setDrawingCacheEnabled(true);
                Bitmap bitmap = content.getDrawingCache();
                bitmap = drawComponentsToCanvas(bitmap);

                // write to file
                String fileName = "/storage/emulated/0/Snapchat/"+UUID.randomUUID().toString()+".png";
                File file = new File(fileName);
                try
                {
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                // refresh the gallery folder
                scanFile(fileName);
            }
        };
    }

    /**
     * Refresh gallery
     *
     * @param path
     */
    private void scanFile(String path) {
        MediaScannerConnection.scanFile(root.getContext(), new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                }
        );
    }

    public Bitmap drawComponentsToCanvas(Bitmap bitmap) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // draw text tp canvas
        int[] text_loc = new int[2];
        imageInputText.getLocationOnScreen(text_loc);
        int text_loc_x = text_loc[0];
        int text_loc_y = text_loc[1];
        imageInputText.setCursorVisible(false);
        imageInputText.buildDrawingCache();
        Bitmap imageInputTextBitmap = Bitmap.createBitmap(imageInputText.getDrawingCache());
        canvas.drawBitmap(imageInputTextBitmap, text_loc_x, text_loc_y, paint);

        // draw icons to canvas
        int imageIconSize = (int)(60 * scale);
        for (String key : imageIcons.keySet()) {
            int id = Integer.parseInt(key);
            ImageView imageIcon = (ImageView)imageEditorRoot.findViewById(id);

            int[] img_loc = new int[2];
            imageIcon.getLocationOnScreen(img_loc);
            int img_loc_x = img_loc[0];
            int img_loc_y = img_loc[1];

            Bitmap imageDragBitmap = BitmapFactory.decodeResource(getResources(),(Integer)imageIcon.getTag());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageDragBitmap, imageIconSize, imageIconSize, false);

            canvas.drawBitmap(resizedBitmap, img_loc_x, img_loc_y, paint);
        }

        return bitmap;
    }

    private View.OnClickListener cancelEditImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: redirect to camera screen. ask paul
            }
        };
    }


    private View.OnClickListener showPickTimer() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: use spinner
//                timerText.setText("11");
            }
        };
    }

    private View.OnClickListener sendImageToStory() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: redirect to story fragment ? ask anita
            }
        };
    }

    private View.OnClickListener sendImageToFriend() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: redirect to pick friends. ask paul
            }
        };
    }
}