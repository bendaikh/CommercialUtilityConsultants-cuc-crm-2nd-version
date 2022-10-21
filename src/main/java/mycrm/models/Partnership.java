package mycrm.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "partnership")
public class Partnership {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String partnershipID;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String contactNumber;
    private String emailAddress;
    private String partnershipPercentage;
}
