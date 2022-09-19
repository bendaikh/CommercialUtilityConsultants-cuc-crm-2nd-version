package mycrm.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mycrm.audit.Auditable;

import java.util.List;

@Getter
@Setter
@Builder
public class LandlineSearchResult {
    private List<LandlineContract> returnedContracts;
    private long returnedContractCount;
    private long totalContractCount;
    private int totalPages;
}
