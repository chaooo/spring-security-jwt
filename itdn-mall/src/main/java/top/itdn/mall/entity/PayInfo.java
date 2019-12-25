package top.itdn.mall.entity;

import java.util.Date;

public class PayInfo {
    private Integer id;

    private Integer user_id;

    private Long order_no;

    private Integer pay_platform;

    private String platform_number;

    private String platform_status;

    private Date create_time;

    private Date update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Long getOrder_no() {
        return order_no;
    }

    public void setOrder_no(Long order_no) {
        this.order_no = order_no;
    }

    public Integer getPay_platform() {
        return pay_platform;
    }

    public void setPay_platform(Integer pay_platform) {
        this.pay_platform = pay_platform;
    }

    public String getPlatform_number() {
        return platform_number;
    }

    public void setPlatform_number(String platform_number) {
        this.platform_number = platform_number == null ? null : platform_number.trim();
    }

    public String getPlatform_status() {
        return platform_status;
    }

    public void setPlatform_status(String platform_status) {
        this.platform_status = platform_status == null ? null : platform_status.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}