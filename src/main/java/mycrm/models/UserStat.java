package mycrm.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "userstats")
public class UserStat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 150)
	private String username;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate = new Date();

	@Column(nullable = false, length = 50)
	private String ipAddress = "";

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User user;

	@Column(nullable = true, length = 1000)
	private String requestedURL;

	@Column(nullable = true, length = 1000)
	private String sessionId;

	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRequestedURL() {
		return requestedURL;
	}

	public void setRequestedURL(String requestedURL) {
		this.requestedURL = requestedURL;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserStat() {
		super();
	}

	public UserStat(String username, User user) {
		super();
		this.username = username;
		this.user = user;
	}

	public UserStat(String username, User user, String ipAddress, String requestedURL, String sessionId, String message) {
		this.username = username;
		this.user = user;
		this.ipAddress = ipAddress;
		this.requestedURL = requestedURL;
		this.sessionId = sessionId;
		this.message = message;
	}

	@Override
	public String toString() {
		return "UserStats [id=" + id + ", username=" + username + ", loginDate=" + loginDate + ", ipAddress="
				+ ipAddress + ", user=" + user + ", requestedURL=" + requestedURL + ", sessionId=" + sessionId + "]";
	}

}
