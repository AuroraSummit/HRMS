package com.hrms.module.system.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class Menu extends BaseEntity {
    private String name;
    private Long parentId;
    private String path;
    private String component;
    private String icon;
    private Integer sort;
    private Integer type;
    private String permission;
    private Integer status;

    /** 前端树组件需要的 title（等同于 name） */
    @TableField(exist = false)
    private String title;

    /** 前端树组件需要的子菜单 */
    @TableField(exist = false)
    private List<Menu> children;

    /** 前端需要的 visible 字段，默认可见 */
    @TableField(exist = false)
    private Integer visible = 1;

    public String getTitle() {
        return name;
    }
}
