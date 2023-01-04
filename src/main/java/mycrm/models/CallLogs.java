package mycrm.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CallLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String callType;
    private String name;
    private String number;
    private String note;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

}
