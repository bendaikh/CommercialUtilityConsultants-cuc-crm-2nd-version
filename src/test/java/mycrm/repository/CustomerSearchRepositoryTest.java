package mycrm.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import mycrm.models.Customer;
import mycrm.models.CustomerSearch;
import mycrm.models.CustomerSearchResult;
import mycrm.repositories.CustomerSearchRepository;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Search.class)
public class CustomerSearchRepositoryTest {

    @InjectMocks
    private CustomerSearchRepository customerSearchRepository;

    @Mock
    private EntityManager entityManager;

    @Ignore
    @Test
    public void shouldReturnSearchListOfLeads() {
        Customer customer = new Customer();
        customer.setId(111l);
        
        CustomerSearchResult customerSearchResult = new CustomerSearchResult();
        customerSearchResult.setTotalCustomers(1);
        customerSearchResult.setReturnedCustomerCount(1l);
        customerSearchResult.setReturnedCustomers(Arrays.asList(customer));
        customerSearchResult.setTotalPages(1);

        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setQ("restaurant");

        PowerMockito.mockStatic(Search.class);

        FullTextEntityManager fullTextEntityManager = mock(FullTextEntityManager.class, Mockito.RETURNS_DEEP_STUBS);

        Query jpaQuery = mock(FullTextQuery.class);
        Query queryTotal = mock(Query.class);

        PowerMockito.when(Search.getFullTextEntityManager(any(EntityManager.class))).thenReturn(fullTextEntityManager);
        PowerMockito
                .when(fullTextEntityManager.createFullTextQuery(any(org.apache.lucene.search.Query.class), any()))
                .thenReturn((FullTextQuery) jpaQuery);
        PowerMockito.when(jpaQuery.getResultList()).thenReturn(Arrays.asList(customer));
        PowerMockito.when(entityManager.createQuery(any(String.class))).thenReturn(queryTotal);
        PowerMockito.when(queryTotal.getSingleResult()).thenReturn(1l);

        CustomerSearchResult search = customerSearchRepository.search(customerSearch, 1);
        assertEquals(customerSearchResult.getTotalCustomerCount(), search.getTotalCustomerCount());
        assertEquals(customerSearchResult.getReturnedCustomerCount(), search.getReturnedCustomerCount());
        assertEquals(customerSearchResult.getReturnedCustomers(), search.getReturnedCustomers());
        assertEquals(customerSearchResult.getTotalPages(), search.getTotalPages());
    }

    @Ignore
    @Test
    public void shouldReturnSearchListOfLeadsWhenNoQ() {
        Customer customer = new Customer();
        customer.setId(111l);
        
        CustomerSearchResult customerSearchResult = new CustomerSearchResult();
        customerSearchResult.setTotalCustomers(1);
        customerSearchResult.setReturnedCustomerCount(1l);
        customerSearchResult.setReturnedCustomers(Arrays.asList(customer));
        customerSearchResult.setTotalPages(1);

        CustomerSearch customerSearch = new CustomerSearch();

        PowerMockito.mockStatic(Search.class);

        FullTextEntityManager fullTextEntityManager = mock(FullTextEntityManager.class, Mockito.RETURNS_DEEP_STUBS);

        Query jpaQuery = mock(FullTextQuery.class);
        Query queryTotal = mock(Query.class);

        PowerMockito.when(Search.getFullTextEntityManager(any(EntityManager.class))).thenReturn(fullTextEntityManager);
        PowerMockito
                .when(fullTextEntityManager.createFullTextQuery(any(org.apache.lucene.search.Query.class), any()))
                .thenReturn((FullTextQuery) jpaQuery);
        PowerMockito.when(jpaQuery.getResultList()).thenReturn(Arrays.asList(customer));
        PowerMockito.when(entityManager.createQuery(any(String.class))).thenReturn(queryTotal);
        PowerMockito.when(queryTotal.getSingleResult()).thenReturn(1l);

        CustomerSearchResult search = customerSearchRepository.search(customerSearch, 1);
        assertEquals(customerSearchResult.getTotalCustomerCount(), search.getTotalCustomerCount());
        assertEquals(customerSearchResult.getReturnedCustomerCount(), search.getReturnedCustomerCount());
        assertEquals(customerSearchResult.getReturnedCustomers(), search.getReturnedCustomers());
        assertEquals(customerSearchResult.getTotalPages(), search.getTotalPages());
        assertNotNull(customerSearch.toString());
    }

}
