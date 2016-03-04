package com.jredu.com.model;

/**
 * Created by Administrator on 2015/10/7 0007.
 * 每一个APP的UID和流量值
 */
public class AppModel {
    private int uid;
    private String pkgName;
    private String appName;
    private long traffic;

    public long getTraffic() {
        return traffic;
    }

    public void setTraffic(long traffic) {
        this.traffic = traffic;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
