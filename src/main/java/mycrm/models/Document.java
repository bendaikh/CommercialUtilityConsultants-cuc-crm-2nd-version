package mycrm.models;

import lombok.Getter;
import lombok.Setter;
import mycrm.audit.Auditable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "documents")
public class Document extends Auditable<User> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Customer customer;

	private String fileName;

	private String fileTitle;

	private String filePath;

	@Column(columnDefinition = "varchar(255) default ''")
	private String documentStatus;

	private long documentFolder;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date validFrom;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date validTo;

	public Document() {
		super();
	}

}
