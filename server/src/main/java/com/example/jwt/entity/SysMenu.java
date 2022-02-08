package com.example.jwt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/10
 */
@Data
public class SysMenu {
    /**
     * Id
     */
    private Integer id;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 子菜单数量
     */
    private Integer childCount;
    /**
     * 父级菜单Id
     */
    private Integer parentId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    /**
     * 隐藏状态：0显示，1隐藏
     */
    private Integer hidden;
    /**
     * 状态：0启用，1停用
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 删除标记：0未删，其他删除
     */
    private Integer delFlag;
    /**
     * 按钮权限
     */
    private List<SysPermission> permissions;
    /**
     * 按钮权限JSON数据：[
     *            { "id":         "ID"
     *              "title":      "按钮标题",
     *              "name":       "权限标识",
     *            },
     *            {多条数据结构同上}
     *         ]
     */
    private String permissionJson;

    private static final long serialVersionUID = 1L;
}
