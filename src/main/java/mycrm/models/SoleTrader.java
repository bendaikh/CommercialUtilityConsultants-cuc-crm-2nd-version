package mycrm.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "sole_trader")
public class SoleTrader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String soleName;
}
