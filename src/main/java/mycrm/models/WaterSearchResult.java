package mycrm.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class WaterSearchResult {
    private List<WaterContract> returnedContracts;
    private long returnedContractCount;
    private long totalContractCount;
    private int totalPages;
}
