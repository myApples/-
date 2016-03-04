package com.jredu.com.model;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2015/10/6 0006.
 * 记录累计获得多少流量（临时保存用，重启手机会清空）
 */
@Table(name = "traffic_total")
public class TrafficTotalModel {
    @Id(column = "id")
    private int id;
    @Id(column = "type")
    private int type;
    @Id(column = "time")
    private long time;
    @Id(column = "traffic")
    private long traffic;

    public long getTraffic() {
        return traffic;
    }

    public void setTraffic(long traffic) {
        this.traffic = traffic;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
