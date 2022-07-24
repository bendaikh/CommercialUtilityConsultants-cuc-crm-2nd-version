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
@Table(name = "customerNotes")
public class CompleteCustomerNote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true, length = 1)
	private boolean completed;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dateCompleted = new Date();

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User completedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(User completedBy) {
		this.completedBy = completedBy;
	}

	public CompleteCustomerNote() {
		super();
	}

	public CompleteCustomerNote(Long id, boolean completed, Date dateCompleted, User completedBy) {
		super();
		this.id = id;
		this.completed = completed;
		this.dateCompleted = dateCompleted;
		this.completedBy = completedBy;
	}

	@Override
	public String toString() {
		return "CompleteCustomerNote [id=" + id + ", completed=" + completed + ", dateCompleted=" + dateCompleted
				+ ", completedBy=" + completedBy + "]";
	}

}
