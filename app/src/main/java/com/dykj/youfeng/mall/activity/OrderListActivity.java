package com.dykj.youfeng.mall.activity;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.fragment.OrderListFragment;
import com.dykj.module.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author lcjing
 */
public class OrderListActivity extends BaseActivity {


    @BindView(R.id.tab)
    XTabLayout tab;
    @BindView(R.id.vp)
    ViewPager vp;

    private String[] mTitles;
    private List<OrderListFragment> listPages;

    @Override
    public int intiLayout() {
        return R.layout.activity_order_list;
    }

    @Override
    public void initData() {
        initTitle("我的订单");
    }

    @Override
    public void doBusiness() {
        listPages = new ArrayList<>();
        mTitles = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};
//        10待支付，20已付款、待发货， 30已发货， 40已收货
        listPages.add(OrderListFragment.getInstance(""));
        listPages.add(OrderListFragment.getInstance("10"));
        listPages.add(OrderListFragment.getInstance("20"));
        listPages.add(OrderListFragment.getInstance("30"));
        listPages.add(OrderListFragment.getInstance("40"));
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
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                for (int i = 0; i < listPages.size(); i++) {
//                    listPages.get(i).isShowToast = i == position;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(adapterPage);
        tab.setupWithViewPager(vp);
        int page = (Integer) getIntentData();
        vp.setCurrentItem(page);
    }


}
