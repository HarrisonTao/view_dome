package com.dykj.youfeng.start;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;


import com.dykj.youfeng.MainActivity;
import com.dykj.youfeng.R;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.util.GuideBannerLocationAdapter;

import butterknife.BindView;

/**
 * @author lcj
 */
public class GuidActivity extends BaseActivity {


    @BindView(R.id.guide_vp)
    ViewPager guideVp;
    @BindView(R.id.guideRg)
    RadioGroup guideRg;
    @BindView(R.id.guideTvTime)
    TextView guideTvTime;
    private boolean isStartTime;

    @Override
    public int intiLayout() {
        return R.layout.activity_guid;
    }

    @Override
    public void initData() {

    }

    @Override
    public void doBusiness() {
        showLocation();
    }

    //加载本地图片的情况
    private void showLocation() {
        final int[] list = {R.mipmap.page_boot1, R.mipmap.page_boot2, R.mipmap.page_boot3};
        guideVp.setAdapter(new GuideBannerLocationAdapter(this, list));
        guideVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.length - 1 && !isStartTime) {
                    isStartTime = true;
                    final int[] number = {3};
                    guideTvTime.setVisibility(View.VISIBLE);
                    guideTvTime.setOnClickListener(v -> {
                        number[0] = -1;
                        startAct(MainActivity.class);
                        finish();
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int number = (int) msg.obj;
                    if (number != 0) {
                        guideTvTime.setText(String.valueOf(number) + "s 跳过");
                    } else {
                        startAct(MainActivity.class);
                        finish();
                    }
                    break;
            }
        }
    };


}
