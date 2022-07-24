package mycrm.entitylisteners;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.Customer;
import mycrm.services.BeanUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BeanUtil.class)
public class CustomerEntityListenerTest {

    @InjectMocks
    private CustomerEntityListener customerEntityListener;  

    @Mock
    private EntityManager entityManager;
    
    @Before
    public void setUp(){
        PowerMockito.mockStatic(BeanUtil.class);
        PowerMockito.when(BeanUtil.getBean(any())).thenReturn(entityManager);
    }
    
    @Test
    public void shouldPostPersist() {        
        customerEntityListener.postPersist(aCustomer());
        
        verify(entityManager, times(1)).persist(any(CustomerHistory.class));
    }

    @Test
    public void shouldPreUpdate() {
        customerEntityListener.preUpdate(aCustomer());
        verify(entityManager, times(1)).persist(any(CustomerHistory.class));
    }

//    @Test
//    public void shouldPreRemove() {
//        customerEntityListener.preRemove(aCustomer());
//        verify(entityManager, times(1)).persist(any(CustomerHistory.class));
//    }

    private Customer aCustomer() {
        return new Customer();
    }

}
