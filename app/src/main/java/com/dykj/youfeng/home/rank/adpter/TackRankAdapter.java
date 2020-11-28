package com.dykj.youfeng.home.rank.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dykj.module.util.GlideUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.base.Planlist;
import com.dykj.youfeng.mode.RepaymentPlanListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TackRankAdapter extends RecyclerView.Adapter<TackRankAdapter.BaseViewHolder> {


    private Context ct;
    private List<Planlist.ListBean> data;
    private View inflater;

    public TackRankAdapter(Context ct, List<Planlist.ListBean> datas) {
        this.ct = ct;
        this.data = datas;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(ct).inflate(R.layout.main_page_tack_bank_layout, parent, false);
     /*   ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        inflater.setLayoutParams(lp);
        RecyclerView.ViewHolder vh = new RecyclerView.ViewHolder(inflater){};*/
        BaseViewHolder myViewHolder = new BaseViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Planlist.ListBean bean=data.get(position);
        GlideUtils.setImage(holder.bank_image, bean.card_logo);

        if(bean.card!=null) {
            holder.bankMoneyText.setText(bean.card.money);
            holder.bankNameText.setText(bean.card.bank_name);
            holder.bankTimeText.setText(bean.card.repayment_date);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null ){
                    onItemClickListener.onItemClick(bean);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface  OnItemClickListener{
        public void onItemClick(Planlist.ListBean bean);
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder {

        TextView bankNameText;

        TextView bankTypeText;

        TextView bankMoneyText;

        TextView bankTimeText,commitText;

        ImageView bank_image;

        View view;

        public BaseViewHolder(@NonNull View holder) {
            super(holder);
            bank_image = holder.findViewById(R.id.bank_image);
            bankNameText=holder.findViewById(R.id.bank_name_text);
            bankTypeText=holder.findViewById(R.id.bank_type_text);
            bankMoneyText=holder.findViewById(R.id.bank_money_text);
            bankTimeText=holder.findViewById(R.id.bank_time_text);
            view=holder;


        }
    }


}