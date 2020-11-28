package com.dykj.youfeng.mall.adapter;

import android.graphics.Rect;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.view.imgbrowser.util.JMatrixUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcjing on 2018/12/26.
 */

public class EvaImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int defaultImageResource= R.mipmap.btn_tian_h;

    public EvaImgAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }
    private List<ImageView> imgList;

    public List<Rect> getImgList() {
        List<Rect> rects = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            if (!StringUtils.isEmpty( getData().get(i))) {
                rects.add(JMatrixUtil.getDrawableBoundsInView(imgList.get(i)));
            }
        }

        return rects;
    }



    public void  refreshData(){
        if (imgList!=null) {
            imgList.clear();
        }
        super.  notifyDataSetChanged();
    }
    public void setDefaultImageResource(int defaultImageResource) {
        this.defaultImageResource = defaultImageResource;
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        if (imgList==null) {
            imgList=new ArrayList<>();
        }
        imgList.add(helper.getView(R.id.iv_content));
        if (StringUtils.isEmpty(item)) {
            helper.setImageResource(R.id.iv_content,defaultImageResource);
            helper.setVisible(R.id.iv_c,false);
        }else {
            GlideUtils.setImage(helper.getView(R.id.iv_content),R.mipmap.btn_tian_h,item);
            helper.setVisible(R.id.iv_c,true);
        }

        helper.addOnClickListener(R.id.iv_content);
        helper.addOnClickListener(R.id.iv_c);
    }
}
