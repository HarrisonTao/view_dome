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
 * Created by lcjing on 2019/7/3.
 */
public class RefundImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public RefundImgAdapter(int layoutResId, List<String> data) {
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

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (imgList==null) {
            imgList=new ArrayList<>();
        }
        imgList.add(helper.getLayoutPosition(),helper.getView(R.id.iv_img));
        helper.setGone(R.id.iv_delete,!StringUtils.isEmpty(item));
        if (StringUtils.isEmpty(item)) {
            helper.setImageResource(R.id.iv_img,R.mipmap.btn_tian_h1);
        }else {
            GlideUtils.setImage(helper.getView(R.id.iv_img),R.mipmap.btn_tian_h1,item);
        }
        helper.addOnClickListener(R.id.iv_img)
                .addOnClickListener(R.id.iv_delete);
    }


}
