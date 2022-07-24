package mycrm.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Builder
@ToString
public class MyCallback {
    private Date callbackTime;

    private String businessName;

    private CustomerSite customerSite;
}
