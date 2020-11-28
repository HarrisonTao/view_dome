package com.dykj.module.view.dialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dykj.module.R;
import com.dykj.module.view.bean.PlanDateBean;

import java.util.List;

/**
 * @author 9
 * @version com.dy.aoyou.credit.repay.widget
 * @since 2019/3/24
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃糟糕，憋不住拉出来了啦～
 * ┃　　　　　　　┃
 * ┃　╰┬┬┬╯　┃
 * ┃　　╰—╯　  ┃
 * ┗━┓　　　┏━┛
 * 　 ┃　　　┃
 * 　  ┃　　　┗━━━┓
 * 　　  ┃　　　　　　　┣━━━━┓
 * 　　  ┃　　　　　　　┏━━━━┛ 　　◢
 * 　 ┗┓┓┏━┳┓┏┛ 　　　　　　◢◤◢◣
 * 　　 　 ┃┫┫　┃┫┫ 　　　　　　◢◣◢◤█◣
 * 　　 　┗┻┛　┗┻┛ 　　　　　◢█◢◣◥◣█◣
 */
public class PlanDateAdapter extends RecyclerView.Adapter<PlanDateAdapter.ViewHolder> {

    private Context mCtx;
    private List<PlanDateBean> mList;

    public PlanDateAdapter(Context context, List<PlanDateBean> list) {
        this.mCtx = context;
        this.mList = list;
    }

    public void setOnItemClickListener(OnOperateListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 1;
        } else {
            if (mList.size() == 0) {
                return 1;
            } else {
                return mList.size();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (mList == null || (mList.size() == 0)) {
                return 0;
            } else if (getItemCount() > position && position > -1) {
                String vType = mList.get(position).getType();
//                if (TextUtils.equals("item1", vType)) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lay = LayoutInflater.from(mCtx);
        View view;
        if (viewType == 1) {
            view = lay.inflate(R.layout.time_sign_item_txt, parent, false);
        } else {
            view = lay.inflate(R.layout.time_sign_item_txt, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int pi) {
        if (mList == null || (mList.size() <= pi)) {
            holder.txtDay.setText("");
            //            Drawable drawable=mCtx.getResources().getDrawable(R.mipmap.si);
//            drawable.setBounds( 0, 0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//            holder.txtDay.setCompoundDrawables(drawable,null, null, null);
            holder.imgIcon.setImageResource(R.drawable.z_shape_oval_sys);
            return;
        }
        PlanDateBean data = mList.get(pi);

        holder.imgIcon.setVisibility(View.GONE);
        holder.txtDay.setText(data.getDay());
        if (data.isEnable()) {
            if (data.isSelect()) {
                holder.imgIcon.setVisibility(View.VISIBLE);
                holder.imgIcon.setImageResource(R.drawable.z_shape_oval_sys);
                holder.txtDay.setTextColor(Color.parseColor("#ffffff"));
                holder.itemView.setOnClickListener(new clickItem(pi, "day", data.getDay()));
            } else {
                //                holder.imgIcon.setVisibility(View.INVISIBLE);
                holder.txtDay.setTextColor(Color.parseColor("#333333"));
                holder.itemView.setOnClickListener(new clickItem(pi, "day", data.getDay()));
            }
        } else {
            holder.txtDay.setTextColor(Color.parseColor("#999999"));
            holder.itemView.setOnClickListener(null);
        }
    }

    /**
     * 设置TextView文字样式及颜色
     *
     * @param txt TextView
     * @param pl0 sty 文字信息段
     */
    private void setTextStyle(TextView txt, String pl0, String pl1, String pl2, String pl3) {
        SpannableString styledText = new SpannableString(txt.getText());
        int length1 = txt.getText().toString().length();

        styledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style_text_sys11), 0, 2, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style_text_black14), 2, pl0.length(), Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(mCtx, R.style.style_text_gray13), length1 - pl3.length(),
                length1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        txt.setText(styledText);
    }
   private OnOperateListener onItemClickListener;
    class clickItem implements View.OnClickListener {
        private String tag, day;
        private int index;

        clickItem(int position, String type, String day) {
            this.index = position;
            this.tag = type;
            this.day = day;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.operate(index, tag, day);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        //空数据页
        TextView txtDay;

        ViewHolder(View vi, int type) {
            super(vi);
            if (type == 1) {
                txtDay = vi.findViewById(R.id.sign_item_txt);
                imgIcon = vi.findViewById(R.id.sign_item_icon);
            } else {
                txtDay = vi.findViewById(R.id.sign_item_txt);
                imgIcon = vi.findViewById(R.id.sign_item_icon);
            }
        }
    }
    public interface OnOperateListener {
        void operate(int instruct, String str0, String str1);
    }
}
