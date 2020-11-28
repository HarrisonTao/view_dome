package com.dykj.youfeng.home.rank.act;

import com.dykj.youfeng.R;
import com.dykj.youfeng.home.rank.fragment.MonthRankFragment;
import com.dykj.youfeng.home.rank.fragment.YearRankFragment;
import com.dykj.module.base.BaseActivity;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 排行榜
 */
public class RankActivity extends BaseActivity {
    @BindView(R.id.stabLayout)
    SegmentTabLayout stabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String[] mTitles = {"月榜", "半年榜"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();


    @Override
    public int intiLayout() {
        return R.layout.activity_rank;
    }

    @Override
    public void initData() {
        mFragments.add(new MonthRankFragment());
        mFragments.add(new YearRankFragment());
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        stabLayout.setTabData(mTitles);
        stabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void doBusiness() {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
