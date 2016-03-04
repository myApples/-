package com.jredu.com.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/28 0028.
 */
@DatabaseTable(tableName = "listman")
public class ListMen implements Serializable{
    @DatabaseField
    private String listMen;
    @DatabaseField
    private String listNumber;
    @DatabaseField
    private boolean isCheckBox;
    @DatabaseField
    private boolean isDelete;
    @DatabaseField( generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    private List<ListMen> nodes;

    public List<ListMen> getNodes() {
        return nodes;
    }

    public void setNodes(List<ListMen> nodes) {
        this.nodes = nodes;
    }

    public boolean isCheckBox() {
        return isCheckBox;
    }

    public void setIsCheckBox(boolean isCheckBox) {
        this.isCheckBox = isCheckBox;
    }

    public String getListNumber() {
        return listNumber;
    }

    public void setListNumber(String listNumber) {
        this.listNumber = listNumber;
    }

    public String getListMen() {
        return listMen;
    }

    public void setListMen(String listMen) {
        this.listMen = listMen;
    }

    public ListMen(String listMen, String listNumber) {
        this.listMen = listMen;
        this.listNumber = listNumber;
    }

    public ListMen() {
       super();
    }
}
