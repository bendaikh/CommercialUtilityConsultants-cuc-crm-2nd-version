package mycrm.rest.clients;

import mycrm.repositories.DiallerResponseRepository;
import mycrm.search.DiallerResponseServiceImpl;
import mycrm.upload.DiallerResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DiallerResponseServiceTest {
    @InjectMocks
    private DiallerResponseServiceImpl diallerResponseService;

    @Mock
    private DiallerResponseRepository mockDiallerResponseRepository;

    @Test
    public void shouldSaveDiallerResponse() {
        DiallerResponse diallerResponse = new DiallerResponse();
        diallerResponse.setStatusCode(200);
        diallerResponse.setResponseBody("Empty body");
        diallerResponseService.saveDiallerResponse(diallerResponse);

        verify(mockDiallerResponseRepository, times(1)).save(diallerResponse);
    }

}
