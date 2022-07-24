package mycrm.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class BeanUtilTest {
    
    @InjectMocks
    private BeanUtil beanUtil;
    
    private static ApplicationContext context;

    @Test
    public final void test() {
        beanUtil.setApplicationContext(context);
    }

}
