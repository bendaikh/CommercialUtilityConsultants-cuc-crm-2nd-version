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
import javax.persistence.Version;

import org.springframework.format.annotation.DateTimeFormat;

import mycrm.audit.Auditable;

@Entity
@Table(name = "brokerNotes")
public class BrokerNote extends Auditable<User> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Broker broker;

	@Column(nullable = false, length = 200)
	private String subject;

	@Column(nullable = false, length = 5000)
	private String note;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User taggedUser;

	@Column(nullable = true, length = 1)
	private boolean completed;

	@Column(nullable = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dateCompleted;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User completedBy;

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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSubject() {
		return subject;
	}

	public User getTaggedUser() {
		return taggedUser;
	}

	public void setTaggedUser(User taggedUser) {
		this.taggedUser = taggedUser;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public User getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(User completedBy) {
		this.completedBy = completedBy;
	}

	public BrokerNote() {
		super();
	}

}
