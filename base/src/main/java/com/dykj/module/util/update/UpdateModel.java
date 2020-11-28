package com.dykj.module.util.update;

import android.content.Intent;

import com.cretin.www.cretinautoupdatelibrary.model.LibraryUpdateEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cretin on 2017/4/21.
 * 这个model是调用 http://www.cretinzp.com/system/versioninfo 这个接口之后返回的数据
 */
public class UpdateModel implements LibraryUpdateEntity {




    /**
     * id : test
     * page : 1
     * rows : 10
     * isForceUpdateFlag : 0
     * preBaselineCode : 0
     * versionName : V1.0.1
     * versionCode : 3
     * downurl : http://120.24.5.102/Webconfig/frj01_211_jiagu_sign.apk
     * updateLog : 1、修复bug
     * size : 10291218
     * hasAffectCodes : 1|2
     * createTime : 1489651956000
     * iosVersion : 1
     */

    private int isForceUpdate;
    private int preBaselineCode;
    private String versionName;
    private String size;
    private String hasAffectCodes;
    private long createTime;
    private int iosVersion;
    /**
     * status : 9999
     * message : 请求成功
     * info : [{"id":24,"url":"http://ckt.diyunkeji.com/ckt.apk","version_code":"312","remark":"新增中介养卡功能。 首页、发现页面优化。 抽奖分享功能优化。 "}]
     */

    private String status;
    private String message;
    private List<InfoBean> info;

    public int getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public int getPreBaselineCode() {
        return preBaselineCode;
    }

    public void setPreBaselineCode(int preBaselineCode) {
        this.preBaselineCode = preBaselineCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return info.get(0).getVersion_code();
    }



    public String getDownurl() {
        return info.get(0).getUrl();
    }



    public String getUpdateLog() {
        return info.get(0).getRemarkX();
    }



    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHasAffectCodes() {
        return hasAffectCodes;
    }

    public void setHasAffectCodes(String hasAffectCodes) {
        this.hasAffectCodes = hasAffectCodes;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(int iosVersion) {
        this.iosVersion = iosVersion;
    }

    @Override
    public int getAppVersionCode() {
        return getVersionCode();
    }

    @Override
    public int forceAppUpdateFlag() {
        return getIsForceUpdate();
    }

    @Override
    public String getAppVersionName() {
        return getVersionName().replaceFirst("v", "");
    }

    @Override
    public String getAppApkUrls() {
        return getDownurl();
    }

    @Override
    public String getAppUpdateLog() {
        return getUpdateLog();
    }

    @Override
    public String getAppApkSize() {
        return getSize();
    }

    @Override
    public String getAppHasAffectCodes() {
        return getHasAffectCodes();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 24
         * url : http://ckt.diyunkeji.com/ckt.apk
         * version_code : 312
         * remark : 新增中介养卡功能。 首页、发现页面优化。 抽奖分享功能优化。
         */

        private int id;
        private String url;
        private String version_code;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getVersion_code() {
            return Integer.parseInt(version_code);
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getRemarkX() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
