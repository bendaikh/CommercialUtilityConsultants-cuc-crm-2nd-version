package mycrm.services;

import mycrm.repositories.ElecContractRepository;
import mycrm.repositories.GasContractRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
@Primary
public class ContractServiceImpl implements ContractService {

    private final ElecContractRepository elecContractRepository;
    private final GasContractRepository gasContractRepository;

    public ContractServiceImpl(ElecContractRepository elecContractRepository, GasContractRepository gasContractRepository) {
        this.elecContractRepository = elecContractRepository;
        this.gasContractRepository = gasContractRepository;
    }

    @Override
    public Set<String> getCampaigns() {
        Set<String> campaigns = new TreeSet<>();

        campaigns.addAll(elecContractRepository.getDistinctCampaigns());
        campaigns.addAll(gasContractRepository.getDistinctCampaigns());

        return campaigns;
    }
}
