package mycrm.upload;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CustomerImportCsvWriterTest {


    @Test
    public void testCSV() throws Exception {
        CustomerImportCsvWriter customerImportCsvWriter = new CustomerImportCsvWriter();

        List<CustomerImportEntity> customerImportEntities = new ArrayList<>();
        String csvBuildLocation = customerImportCsvWriter.buildCsv(customerImportEntities);

        //assertEquals("", csvBuildLocation);
    }

}
