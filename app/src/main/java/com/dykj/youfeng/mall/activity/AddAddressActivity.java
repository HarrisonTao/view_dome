package com.dykj.youfeng.mall.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.AddressBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.AppConstant;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.dykj.module.view.bean.AreaBean;
import com.dykj.module.view.dialog.SelectAreaDialog;
import com.tencent.mmkv.MMKV;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    private AddressBean bean;

    @Override
    public int intiLayout() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initData() {
        params = new HashMap<>();
    }

    @Override
    public void doBusiness() {
        if (getIntentData()!=null) {
            bean = (AddressBean) getIntentData();
        }
        if (bean == null) {
            initTitle("新增地址");
        } else {
            initTitle("编辑地址");
            tvArea.setText(String.format("%s %s %s", bean.province, bean.city, bean.area));
            params.put("province_id", bean.province_id);
            params.put("city_id", bean.city_id);
            params.put("area_id", bean.area_id);
            etAddress.setText(bean.area_info);
            etPhone.setText(bean.tel_phone);
            etName.setText(bean.true_name);
            isDefault = "1".equals(bean.is_default);
            if (isDefault) {
                tvDefault.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.btn_xuanz_c), null);
            } else {
                tvDefault.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.btn_xuanz_big), null);
            }
        }
    }

    private boolean isDefault = false;

    @OnClick({R.id.tv_area, R.id.tv_default, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_area:
                selectArea();
                break;
            case R.id.tv_default:
                isDefault = !isDefault;
                if (isDefault) {
                    tvDefault.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.btn_xuanz_c), null);
                } else {
                    tvDefault.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.btn_xuanz_big), null);
                }
                break;
            case R.id.tv_commit:
                String name = etName.getText().toString();
                if (StringUtils.isEmpty(name)) {
                    DyToast.getInstance().warning("请输入收货人姓名");
                    return;
                }
                String phone = etPhone.getText().toString();
                if (StringUtils.isEmpty(phone)) {
                    DyToast.getInstance().warning("请输入电话号码");
                    return;
                }
                if (!RegexUtils.isMobileSimple(phone)) {
                    DyToast.getInstance().warning("手机号码格式不正确");
                    return;
                }
                if (StringUtils.isEmpty(params.get("area_id"))) {
                    DyToast.getInstance().warning("请选择省市区");
                    return;
                }
                String address = etAddress.getText().toString();
                if (StringUtils.isEmpty(address)) {
                    DyToast.getInstance().warning("请输入详细地址");
                    return;
                }
                params.put("true_name", name);
                params.put("tel_phone", phone);
                params.put("area_info", address);
                params.put("is_default", isDefault ? "1" : "0");
                String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
                params.put("token", mToken);
                if (bean == null) {
                    commit();
                } else {
                    edit();
                }

                break;
        }
    }


    private void commit() {
        HttpFactory.getMallInstance().addArea(params)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AddAddressActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            //设置数据
                            DyToast.getInstance().success(data.getMessage());
                            setResult(1);
                            finish();
                        } else {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));

    }

    private void edit() {
        params.put("address_id",bean.address_id);
        HttpFactory.getMallInstance().editArea(params)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(AddAddressActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            //设置数据
                            DyToast.getInstance().success(data.getMessage());
                            setResult(1);
                            finish();
                        } else {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));


    }

    private Map<String, String> params;

    /**
     * 省市区选择框
     */
    private void selectArea() {
        //1、初始化
        SelectAreaDialog selectAreaPopup = new SelectAreaDialog(this, "选择地区");
        //2、设置监听回调
        selectAreaPopup.setCallBack(new SelectAreaDialog.SelectAreaBack() {
            @Override
            public void getArea(String pId) {
                //通过网络请求获取数据
                HttpFactory.getMallInstance().getArea(pId)
                        .compose(HttpObserver.schedulers(getAty()))
                        .as(HttpObserver.life(AddAddressActivity.this))
                        .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<AreaBean>>>() {
                            @Override
                            public void success(HttpResponseData<List<AreaBean>> data) {
                                if (data.status.equals(AppConstant.SUCCESS)) {
                                    //设置数据
                                    selectAreaPopup.setData(data.getData(), pId);
                                }
                            }

                            @Override
                            public void error(Throwable data) {
                                MyLogger.dLog().e(data);
                                DyToast.getInstance().error(data.getMessage());
                            }
                        }));
            }

            @Override
            public void select(String pId, String cId, String aId, String provinceName, String cityName, String areaName) {
                params.put("province_id", pId);
                params.put("city_id", cId);
                params.put("area_id", aId);
                tvArea.setText(provinceName + " " + cityName + " " + areaName);
            }
        });
        //3、显示
        selectAreaPopup.setLifecycle(getLifecycle()).show();
    }


}
