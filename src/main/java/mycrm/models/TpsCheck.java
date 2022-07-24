package mycrm.models;

import mycrm.audit.Auditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "tps_check")
public class TpsCheck extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column
    private String number;
    @Column
    private String message;
    @Column
    private Integer statusCode;

    public TpsCheck() {
    }

    public TpsCheck(String number, String message, Integer statusCode) {
        this.number = number;
        this.message = message;
        this.statusCode = statusCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "TpsCheck{" +
                "id=" + id +
                ", version=" + version +
                ", number='" + number + '\'' +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
