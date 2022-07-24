package mycrm.audit.history;

import mycrm.entitylisteners.Action;
import mycrm.models.Customer;
import mycrm.models.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class CustomerHistory {

	@Id
	@GeneratedValue
    private Long id;

	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Customer customer;

    @Column(length = 20000)
	private String content;

	@CreatedBy
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private User modifiedBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Enumerated(EnumType.STRING)
	private Action action;

    public Long getId() {
		return id;
	}

    public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public CustomerHistory() {
	}

	public CustomerHistory(Customer customer, Action action) {
		super();
		this.customer = customer;
		this.content = customer.toString();
		this.action = action;
	}

	@Override
	public String toString() {
		return "CustomerHistory [id=" + id + ", customer=" + customer + ", content=" + content + ", modifiedBy="
				+ modifiedBy + ", modifiedDate=" + modifiedDate + ", action=" + action + "]";
	}

}
