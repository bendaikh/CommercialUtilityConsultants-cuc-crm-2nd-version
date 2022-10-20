package mycrm.models;

import lombok.Getter;
import lombok.Setter;
import mycrm.audit.Auditable;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "merchant_services_document")
public class MerchantServicesDocuments extends Auditable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private String fileName;

    private String fileTitle;

    @JoinColumn(name = "merchant_services_contract_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MerchantServicesContract merchantServicesContract;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validTo;

    private String filePath;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @Column(columnDefinition = "varchar(255) default ''")
    private String documentStatus;

    private long documentFolder;
}
