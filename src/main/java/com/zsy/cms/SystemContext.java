package com.zsy.cms;

public class SystemContext {
    private static ThreadLocal offset = new ThreadLocal();
    private static ThreadLocal pageSize = new ThreadLocal();

    public static int getOffset() {
        Integer _offset = (Integer)offset.get();
        if(_offset == null) {
            return 0;
        }
        return _offset;
    }

    public static void setOffset(int _offset) {
        offset.set(_offset);
    }

    public static void removeOffset() {
        offset.remove();
    }

    public static int getPageSize() {
        Integer _pageSize = (Integer)pageSize.get();
        if(_pageSize == null) {
            return Integer.MAX_VALUE;
        }
        return _pageSize;
    }

    public static void setPageSize(int _pageSize) {
        pageSize.set(_pageSize);
    }

    public static void removePageSize() {
        pageSize.remove();
    }
}
