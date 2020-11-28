package com.dykj.youfeng.home.repayment.activity;


import com.dykj.youfeng.R;
import com.dykj.youfeng.home.fragment.RepaymentFragment;
import com.dykj.module.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 还款计划
 */
public class RepaymentPlanActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewpager)
    ViewPager mViewpager;

//    状态，0-已取消，1-进行中，2-完成，3-失败，默认5-全部
    private String[] itemTitle = {"全部", "执行中", "已完成", "已失败", "已取消"};
    private Integer[] itemStatus = {5, 1, 2, 3, 0};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private int COUNT_ITEM = 5;
    private String mType;


    @Override
    public int intiLayout() {
        return R.layout.activity_repayment_plan;
    }

    @Override
    public void initData() {
        mType = (String) getIntentData();
        initTitle("还款计划");
        fragmentList.clear();
        for (int i = 0; i < itemStatus.length; i++) {
            fragmentList.add(RepaymentFragment.newInstance(itemStatus[i] +"",mType));
        }

        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position % COUNT_ITEM);
            }

            @Override
            public int getCount() {
                return COUNT_ITEM;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return itemTitle[position % COUNT_ITEM];
            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(COUNT_ITEM);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void doBusiness() {

    }
}
