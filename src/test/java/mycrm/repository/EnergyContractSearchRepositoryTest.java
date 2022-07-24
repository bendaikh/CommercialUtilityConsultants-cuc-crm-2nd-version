package mycrm.repository;

import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.GasCustomerContract;
import mycrm.repositories.EnergyContractSearchRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Search.class)
public class EnergyContractSearchRepositoryTest {

    @InjectMocks
    private EnergyContractSearchRepository energyContractSearchRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Ignore
    @Test
    public void shouldReturnSearchListOfCustomerContracts() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setAccountNumber("blah");

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setFromDate("2018-01-01");
        contractSearch.setToDate("2018-12-01");
        contractSearch.setQ("restaurant");

        PowerMockito.mockStatic(Search.class);

        FullTextEntityManager fullTextEntityManager = mock(FullTextEntityManager.class, Mockito.RETURNS_DEEP_STUBS);

        javax.persistence.Query jpaQuery = mock(FullTextQuery.class);
        Query queryTotal = mock(Query.class);

        PowerMockito.when(Search.getFullTextEntityManager(any(EntityManager.class))).thenReturn(fullTextEntityManager);
        PowerMockito
                .when(fullTextEntityManager.createFullTextQuery(any(org.apache.lucene.search.Query.class), any()))
                .thenReturn((FullTextQuery) jpaQuery);
        PowerMockito.when(jpaQuery.getResultList()).thenReturn(Arrays.asList(gasCustomerContract));
        PowerMockito.when(entityManager.createQuery(any(String.class))).thenReturn(queryTotal);
        PowerMockito.when(queryTotal.getSingleResult()).thenReturn(1l);

        List<Contract> search = energyContractSearchRepository.search(contractSearch, 1).getReturnedContracts();
        assertEquals(gasCustomerContract.getAccountNumber(), search.get(0).getAccountNumber());
    }

    @Ignore
    @Test
    public void shouldReturnSearchListOfCustomerContractsWhenNoQ() {
        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setAccountNumber("blah");

        ContractSearch contractSearch = new ContractSearch();
        contractSearch.setFromDate("2018-01-01");
        contractSearch.setToDate("2018-12-01");

        PowerMockito.mockStatic(Search.class);

        FullTextEntityManager fullTextEntityManager = mock(FullTextEntityManager.class, Mockito.RETURNS_DEEP_STUBS);

        javax.persistence.Query jpaQuery = mock(FullTextQuery.class);

        PowerMockito.when(Search.getFullTextEntityManager(any(EntityManager.class))).thenReturn(fullTextEntityManager);
        PowerMockito
                .when(fullTextEntityManager.createFullTextQuery(any(org.apache.lucene.search.Query.class), any()))
                .thenReturn((FullTextQuery) jpaQuery);
        PowerMockito.when(jpaQuery.getResultList()).thenReturn(Arrays.asList(gasCustomerContract));

        List<Contract> search = energyContractSearchRepository.search(any(ContractSearch.class), eq(1)).getReturnedContracts();
        assertEquals(gasCustomerContract.getAccountNumber(), search.get(0).getAccountNumber());
        assertNotNull(contractSearch.toString());
    }

}
