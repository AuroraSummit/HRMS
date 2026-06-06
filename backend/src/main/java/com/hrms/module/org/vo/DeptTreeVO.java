package com.hrms.module.org.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeptTreeVO {
    private Long id;
    private String label;
    private List<DeptTreeVO> children;
}
