package mycrm.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import mycrm.models.ElecCustomerContract;
import mycrm.models.GasCustomerContract;
import mycrm.services.ElecContractService;
import mycrm.services.GasContractService;

@RunWith(MockitoJUnitRunner.class)
public class PdfControllerTest {

    @Mock
    private ElecContractService mockElecContractService;

    @Mock
    private GasContractService mockGasContractService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnElectricWelcomeLetter() throws Exception {

        PdfController pdfController = new PdfController(mockElecContractService, mockGasContractService);

        ElecCustomerContract elecCustomerContract = new ElecCustomerContract();
        elecCustomerContract.setId(Long.valueOf(98));

        when(mockElecContractService.findById(any(Long.class))).thenReturn(elecCustomerContract);

        ModelAndView mav = pdfController.electricWelcomeLetter(Long.valueOf(98));
        assertEquals(elecCustomerContract, mav.getModelMap().get("elecCustomerContract"));
    }

    @Test
    public void shouldReturnGasWelcomeLetter() throws Exception {

        PdfController pdfController = new PdfController(mockElecContractService, mockGasContractService);

        GasCustomerContract gasCustomerContract = new GasCustomerContract();
        gasCustomerContract.setId(Long.valueOf(981));

        when(mockGasContractService.findById(any(Long.class))).thenReturn(gasCustomerContract);

        ModelAndView mav = pdfController.gasWelcomeLetter(Long.valueOf(981));
        assertEquals(gasCustomerContract, mav.getModelMap().get("gasCustomerContract"));
    }

}
