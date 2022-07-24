package mycrm.upload;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "dialler_response")
public class DiallerResponse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int statusCode;

    @Column(length = 10000)
    private String responseBody;

    @Column
    private LocalDateTime dateCreated;

    public DiallerResponse() {
    }

    public DiallerResponse(int statusCode, String responseBody, LocalDateTime dateCreated) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.dateCreated = dateCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "DiallerResponse{" +
                "id=" + id +
                ", statusCode=" + statusCode +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
