package com.hrms.common.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class PageQuery {

    private int page = 1;
    private int size = 10;
    private String sort;

    /** Accept pageSize as alias for size */
    public void setPageSize(int pageSize) {
        this.size = pageSize;
    }
    public int getPageSize() {
        return this.size;
    }

    public <T> Page<T> toPage() {
        Page<T> page = new Page<>(this.page, this.size);
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0].trim();
                String order = sortParams[1].trim().toLowerCase();
                if ("asc".equals(order)) {
                    page.addOrder(OrderItem.asc(field));
                } else if ("desc".equals(order)) {
                    page.addOrder(OrderItem.desc(field));
                }
            } else {
                page.addOrder(OrderItem.asc(sort.trim()));
            }
        }
        return page;
    }
}
