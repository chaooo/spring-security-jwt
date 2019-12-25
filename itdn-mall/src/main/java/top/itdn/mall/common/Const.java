package top.itdn.mall.common;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String TOKEN_PREFIX = "token_";
    public static final int REDIS_SESSION_EXTIME = 60 * 30;//30分钟


    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public enum MessageEnum {
        SUCCESS(0, "成功"),
        ERROR(-1, "失败"),

        INSERT_SUCCESS(0, "添加成功"),
        INSERT_ERROR(1, "添加失败"),
        DELETE_SUCCESS(0, "删除成功"),
        DELETE_ERROR(2, "删除失败"),
        UPDATE_SUCCESS(0, "更新成功"),
        UPDATE_ERROR(3, "更新失败"),
        QUERY_SUCCESS(0, "查询成功"),
        QUERY_ERROR(4, "查询失败"),
        QUERY_EMPTY(100, "未找到记录"),
        NAME_OCCUPIED(101, "用户名被占用");

        private int status;
        private String msg;
        private MessageEnum(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }
        public int getStatus() {
            return status;
        }
        public String getMsg() {
            return msg;
        }
    }




}
