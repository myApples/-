package com.jredu.com.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2015/9/24 0024.
 */
public class RunningProcess {
    private int pid,uid;
    private String processName;
    private String appName;//应用名字
    private long memorySize;//应用大小
    private Drawable icon;//图片
    private boolean isSystem;//是否是系统应用
    private boolean isSelected = false;






    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public long getMemorySize() {
        return memorySize;
    }
    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public boolean isSystem() {
        return isSystem;
    }
    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }
    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }


}
