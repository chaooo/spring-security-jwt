package com.example.jwt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/17
 */
@Data
public class SysRole {
    /**
     * 角色ID
     */
    private Integer id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 描述
     */
    private String roleDesc;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
    /**
     * 删除标记：0未删，其他删除
     */
    private Integer delFlag;
    /**
     * 角色授权菜单
     */
    private String menuIds;

    private static final long serialVersionUID = 1L;
}
