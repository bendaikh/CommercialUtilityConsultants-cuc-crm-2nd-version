package mycrm.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MerchantServicesSearchResult {
    private List<MerchantServicesContract> returnedContracts;
    private long returnedContractCount;
    private long totalContractCount;
    private int totalPages;

}
