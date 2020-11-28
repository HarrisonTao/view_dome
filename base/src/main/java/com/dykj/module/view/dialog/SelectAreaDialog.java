package com.dykj.module.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.module.R;
import com.dykj.module.view.WheelView;
import com.dykj.module.view.bean.AreaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcjing on 2019/3/30.
 */
public class SelectAreaDialog extends BaseDialog {

    private Context context;
    private List<AreaBean> provinceList;
    private List<AreaBean> cityList;
    private List<AreaBean> areaList;
    private String title = "";
    private int pIndex = 0, cIndex = 0, aIndex = 0;
    private SelectAreaBack callBack;
    private List<String> provinces, citys, areas;



    public SelectAreaDialog(Context context, String title, SelectAreaBack callBack) {
        this.context = context;
        this.title = title;
        this.callBack = callBack;
        provinces = new ArrayList<>();
        citys = new ArrayList<>();
        areas = new ArrayList<>();
        callBack.getArea("");
    }



    public SelectAreaDialog(Context context, String title) {
        this.context = context;
        this.title = title;
        provinces = new ArrayList<>();
        citys = new ArrayList<>();
        areas = new ArrayList<>();
    }

    public void setCallBack(SelectAreaBack callBack) {
        this.callBack = callBack;
        callBack.getArea("");
    }

    private String provinceId = "", cityId, aId, provinceName, cityName, aName;
    private WheelView wvCity, wvArea, wvProvince;

    @Override
    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_wheelview_pca, null);
        View llContent;
        TextView tvTitle;
        TextView tvConfirm, tvCancel;
        llContent = dialogView.findViewById(R.id.llContent);
        tvTitle = dialogView.findViewById(R.id.tvTitle);
        tvCancel = dialogView.findViewById(R.id.tv_cancel);
        tvConfirm = dialogView.findViewById(R.id.tvConfirm);
        wvProvince = dialogView.findViewById(R.id.wv_province);
        wvCity = dialogView.findViewById(R.id.wv_city);
        wvArea = dialogView.findViewById(R.id.wv_area);
        citys.add("");
        areas.add("");
        tvTitle.setText(title);
        tvCancel.setOnClickListener(v -> dismiss());
        tvConfirm.setOnClickListener(v -> {
            if (callBack != null) {
                callBack.select(provinceId, cityId, aId, provinceName, cityName, aName);
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
                    callBack.getArea(provinceId);
              clearCity();
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
                if (callBack != null) {
                    callBack.getArea(cityId);
                    clearArea();
                }
            }
        });
        wvArea.setOffset(2);
        wvArea.setItems(areas);
        wvArea.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                aIndex = selectedIndex - 2;
                if (aIndex >= 0 && aIndex < areas.size() && areas.get(aIndex) != null) {
                    aId = areaList.get(aIndex).getId();
                    aName = areaList.get(aIndex).getName();
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


    public boolean isShow() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }

    private void clearArea(){
        areas.clear();
        wvArea.setItems(areas);
        wvArea.setSelection(0);
    }

    private void clearCity(){
        citys.clear();
        wvCity.setItems(areas);
        wvCity.setSelection(0);
        clearArea();
    }

    public void setCitys(List<AreaBean> cityList) {
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
            if (callBack != null) {
                callBack.getArea(cityId);
                clearArea();
            }
        }
    }

    public void setAreas(List<AreaBean> areaList) {
        this.areaList = areaList;
        areas.clear();
        if (areaList.size() > 0) {
            for (int i = 0; i < areaList.size(); i++) {
                areas.add(areaList.get(i).getName());
            }
            aId = areaList.get(0).getId();
            aName = areaList.get(0).getName();
            wvArea.setItems(areas);
            wvArea.setSelection(0);
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
            callBack.getArea(provinceId);
            clearCity();
        }
    }

    public void setData(List<AreaBean> list, String pId) {
        if (StringUtils.isEmpty(pId)) {
            setProvinceList(list);
        } else if (pId.equals(provinceId)) {
            setCitys(list);
        } else {
            setAreas(list);
        }
    }


    public interface SelectAreaBack {
        //获取数据
        void getArea(String pId);

        //回调
        void select(String provinceId, String cityId, String areaId,
                    String provinceName, String cityName, String areaName);
    }
}
