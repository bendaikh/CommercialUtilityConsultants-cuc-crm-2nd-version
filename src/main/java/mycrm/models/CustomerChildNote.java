package mycrm.models;

import mycrm.audit.Auditable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "customerChildNotes")
public class CustomerChildNote extends Auditable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CustomerNote customerNote;

    @Column(nullable = false, length = 5000)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private User replyTaggedUser;

    @Column(length = 1)
    private boolean replyCompleted;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replyDateCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User replyCompletedBy;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date replyDueByDate;

    @Column
    private String replyPriority;

    @Column
    private String replyRelatedTo;

    @Column(nullable = false, length = 1)
    private boolean quickWin;

    public CustomerChildNote() {
    }

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

    public CustomerNote getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(CustomerNote customerNote) {
        this.customerNote = customerNote;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getReplyTaggedUser() {
        return replyTaggedUser;
    }

    public void setReplyTaggedUser(User replyTaggedUser) {
        this.replyTaggedUser = replyTaggedUser;
    }

    public boolean isReplyCompleted() {
        return replyCompleted;
    }

    public void setReplyCompleted(boolean replyCompleted) {
        this.replyCompleted = replyCompleted;
    }

    public Date getReplyDateCompleted() {
        return replyDateCompleted;
    }

    public void setReplyDateCompleted(Date replyDateCompleted) {
        this.replyDateCompleted = replyDateCompleted;
    }

    public User getReplyCompletedBy() {
        return replyCompletedBy;
    }

    public void setReplyCompletedBy(User replyCompletedBy) {
        this.replyCompletedBy = replyCompletedBy;
    }

    public Date getReplyDueByDate() {
        return replyDueByDate;
    }

    public void setReplyDueByDate(Date replyDueByDate) {
        this.replyDueByDate = replyDueByDate;
    }

    public String getReplyPriority() {
        return replyPriority;
    }

    public void setReplyPriority(String replyPriority) {
        this.replyPriority = replyPriority;
    }

    public String getReplyRelatedTo() {
        return replyRelatedTo;
    }

    public void setReplyRelatedTo(String replyRelatedTo) {
        this.replyRelatedTo = replyRelatedTo;
    }

    public boolean isQuickWin() {
        return quickWin;
    }

    public void setQuickWin(boolean quickWin) {
        this.quickWin = quickWin;
    }

    public String getPriorityClass() {
        // show due today in green and older than today in red
        if (getReplyDueByDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (format.format(getReplyDueByDate()).equals(format.format(getToday()))) {
                return "bright-green-bg";
            }

            if (getReplyDueByDate().before(getToday())) {
                return "bright-red-bg";
            }
        }

        if (!StringUtils.hasText(replyPriority)) {
            return "";
        }

        switch (replyPriority) {
            case "HIGH":
                return "danger";

            case "MEDIUM":
                return "warning";

            case "LOW":
                return "success";

            default:
                return "";
        }
    }

    public Date getToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        return today.getTime();
    }
}
