package com.hrms.common.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private PageData<T> data;

    public PageResult() {
        this.code = 200;
        this.message = "操作成功";
    }

    @Data
    public static class PageData<T> {
        private List<T> records;
        private long total;
        private long page;
        private long pageSize;

        public PageData(Page<T> page) {
            this.records = page.getRecords();
            this.total = page.getTotal();
            this.page = page.getCurrent();
            this.pageSize = page.getSize();
        }

        public PageData(List<T> records, long total, long page, long pageSize) {
            this.records = records;
            this.total = total;
            this.page = page;
            this.pageSize = pageSize;
        }
    }

    public static <T> PageResult<T> success(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setData(new PageData<>(page));
        return result;
    }

    public static <T> PageResult<T> success(List<T> records, long total, long page, long pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setData(new PageData<>(records, total, page, pageSize));
        return result;
    }

}
