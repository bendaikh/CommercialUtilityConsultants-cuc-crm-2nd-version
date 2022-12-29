package mycrm.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CallLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String callType;
    private String name;
    private String number;
    private String note;
}
