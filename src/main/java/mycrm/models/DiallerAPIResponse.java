package mycrm.models;

import java.io.Serializable;

public class DiallerAPIResponse implements Serializable {
    private boolean success;
    private String account_id;
    private String token;
    private String expire;
    private long total;
    private long bad;
    private long key;
    private String info;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DiallerAPIResponse() {
    }

    public DiallerAPIResponse(boolean success, String info) {
        this.success = success;
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getBad() {
        return bad;
    }

    public void setBad(long bad) {
        this.bad = bad;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "DiallerAPIResponse{" +
                "success=" + success +
                ", account_id='" + account_id + '\'' +
                ", token='" + token + '\'' +
                ", expire='" + expire + '\'' +
                ", total=" + total +
                ", bad=" + bad +
                ", key=" + key +
                ", info='" + info + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
