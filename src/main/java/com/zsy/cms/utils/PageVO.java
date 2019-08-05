package com.zsy.cms.utils;

import java.util.List;

public class PageVO<E> {
    private int total;
    private List<E> datas;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<E> getDatas() {
        return datas;
    }

    public void setDatas(List<E> datas) {
        this.datas = datas;
    }
}
