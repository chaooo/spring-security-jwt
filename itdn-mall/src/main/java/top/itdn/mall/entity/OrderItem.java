package top.itdn.mall.entity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderItem {
    private Integer id;

    private Integer user_id;

    private Long order_no;

    private Integer product_id;

    private String product_name;

    private String product_image;

    private BigDecimal current_unit_price;

    private Integer quantity;

    private BigDecimal total_price;

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

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name == null ? null : product_name.trim();
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image == null ? null : product_image.trim();
    }

    public BigDecimal getCurrent_unit_price() {
        return current_unit_price;
    }

    public void setCurrent_unit_price(BigDecimal current_unit_price) {
        this.current_unit_price = current_unit_price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
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