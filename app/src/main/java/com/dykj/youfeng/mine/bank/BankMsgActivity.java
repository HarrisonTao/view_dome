package com.dykj.youfeng.mine.bank;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mine.bank.fragment.CreditCardFragment;
import com.dykj.youfeng.mine.bank.fragment.DepositCardFragment;
import com.dykj.module.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 卡片管理
 */
public class BankMsgActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewpager)
    ViewPager mViewpager;

    private String[] itemTitle = {"信用卡", "储蓄卡"};

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

    private int COUNT_ITEM = 2;

    @Override
    public int intiLayout() {
        return R.layout.activity_repayment_plan;
    }

    @Override
    public void initData() {
        initTitle("卡片管理");
        fragmentList.clear();
        fragmentList.add(new CreditCardFragment());
        fragmentList.add(new DepositCardFragment());

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
