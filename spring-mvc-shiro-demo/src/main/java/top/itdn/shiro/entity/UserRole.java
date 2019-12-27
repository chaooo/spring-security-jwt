package top.itdn.shiro.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * user_roles
 * @author 
 */
@Data
public class UserRole implements Serializable {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String roleName;

    private static final long serialVersionUID = 1L;
}