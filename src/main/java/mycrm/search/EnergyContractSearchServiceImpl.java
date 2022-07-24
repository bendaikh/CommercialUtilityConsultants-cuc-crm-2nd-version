package mycrm.search;

import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.repositories.EnergyContractSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EnergyContractSearchServiceImpl implements EnergyContractSearchService {

    private final EnergyContractSearchRepository energyContractSearchRepository;

    @Autowired
    public EnergyContractSearchServiceImpl(EnergyContractSearchRepository energyContractSearchRepository) {
        this.energyContractSearchRepository = energyContractSearchRepository;
    }

    @Override
    public ContractSearchResult getEnergyContracts(ContractSearch contractSearch, int pageNumber) {
        return this.energyContractSearchRepository.search(contractSearch, pageNumber);
    }
}
