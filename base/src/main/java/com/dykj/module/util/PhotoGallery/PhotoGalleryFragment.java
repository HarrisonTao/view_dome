package com.dykj.module.util.PhotoGallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dykj.module.R;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by 小智
 * on 2017/12/25
 * 描述：
 */

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private static final String PHOTO_PARAM = "photo_param";
    private static final String LOCAL_PARAM = "local_param";
    private SubsamplingScaleImageView mPhoto;
    private String imageUrl;
    private View view;
    private String shareFileName;

    public PhotoGalleryFragment() {
    }

    public static PhotoGalleryFragment newInstance(String param, boolean isLocal) {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        Bundle args = new Bundle();
        args.putString("photo_param", param);
        args.putBoolean("local_param", isLocal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            this.imageUrl = this.getArguments().getString("photo_param");
//            this.isLocal = this.getArguments().getBoolean("local_param");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.createView(inflater, container);
    }

    private View createView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_photo_gallery_image, container, false);
        this.mPhoto = view.findViewById(R.id.fragment_photo_gallery_image_banner_image);
        //        this.mPhoto.setOnViewTapListener(this);

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        mPhoto.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(final View v) {
//                BCDialogUtil.showDialog(getActivity(), R.color.main_color,
//                                        "保存", "保存图片", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                saveBitmap(imageUrl);
//                            }
//                        }, null);
//                return true;
//            }
//        });
        RequestOptions options = new RequestOptions();
//        options.placeholder(R.mipmap.ic_launcher);
        Glide.with(this).load(imageUrl).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                BitmapDrawable bd = (BitmapDrawable) resource;
                Bitmap bm = bd.getBitmap();
                displayLongPic(bm);
            }
        });

        return view;
    }

    private void displayLongPic(Bitmap bmp) {
        mPhoto.setQuickScaleEnabled(true);
        mPhoto.setZoomEnabled(true);
        mPhoto.setPanEnabled(true);
        mPhoto.setDoubleTapZoomDuration(100);
        mPhoto.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        mPhoto.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        mPhoto.setImage(ImageSource.cachedBitmap(bmp), new ImageViewState(0, new PointF(0, 0), 0));
    }

    private void saveBitmap(final String imageUrl) {
        Glide.get(getActivity()).clearMemory();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(getActivity()).load(imageUrl).apply(requestOptions).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                saveImageToGallery(getActivity(), drawable2Bitmap(resource));
            }
        });
    }


    Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                               drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    public static String saveImageToGallery(Context context, Bitmap bmp) {
        Log.d(TAG, "saveImageToGallery: " + bmp);
        String imgpath = Environment.getExternalStorageDirectory().toString() + "/HeBeiNM/";
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory().toString(), "HeBeiNM");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
//        String fileName = System.currentTimeMillis() + "sc.jpg";
        String fileName = System.currentTimeMillis() + "";
        imgpath += fileName;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imgpath)));
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        return imgpath;
    }

}
