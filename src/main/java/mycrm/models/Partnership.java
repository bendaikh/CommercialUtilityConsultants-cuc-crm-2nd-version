package mycrm.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
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
    @Column(nullable = true, length = 150)
    private String addressLine1;

    @Column(nullable = true, length = 150)
    private String addressLine2;

    @Field
    @Column(nullable = true, length = 150)
    private String city;

    @Field
    @Column(nullable = true, length = 4)
    private String postcodeIn;

    @Field
    @Column(nullable = true, length = 4)
    private String postcodeOut;

    private String partnershipID;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String contactNumber;
    private String emailAddress;
    private String partnershipPercentage;
}
