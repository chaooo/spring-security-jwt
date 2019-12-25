package top.itdn.mall.common;

import java.io.Serializable;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */
public class ResponseJson implements Serializable {
    private int status;
    private String msg;
    private Object data;

    public ResponseJson() {}

    public ResponseJson(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "RseponseJson{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
