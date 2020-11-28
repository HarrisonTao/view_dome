package com.dykj.youfeng.home.sign.act;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.SignInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.Flag;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseEventData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.date.DateFormatUtil;
import com.dykj.module.util.toast.DyToast;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 签到
 */
public class SignActivity extends BaseActivity {
    @BindView(R.id.tv_index)
    TextView tvIndex;
    @BindView(R.id.tv_index_points)
    TextView tvIndexPoints;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_sign_log)
    TextView tvSignLog;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private int mDayPoints;   // 当日签到积分

    @Override
    public int intiLayout() {
        return R.layout.activity_sign;
    }


    @Override
    public void initData() {
        initTitle("签到");
        mCalendarView.setOnCalendarInterceptListener(new CalendarView.OnCalendarInterceptListener() {
            @Override
            public boolean onCalendarIntercept(Calendar calendar) {
                return calendar.hasScheme();
            }

            @Override
            public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
                LogUtils.e(isClick);
            }
        });
        tvTime.setText(mCalendarView.getCurYear() + "." + mCalendarView.getCurMonth() + "." + mCalendarView.getCurDay());
    }


    @Override
    public void doBusiness() {
        getSignIndex();
        checkIsSign();
    }


    @OnClick({R.id.tv_sign_log, R.id.tv_sign, R.id.tv_use_points})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_log:
                startAct(SignLogActivity.class);
                break;


            case R.id.tv_sign:
                signSignIn();
                break;


            case R.id.tv_use_points:
                EventBus.getDefault().post(new BaseEventData<>(Flag.Event.JUMP_TAB2));
                finish();
                break;
        }
    }


    /**
     * 获取积分
     */
    private void getSignIndex() {
        HttpFactory.getInstance().signIndex(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(SignActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SignInfoBean>>() {

                    @Override
                    public void success(HttpResponseData<SignInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            SignInfoBean smsBean = data.getData();
                            tvIndex.setText(smsBean.points + "");
                            tvDay.setText(smsBean.day + "");

                            List<String> dates = data.getData().dates;
                            if (null != dates){
                                List<String> mSignDateList = dates;
                                setCalendarData(mSignDateList);
                            }

                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    /**
     * 设置日历 2019-11-01,2019-11-05,2019-11-08
     *
     * @param mSignDateList
     */
    private List<String> mSignDayDataList = new ArrayList<>();     // 签到的天数
    private Map<String, Calendar> map = new HashMap<>();     // 没有签到的天

    private void setCalendarData(List<String> mSignDateList) {
        mSignDayDataList.clear();
        map.clear();
        for (int i = 0; i < mSignDateList.size(); i++) {
            String ymd = mSignDateList.get(i);
            String dayData = ymd.substring(ymd.length() - 2);  // 取出最后两位  日
            if (dayData.startsWith("0")) {
                mSignDayDataList.add(dayData.replace("0", ""));
            } else {
                mSignDayDataList.add(dayData);
            }
        }

        // 过滤出没有签到的天数
        int dayOfMonth = DateFormatUtil.getDayOfMonth();

        for (int i = 1; i <= dayOfMonth; i++) {
            if (!mSignDayDataList.contains(i + "")) {

                map.put(getSchemeCalendar(i).toString(), getSchemeCalendar(i));
            }
        }


        mCalendarView.setSchemeDate(map);

        Calendar startCalendar = new Calendar();
        startCalendar.setYear(mCalendarView.getCurYear());
        startCalendar.setMonth(mCalendarView.getCurMonth());
        startCalendar.setDay(Integer.parseInt(mSignDayDataList.get(0)));

        Calendar endCalendar = new Calendar();
        endCalendar.setYear(mCalendarView.getCurYear());
        endCalendar.setMonth(mCalendarView.getCurMonth());
        endCalendar.setDay(Integer.parseInt(mSignDayDataList.get(mSignDayDataList.size() - 1)));
        mCalendarView.setSelectCalendarRange(startCalendar, endCalendar);
    }


    /**
     * 用户签到
     */
    private void signSignIn() {
        HttpFactory.getInstance().signSignIn(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(SignActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<Integer>>() {
                    @Override
                    public void success(HttpResponseData<Integer> data) {
                        if ("9999".equals(data.status)) {
                            DyToast.getInstance().success(data.message);
                            tvSign.setClickable(false);
                            tvSign.setText("今日已签到");
                            tvSign.setBackgroundColor(getResources().getColor(R.color.font_c3c3));
                            mDayPoints = data.getData();
                            tvIndexPoints.setText(mDayPoints + "积分");

                            getSignIndex();
                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }

    /**
     * 检测是否签到   签到1100,未签到1101
     */
    private void checkIsSign() {
        HttpFactory.getInstance().checkIsSign(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(SignActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("1100".equals(data.status)) {
                            tvSign.setClickable(false);
                            tvSign.setText("今日已签到");
                            tvSign.setBackgroundColor(getResources().getColor(R.color.font_c3c3));

                            tvIndexPoints.setText(mDayPoints + "积分");
                        } else if ("1101".equals(data.status)) {
                            tvSign.setClickable(true);
                            tvSign.setText("立即签到");
                            tvSign.setBackgroundColor(getResources().getColor(R.color.main_color));
                        } else {
                            DyToast.getInstance().warning(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    private Calendar getSchemeCalendar(int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(mCalendarView.getCurYear());
        calendar.setMonth(mCalendarView.getCurMonth());
        calendar.setSchemeColor(0xFFFFFF);//如果单独标记颜色、则会使用这个颜色
        calendar.setDay(day);
        return calendar;
    }


}
