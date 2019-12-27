package top.itdn.shiro.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * user_roles
 * @author 
 */
@Data
public class User implements Serializable {
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
    private String password;

    private static final long serialVersionUID = 1L;
}