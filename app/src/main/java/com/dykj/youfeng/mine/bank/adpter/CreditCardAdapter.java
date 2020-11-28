/*
package com.dykj.youfeng.mine.bank.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.module.util.GlideUtils;

import androidx.annotation.NonNull;

*/
/**
 * 信用卡 适配器
 *//*

public class CreditCardAdapter extends BaseQuickAdapter<CreditcardListBean.ListBean, BaseViewHolder> {
    public CreditCardAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CreditcardListBean.ListBean item) {
        helper.addOnClickListener(R.id.iv_edit)
                .addOnClickListener(R.id.iv_delete)
                .setText(R.id.tv_bank_name,item.bank_name)
                .setText(R.id.tv_card_number, "**** **** **** "+item.bank_num);

        GlideUtils.setImage(helper.getView(R.id.iv_bank_logo), item.logo);
        helper.itemView.setBackgroundResource(item.viewShape);
    }
}
*/

package com.dykj.youfeng.mine.bank.adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.module.util.GlideUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.cretin.www.cretinautoupdatelibrary.utils.AppUtils.dp2px;

/**
 * 信用卡 适配器
 */
public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.MyViewHolder> {

    private Context context;
    private List<CreditcardListBean.ListBean> data;
    private View inflater;
    public CreditCardAdapter(Context context, ArrayList<CreditcardListBean.ListBean > datas) {
        this.context = context;
        this.data = datas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.item_credit_card,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }
    private float [] getCornerRadii(float leftTop,float rightTop, float leftBottom,float rightBottom){
        //这里返回的一个浮点型的数组，一定要有8个元素，不然会报错
        return floatArrayOf(dp2px(leftTop), dp2px(leftTop), dp2px(rightTop),
                dp2px(rightTop),dp2px(rightBottom), dp2px(rightBottom),dp2px(leftBottom),dp2px(leftBottom));
    }

    private float[] floatArrayOf(float dp2px, float dp2px1, float dp2px2, float dp2px3, float dp2px4, float dp2px5, float dp2px6, float dp2px7) {
        float array[]={dp2px,dp2px1,dp2px2,dp2px3,dp2px4,dp2px5,dp2px6,dp2px7};
       return  array;
    }

    private float dp2px(float dpVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CreditcardListBean.ListBean item=data.get(position);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(item.color));
        gd.setCornerRadii(getCornerRadii(8,8,0,0));
        holder.credRelative.setBackground(gd);

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor(item.color));
        gd2.setCornerRadii(getCornerRadii(0,0,8,8));
       holder.credLinear.setBackground(gd2);


        holder.tv_bank_name.setText(item.bank_name);
        holder.tv_card_number.setText(item.bank_num);
        GlideUtils.setImage(holder.iv_bank_logo, item.logo);

        holder.tv_card_edu.setText(item.money);
        holder.tv_card_riqi.setText(item.statement_date);
        holder.tv_card_haikri.setText(item.repayment_date);
        holder.tv_delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickMannageListene!=null){
                    onClickMannageListene.onCommit(item);
                }
            }

        });
        holder.editCardText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEditCardListener!=null){
                    onEditCardListener.onEditCard(item);
                }
            }
        });

            holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView editCardText,tv_bank_name,tv_card_number,tv_card_edu,tv_card_riqi,tv_card_haikri,tv_delete_card;
        public LinearLayout credLinear;
        public RelativeLayout credRelative;
        public ImageView iv_bank_logo;

        public View view;
        public MyViewHolder(View convertView) {
            super(convertView);

            editCardText=convertView.findViewById(R.id.editCardText);
            tv_bank_name=convertView.findViewById(R.id.tv_bank_name);
            tv_card_number=convertView.findViewById(R.id.tv_card_number);
            tv_card_edu=convertView.findViewById(R.id.tv_card_edu);
            tv_card_riqi=convertView.findViewById(R.id.tv_card_riqi);
            tv_card_haikri=convertView.findViewById(R.id.tv_card_haikri);
            tv_delete_card=convertView.findViewById(R.id.tv_delete_card);

            credLinear=convertView.findViewById(R.id.credLinear);
            credRelative=convertView.findViewById(R.id.credRelative);
            iv_bank_logo=convertView.findViewById(R.id.iv_bank_logo);

            view=convertView;

        }
    }

    public  interface  OnClickMannageListene{
        void onCommit(CreditcardListBean.ListBean b);

    }

    //私有属性
    private  OnItemClickListener onItemClickListener = null;

    public void setOnClickMannageListene(OnClickMannageListene onClickMannageListene) {
        this.onClickMannageListene = onClickMannageListene;
    }

    private OnClickMannageListene onClickMannageListene;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnEditCardListener(OnEditCardListener onEditCardListener) {
        this.onEditCardListener = onEditCardListener;
    }

    private OnEditCardListener onEditCardListener;
    public interface  OnEditCardListener{
        void onEditCard(CreditcardListBean.ListBean b);
    }

}

