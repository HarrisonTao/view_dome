package com.dykj.youfeng.mall.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.fragment.HasEvaListFragment;
import com.dykj.youfeng.mall.fragment.UnEvaListFragment;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyEvaActivity extends BaseActivity {

    @BindView(R.id.tab)
    XTabLayout tab;
    @BindView(R.id.vp)
    ViewPager vp;
    private String[] mTitles;
    private List<BaseFragment> listPages;



    @Override
    public int intiLayout() {
        return R.layout.activity_my_eva;
    }

    @Override
    public void initData() {
        initTitle("我的评价");
    }

    @Override
    public void doBusiness() {
        initTab();
    }

    private void initTab() {
        listPages = new ArrayList<>();
        mTitles = new String[]{"已评价", "未评价"};

        listPages.add(new HasEvaListFragment());
        listPages.add(new UnEvaListFragment());

        FragmentPagerAdapter adapterPage = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return listPages.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return listPages.get(arg0);
            }

            //重写这个方法，将设置每个Tab的标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };

        vp.setOffscreenPageLimit(2);
        vp.setAdapter(adapterPage);
        tab.setupWithViewPager(vp);
//        int page = getIntent().getIntExtra("position", 0);
//        vp.setCurrentItem(page);
    }

}
