package com.dykj.module.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.dykj.module.R;
import com.dykj.module.util.MyLogger;
import com.dykj.module.view.bean.PlanDateBean;
import com.dykj.module.view.dialog.adapter.PlanDateAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @author 9
 * @version com.dy.aoyou.credit.repay.widget
 * @since 2019/3/23还款计划的日期选择
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
public class PlanDateChooseDialog extends Dialog implements View.OnClickListener, LifecycleObserver {
    private Context mCtx;
    private PlanDateResult mListener;


    private int cardDateHkr, cardDateZdr;
    private int mCurY, mCurM, mCurD;
    private int dataYear, dataMonth;

    private TextView tvMonth1, tvMonth2;
    private RecyclerView mRecycler2;
    //本月日期选择
    private PlanDateAdapter mAdapter1;
    private List<PlanDateBean> listMonth1 = new ArrayList<>();

    private PlanDateAdapter mAdapter2;
    private List<PlanDateBean> listMonth2 = new ArrayList<>();

    private List<String> selectDay = new ArrayList<>();

    public List<String> getSelectDay() {
        //排序
        Collections.sort(selectDay);
        return selectDay;
    }


    public PlanDateChooseDialog(Context context, PlanDateResult operateListener) {
        super(context, R.style.dialog_bottom);
        this.mCtx = context;
        this.mListener = operateListener;
        initDialogViewCreate();
    }

    public PlanDateChooseDialog setLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        MyLogger.dLog().e("Lifecycle.Event：" + "ON_DESTROY");
        dismiss();
    }


    private void initDialogViewCreate() {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.time_repayment_plan_date_dialog, null);
        this.setCancelable(true);
        this.setContentView(view);
        this.setCanceledOnTouchOutside(true);

        view.findViewById(R.id.viewNull).setOnClickListener(this);
        view.findViewById(R.id.tvDialogDismiss).setOnClickListener(this);
        view.findViewById(R.id.tvDialogWhole).setOnClickListener(this);
        view.findViewById(R.id.btnConfirm).setOnClickListener(this);

        tvMonth1 = view.findViewById(R.id.tvMonth1);
        tvMonth2 = view.findViewById(R.id.tvMonth2);

        mAdapter1 = new PlanDateAdapter(mCtx, listMonth1);
        mAdapter1.setOnItemClickListener((instruct, str0, str1) -> {

            int isAddIndex = -1;
            for (int i = 0; i < selectDay.size(); i++) {
                if (TextUtils.equals(selectDay.get(i), listMonth1.get(instruct).getTime())) {
                    isAddIndex = i;
                }
            }
            if (listMonth1.get(instruct).isSelect()) {
                listMonth1.get(instruct).setSelect(false);
                if (isAddIndex == -1) {
                    selectDay.add(listMonth1.get(instruct).getTime());
                } else {
                    selectDay.remove(isAddIndex);
                }
            } else {
                listMonth1.get(instruct).setSelect(true);
                if (isAddIndex == -1) {
                    selectDay.add(listMonth1.get(instruct).getTime());
                }
            }

            mAdapter1.notifyDataSetChanged();
        });
        RecyclerView recyclerB = findViewById(R.id.recyclerDay1);
        recyclerB.setLayoutManager(new GridLayoutManager(mCtx, 7));
        recyclerB.setAdapter(mAdapter1);
        recyclerB.setFocusable(false);
        recyclerB.setFocusableInTouchMode(false);

        mAdapter2 = new PlanDateAdapter(mCtx, listMonth2);
        mAdapter2.setOnItemClickListener((instruct, str0, str1) -> {

            int isAddIndex = -1;
            for (int i = 0; i < selectDay.size(); i++) {
                if (TextUtils.equals(selectDay.get(i), listMonth2.get(instruct).getTime())) {
                    isAddIndex = i;
                }
            }
            if (listMonth2.get(instruct).isSelect()) {
                listMonth2.get(instruct).setSelect(false);
                if (isAddIndex == -1) {
                    selectDay.add(listMonth2.get(instruct).getTime());
                } else {
                    selectDay.remove(isAddIndex);
                }
            } else {
                listMonth2.get(instruct).setSelect(true);
                if (isAddIndex == -1) {
                    selectDay.add(listMonth2.get(instruct).getTime());
                }
            }

            mAdapter2.notifyDataSetChanged();
        });
        mRecycler2 = findViewById(R.id.recyclerDay2);
        mRecycler2.setLayoutManager(new GridLayoutManager(mCtx, 7));
        mRecycler2.setAdapter(mAdapter2);
        mRecycler2.setFocusable(false);
        mRecycler2.setFocusableInTouchMode(false);
    }

    private boolean isAllChoose;

    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.btnConfirm) {
            if (mListener != null) {
                if (selectDay.size() > 0) {
                    dismiss();
                    mListener.operate(1, "ok", getSelectDay());
                } else {
                    ToastUtils.showShort("请选择日期");
                }
            }
        } else if (i1 == R.id.tvDialogWhole) {
            isAllChoose = !isAllChoose;

            selectDay.clear();
            if (listMonth1.size() > 0) {
                for (int i = 0; i < listMonth1.size(); i++) {
                    listMonth1.get(i).setSelect(isAllChoose);
                    if (isAllChoose && listMonth1.get(i).isEnable()) {
                        listMonth1.get(i).setSelect(true);
                        selectDay.add(listMonth1.get(i).getTime());
                    }
                }
                mAdapter1.notifyDataSetChanged();
            }
            if (listMonth2.size() > 0) {
                for (int i = 0; i < listMonth2.size(); i++) {
                    listMonth2.get(i).setSelect(isAllChoose);
                    if (isAllChoose && listMonth2.get(i).isEnable()) {
                        listMonth2.get(i).setSelect(true);
                        selectDay.add(listMonth2.get(i).getTime());
                    }
                }
                mAdapter2.notifyDataSetChanged();
            }
        } else if (i1 == R.id.viewNull || i1 == R.id.tvDialogDismiss) {
            dismiss();
        }
    }

    private void setVisibilityMonth1(boolean isVisibility, int yy, int mm, List<PlanDateBean> days) {
        if (isVisibility) {
            tvMonth1.setVisibility(View.VISIBLE);
            tvMonth1.setText(yy + "年" + mm + "月");

            listMonth1.clear();
            listMonth1.addAll(days);
            mAdapter1.notifyDataSetChanged();
        } else {
            tvMonth1.setVisibility(View.GONE);
        }
    }

    private void setVisibilityMonth2(boolean isVisibility, int yy, int mm, List<PlanDateBean> days) {
        if (isVisibility) {
            tvMonth2.setVisibility(View.VISIBLE);
            tvMonth2.setText(yy + "年" + mm + "月");

            mRecycler2.setVisibility(View.VISIBLE);
            listMonth2.clear();
            listMonth2.addAll(days);
            mAdapter2.notifyDataSetChanged();
        } else {
            tvMonth2.setVisibility(View.GONE);
            mRecycler2.setVisibility(View.GONE);
        }

    }

    /**
     * 设置选择日期范围 , 是否可以选择当前日期
     *
     * @param statementDate 账单日
     * @param hkTime        还款日
     */
    public void setInitDate(String statementDate, String hkTime) {
        //获取当前日期
        Calendar c = Calendar.getInstance();
        mCurY = c.get(Calendar.YEAR);
        mCurM = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        mCurD = c.get(Calendar.DAY_OF_MONTH);
        this.dataYear = mCurY;
        this.dataMonth = mCurM;

        try {
            this.cardDateHkr = Integer.parseInt(hkTime);
            this.cardDateZdr = Integer.parseInt(statementDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mCurD < cardDateHkr) {
            //当前天<还款日
            if (mCurD > cardDateZdr) {
                //当前日期大于账单日，账单日<当前日期<还款日，再本月范围内，
                setVisibilityMonth1(true, mCurY, mCurM, this.getDayMonth1NowToHkDay(dataYear, dataMonth));
                setVisibilityMonth2(false, mCurY, mCurM, null);
            } else if (cardDateHkr < cardDateZdr) {
                //当前日期<还款日<账单日，再本月范围内，
                setVisibilityMonth1(true, mCurY, mCurM, this.getDayMonth1NowToHkDay(dataYear, dataMonth));
//                int nextM,nextY;
//                if (mCurM==12){
//                    nextM=1;
//                    nextY=mCurY+1;
//                }else{
//                    nextM=mCurM+1;
//                    nextY=mCurY;
//                }
//                setVisibilityMonth2(true,nextY,nextM,getDayGenerateNextMonth(nextY,nextM));
                setVisibilityMonth2(false, mCurY, mCurM, null);
            } else {
                //当前<账单日<还款日
                setVisibilityMonth1(true, mCurY, mCurM, this.getDayMonth1ZdrToHk(dataYear, dataMonth));
                setVisibilityMonth2(false, mCurY, mCurM, null);
            }
        } else {
            //当前天>还款日
            if (mCurD > cardDateZdr) {
                //当前天>账款日
                if (cardDateZdr > cardDateHkr) {
                    //当前日>账单日>还款日;选择这个月剩余到下个月还款日
                    setVisibilityMonth1(true, mCurY, mCurM, this.getDayGenerateMonthEnd(dataYear, dataMonth));
                    int nextM, nextY;
                    if (mCurM == 12) {
                        nextM = 1;
                        nextY = mCurY + 1;
                    } else {
                        nextM = mCurM + 1;
                        nextY = mCurY;
                    }
                    setVisibilityMonth2(true, nextY, nextM, getDayGenerateNextMonth(nextY, nextM));
                } else {
                    //账单日<还款日<当前日;选择下个月的
                    int nextM, nextY;
                    if (mCurM == 12) {
                        nextM = 1;
                        nextY = mCurY + 1;
                    } else {
                        nextM = mCurM + 1;
                        nextY = mCurY;
                    }
                    setVisibilityMonth1(true, nextY, nextM, this.getDayMonth1ZdrToHk(nextY, nextM));
                    setVisibilityMonth2(false, 0, 0, null);
                }
            } else {
                //当前天<账款日；选择账单日到下个月还款日
                setVisibilityMonth1(true, mCurY, mCurM, this.getDayGenerateZdToEnd(dataYear, dataMonth));
                int nextM, nextY;
                if (mCurM == 12) {
                    nextM = 1;
                    nextY = mCurY + 1;
                } else {
                    nextM = mCurM + 1;
                    nextY = mCurY;
                }
                setVisibilityMonth2(true, nextY, nextM, getDayGenerateNextMonth(nextY, nextM));
            }
        }
        selectDay.clear();
    }

    /**
     * create 获取月份天数
     *
     * @param year  年份
     * @param month 鱼粉
     * @return n
     */
    private int getDaySize(int year, int month) {
        int dayCount = 30;
        switch (month) {
            case 2:
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    dayCount = 29;
                } else {
                    dayCount = 28;
                }
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayCount = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                dayCount = 30;
                break;
        }
        return dayCount;
    }

    //获取时间，从当前日期，到月末
    private List<PlanDateBean> getDayGenerateZdToEnd(int year, int month) {
        List<PlanDateBean> listDate = new ArrayList<>();
        for (int i = cardDateZdr; i <= getDaySize(year, month); i++) {
            PlanDateBean bean = new PlanDateBean(i + "");
            bean.setDay(i + "");
            bean.setTime(getTimestampYYMMDD(year, month, i));
            if (isEnable(year, month, i)) {
                bean.setSelect(isSelectDay(year, month, i));
                bean.setEnable(true);
            } else {
                bean.setEnable(false);
            }
            listDate.add(bean);
        }
        return listDate;
    }

    //获取时间，从当前日期，到月末
    private List<PlanDateBean> getDayGenerateMonthEnd(int year, int month) {
        List<PlanDateBean> listDate = new ArrayList<>();
        for (int i = mCurD; i <= getDaySize(year, month); i++) {
            PlanDateBean bean = new PlanDateBean(i + "");
            bean.setDay(i + "");
            bean.setTime(getTimestampYYMMDD(year, month, i));
            if (isEnable(year, month, i)) {
                bean.setSelect(isSelectDay(year, month, i));
                bean.setEnable(true);
            } else {
                bean.setEnable(false);
            }
            listDate.add(bean);
        }
        return listDate;
    }

    //获取时间，从月初，到还款日
    private List<PlanDateBean> getDayGenerateNextMonth(int year, int month) {
        List<PlanDateBean> listDate = new ArrayList<>();
        for (int i = 1; i <= cardDateHkr; i++) {
            PlanDateBean bean = new PlanDateBean(i + "");
            bean.setDay(i + "");
            bean.setTime(getTimestampYYMMDD(year, month, i));
            if (isEnable(year, month, i)) {
                bean.setSelect(isSelectDay(year, month, i));
                bean.setEnable(true);
            } else {
                bean.setEnable(false);
            }
            listDate.add(bean);
        }
        return listDate;
    }

    /**
     * create 当前月份，账单日到还款日
     *
     * @param year  年份
     * @param month 鱼粉
     * @return n
     */
    private List<PlanDateBean> getDayMonth1ZdrToHk(int year, int month) {
        List<PlanDateBean> listDate = new ArrayList<>();

//        //月末还款的
        int size = getDaySize(mCurY, mCurM);
        if (cardDateHkr > size) {
            cardDateHkr = size;
        }
        for (int i = cardDateZdr; i <= cardDateHkr; i++) {
            PlanDateBean bean = new PlanDateBean(i + "");
            bean.setDay(i + "");
            bean.setTime(getTimestampYYMMDD(year, month, i));
            //当前日期正在还款日与账单日之间
            if (isEnable(year, month, i)) {
                bean.setSelect(isSelectDay(year, month, i));
                bean.setEnable(true);
            } else {
                bean.setEnable(false);
            }
            listDate.add(bean);
        }
        return listDate;
    }

    /**
     * create 当前月份，当前日到还款日
     *
     * @param year  年份
     * @param month 鱼粉
     * @return n
     */
    private List<PlanDateBean> getDayMonth1NowToHkDay(int year, int month) {
        int size = getDaySize(mCurY, mCurM);
        if (cardDateHkr > size) {
            cardDateHkr = size;
        }
        List<PlanDateBean> listDate = new ArrayList<>();
        for (int i = mCurD; i <= cardDateHkr; i++) {
            PlanDateBean bean = new PlanDateBean(i + "");
            bean.setDay(i + "");
            bean.setTime(getTimestampYYMMDD(year, month, i));
            //当前日期正在还款日与账单日之间
            if (isEnable(year, month, i)) {
                bean.setSelect(isSelectDay(year, month, i));
                bean.setEnable(true);
            } else {
                bean.setEnable(false);
            }
            listDate.add(bean);
        }
        return listDate;
    }

    //生成时间字符串 yy-mm-dd
    private String getTimestampYYMMDD(int year, int month, int day) {
        String stDay;
        if (day < 10) {
            stDay = "0" + day;
        } else {
            stDay = "" + day;
        }
        if (month < 10) {
            return (year + "-0" + month + "-" + stDay);
        } else {
            return year + "-" + month + "-" + stDay;
        }
    }

    //判断日期是否已被选中
    private boolean isSelectDay(int year, int month, int day) {
        boolean select = false;
        for (int j = 0; j < selectDay.size(); j++) {
            try {
                String[] aryD = selectDay.get(j).split("-");
                int sYy = Integer.parseInt(aryD[0]);
                int sMM = Integer.parseInt(aryD[1]);
                int sDD = Integer.parseInt(aryD[2]);
                if (sYy == year && month == sMM && sDD == day) {
                    select = true;
                }
            } catch (Exception e) {
                select = false;
            }
        }
        return select;
    }

    //判断日期是否已被选中
    private boolean isEnable(int year, int month, int day) {
        boolean enable = true;
        if (year == mCurY && month == mCurM && day <= mCurD) {
            enable = false;
        }
        return enable;
    }

    public interface PlanDateResult {
        void operate(int instruct, String str0, List<String> str1);
    }
}
