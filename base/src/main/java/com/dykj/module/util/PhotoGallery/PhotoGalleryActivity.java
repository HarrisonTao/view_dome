package com.dykj.module.util.PhotoGallery;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dykj.module.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoGalleryActivity extends FragmentActivity {
    private PhotoGalleryViewPager mViewPager;
    private TextView tvCount;
    private List<String> mImageList = new ArrayList();
    private PhotoGalleryData photoGalleryData;

    public PhotoGalleryActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_photo_gallery);
        initView();
    }

    private void initView() {
        photoGalleryData = getIntent().getParcelableExtra("Data");
        if (this.photoGalleryData.imageList != null && !this.photoGalleryData.imageList.isEmpty()) {
            this.mImageList = this.photoGalleryData.imageList;
        } else {
            if (this.photoGalleryData.images == null) {
                return;
            }

            this.mImageList = Arrays.asList(this.photoGalleryData.images);

        }
        this.mViewPager = this.findViewById(R.id.photo_gallery_image_banner_pager);
        this.tvCount = this.findViewById(R.id.photo_gallery_image_banner_tv_count);
        this.mViewPager.setAdapter(new ImageViewPagerAdapter(this.getSupportFragmentManager()));
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                PhotoGalleryActivity.this.tvCount.setText(position + 1 + "/" + PhotoGalleryActivity.this.mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        this.mViewPager.setCurrentItem(this.photoGalleryData.index);
        this.tvCount.setText(this.photoGalleryData.index + 1 + "/" + this.mImageList.size());
    }

    private class ImageViewPagerAdapter extends FragmentPagerAdapter {
        ImageViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String path = (String) PhotoGalleryActivity.this.mImageList.get(position);
            return PhotoGalleryFragment.newInstance(path, PhotoGalleryActivity.this.photoGalleryData.isLocal);
        }

        @Override
        public int getCount() {
            return PhotoGalleryActivity.this.mImageList.size();
        }
    }
}
