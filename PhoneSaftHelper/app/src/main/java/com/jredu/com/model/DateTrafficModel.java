package com.jredu.com.model;

/**
 * Created by Administrator on 2015/10/7 0007.
 * 时间与流量的model
 */
public class DateTrafficModel {
    private long date;
    private long traffic;

    public long getTraffic() {
        return traffic;
    }

    public void setTraffic(long traffic) {
        this.traffic = traffic;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
