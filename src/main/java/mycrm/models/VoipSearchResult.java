package mycrm.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class VoipSearchResult {
    private List<VoipContract> returnedContracts;
    private long returnedContractCount;
    private long totalContractCount;
    private int totalPages;
}
