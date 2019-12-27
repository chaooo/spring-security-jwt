package top.itdn.ssm.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * admin
 * @author 
 */
@Data
public class Admin implements Serializable {
    /**
     * 管理员ID
     */
    private Integer id;

    /**
     * 管理员账号
     */
    private String name;

    /**
     * 管理员密码
     */
    private String password;

    private static final long serialVersionUID = 1L;
}