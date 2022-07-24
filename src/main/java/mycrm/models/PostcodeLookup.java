package mycrm.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "postcode_lookup")
public class PostcodeLookup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = true, length = 10, name = "PostCode")
	private String postCode;
	
	@Column(nullable = true, length = 5, name = "EXZ_id")
	private String exzId;
	
	@Column(nullable = true, length = 5, name = "LDZ_id")
	private String ldzId;

	public Long getId() {
		return id;
	}

	public String getPostCode() {
		return postCode;
	}

	public String getExzId() {
		return exzId;
	}

	public String getLdzId() {
		return ldzId;
	}
}
