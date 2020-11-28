package com.dykj.module.util;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * @author WZ
 * @date 2019/6/6.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class SmartRefreshUtils {
    private static final SmartRefreshUtils ourInstance = new SmartRefreshUtils();

    public static SmartRefreshUtils getInstance() {
        return ourInstance;
    }

    public SmartRefreshUtils() {
    }

    public static void loadMore(BaseQuickAdapter adapter, int nowPage, int allPage, List list, SmartRefreshLayout mSrlRefresh) {
        //zxj 发现APP有异常退出，Attempt to invoke virtual method 'com.scwang.smartrefresh.layout.api.RefreshLayout com.scwang.smartrefresh.layout.SmartRefreshLayout.finishRefresh()' on a null object reference
        //具体原因不明确，暂时加上相关处理
        if(mSrlRefresh == null)
        {
            return;
        }

        if (list == null) {
            if (allPage == 0) {
                adapter.setNewData(null);
            }
            mSrlRefresh.finishRefresh();
            mSrlRefresh.finishLoadMoreWithNoMoreData();
            return;
        }
        if (nowPage == 1) {
            if(list==null || list.size()<1)
            {
                adapter.setNewData(null);
                mSrlRefresh.finishRefresh();
            }else if (nowPage == allPage){
                adapter.setNewData(list);
                mSrlRefresh.finishRefresh();
                mSrlRefresh.setEnableLoadMore(false);
            } else  {
                adapter.setNewData(list);
                mSrlRefresh.finishRefresh();
            }
        } else {
            if (nowPage < allPage) {
                if (list != null && list.size() > 0) {


                    adapter.addData(list);
                    mSrlRefresh.finishLoadMore();
                } else {
                    mSrlRefresh.finishLoadMoreWithNoMoreData();
                }
            } else if (nowPage == allPage) {
                if (list != null && list.size() > 0) {
                    adapter.addData(list);
                    mSrlRefresh.finishLoadMoreWithNoMoreData();
                } else {
                    mSrlRefresh.finishLoadMoreWithNoMoreData();
                }
            } else {
                mSrlRefresh.finishLoadMoreWithNoMoreData();
            }
        }
    }




    public static void loadMore(BaseQuickAdapter adapter, List list, SmartRefreshLayout mSrlRefresh) {
        if (list == null) {
            adapter.setNewData(null);
            mSrlRefresh.finishRefresh();
            mSrlRefresh.finishLoadMoreWithNoMoreData();
            return;
        }
        if (list.size() > 0) {
            adapter.setNewData(list);
            mSrlRefresh.finishRefresh();
            mSrlRefresh.finishLoadMore();
        } else {
//            adapter.setNewData(null);
            mSrlRefresh.finishRefresh();
            mSrlRefresh.finishLoadMore();
        }
    }
}
