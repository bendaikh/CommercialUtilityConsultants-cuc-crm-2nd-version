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
@Table(name = "plugin")
public class Plugin extends Auditable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column
    private String api;

    @Column
    private String token;

    @Column
    private String url;

    @Column
    private String outputFormat;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private boolean enabled;

    @Column
    private String primaryDataset;

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

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPrimaryDataset() {
        return primaryDataset;
    }

    public void setPrimaryDataset(String primaryDataset) {
        this.primaryDataset = primaryDataset;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "id=" + id +
                ", version=" + version +
                ", api='" + api + '\'' +
                ", token='" + token + '\'' +
                ", url='" + url + '\'' +
                ", outputFormat='" + outputFormat + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", primaryDataset='" + primaryDataset + '\'' +
                '}';
    }
}
