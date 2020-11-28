package com.dykj.module.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dykj.module.R;
import com.dykj.module.view.WheelView;
import com.dykj.module.view.bean.AreaBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lcjing
 * @date 2019/3/30
 */
public class SelectCityDialog extends BaseDialog {

    private Context context;
    private List<AreaBean> provinceList;
    private List<AreaBean> cityList;
    private String title = "";
    private int pIndex = 0, cIndex = 0;
    private SelectCityBack callBack;
    private List<String> provinces, citys;

    public SelectCityDialog(Context context, SelectCityBack callBack, String title) {
        this.context = context;
        this.title = title;
        this.callBack = callBack;
        provinces = new ArrayList<>();
        citys = new ArrayList<>();
        if (callBack != null) {
            callBack.getCity("");
        }
    }

    public SelectCityDialog(Context context, String title) {
        this.context = context;
        this.title = title;
        provinces = new ArrayList<>();
        citys = new ArrayList<>();
    }

    public void setCallBack(SelectCityBack callBack) {
        this.callBack = callBack;
        callBack.getCity("");
    }

    private String provinceId, cityId, provinceName, cityName;
    private WheelView wvProvince, wvCity;

    @Override
    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_wheelview_pc, null);
        View llContent;
        TextView tvTitle;
        TextView tvConfirm, tvCancel;
        llContent = dialogView.findViewById(R.id.llContent);
        tvTitle = dialogView.findViewById(R.id.tvTitle);
        tvCancel = dialogView.findViewById(R.id.tv_cancel);
        tvConfirm = dialogView.findViewById(R.id.tvConfirm);
        wvProvince = dialogView.findViewById(R.id.wv_province);
        wvCity = dialogView.findViewById(R.id.wv_city);
        citys.add("");
        tvTitle.setText(title);
        tvCancel.setOnClickListener(v -> dismiss());
        tvConfirm.setOnClickListener(v -> {
            if (callBack != null) {
                Map<String, String> params = new HashMap<>();
                params.put("provinceId", provinceId);
                params.put("provinceName", provinceName);
                params.put("cityId", cityId);
                params.put("cityName", cityName);
                callBack.select(params);
            }
            dismiss();
        });
        llContent.setOnClickListener(v -> dismiss());

        wvProvince.setOffset(2);
        wvProvince.setItems(provinces);

        wvProvince.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if (provinceList == null) {
                    return;
                }
                pIndex = selectedIndex - 2;
                provinceId = provinceList.get(pIndex).getId();
                provinceName = provinceList.get(pIndex).getName();
                if (callBack != null) {
                    callBack.getCity(provinceId);
                }
            }
        });
        wvCity.setOffset(2);
        wvCity.setItems(citys);
        wvCity.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                cIndex = selectedIndex - 2;
                if (cIndex >= 0 && cIndex < cityList.size() && cityList.get(cIndex) != null) {
                    cityId = cityList.get(cIndex).getId();
                    cityName = cityList.get(cIndex).getName();
                }
            }
        });

        dialog = new Dialog(context, R.style.dialog_bottom);
        dialog.setCancelable(true);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.show();
    }

    private void setCitys(List<AreaBean> cityList) {
        this.cityList = cityList;
        citys.clear();
        if (cityList.size() > 0) {
            for (int i = 0; i < cityList.size(); i++) {
                citys.add(cityList.get(i).getName());
            }
            cityId = cityList.get(0).getId();
            cityName = cityList.get(0).getName();
            wvCity.setItems(citys);
            wvCity.setSelection(0);
        }
    }

    private void setProvinceList(List<AreaBean> list) {
        this.provinceList = list;
        provinces.clear();
        for (int i = 0; i < provinceList.size(); i++) {
            provinces.add(provinceList.get(i).getName());
        }
        if (provinceList.size() > 0) {
            provinceId = provinceList.get(0).getId();
            provinceName = provinceList.get(0).getName();
        } else {
            return;
        }
        wvProvince.setItems(provinces);
        wvProvince.setSelection(0);
        if (callBack != null) {
            callBack.getCity(provinceId);
        }
    }


    public void setData(List<AreaBean> list) {
        if (provinceList == null || provinceList.size() < 1) {
            setProvinceList(list);
        } else {
            setCitys(list);
        }
    }


    public interface SelectCityBack {
        //获取数据
        void getCity(String provinceId);

        //回调
        void select(Map<String, String> params);
    }

}
