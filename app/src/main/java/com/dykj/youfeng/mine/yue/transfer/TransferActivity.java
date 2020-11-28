package com.dykj.youfeng.mine.yue.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.dykj.module.base.BaseActivity;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 转账
 *
 * @author zhaoyanhui
 */
public class TransferActivity extends BaseActivity {


    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.userPhoneEdit)
    EditText userPhoneEdit;
    @BindView(R.id.userNameEdit)
    EditText userNameEdit;

    @Override
    public int intiLayout() {
        return R.layout.activity_transfer;

    }

    @Override
    public void initData() {
        initTitle("转账");
    }

    @Override
    public void doBusiness() {

    }


    @OnClick(R.id.tv_next)
    public void onViewClicked() {

        String name = userNameEdit.getText().toString().trim();
        String userPhone = userPhoneEdit.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            DyToast.getInstance().warning("请输入姓名!");
            return;
        }
        if (TextUtils.isEmpty(userPhone)) {
            DyToast.getInstance().warning("请输入对方的账户!");
            return;
        }

        Intent intent=new Intent(this,ConfirmTransferActivity.class);
               intent.putExtra("name",name);
               intent.putExtra("userPhone",userPhone);
       startActivity(intent);

    }


}
