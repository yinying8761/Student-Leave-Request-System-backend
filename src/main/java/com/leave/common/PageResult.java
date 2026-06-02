package com.leave.common;

import java.util.List;

public class PageResult<T> {
    private List<T> records;
    private long total;
    private long current;
    private long size;

    private PageResult() {}

    public static <T> PageResult<T> of(List<T> records, long total, long current, long size) {
        PageResult<T> r = new PageResult<>();
        r.records = records;
        r.total = total;
        r.current = current;
        r.size = size;
        return r;
    }

    public List<T> getRecords() { return records; }
    public long getTotal() { return total; }
    public long getCurrent() { return current; }
    public long getSize() { return size; }
}
