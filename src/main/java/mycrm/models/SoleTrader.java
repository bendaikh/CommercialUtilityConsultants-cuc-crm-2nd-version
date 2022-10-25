package mycrm.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "sole_trader")
public class SoleTrader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String soleName;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
}
