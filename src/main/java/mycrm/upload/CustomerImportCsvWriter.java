package mycrm.upload;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CustomerImportCsvWriter {

    public String buildCsv(List<CustomerImportEntity> customerImportEntities) throws Exception {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String nowDate = dtf.format(now);

        String filename = "C:\\cecfiles\\imports\\unsuccessful-imports-" + nowDate + ".csv";

        Path path = Paths.get(filename);

        writeCsv(customerImportEntities, path);

        return filename;
    }


    private void writeCsv(List<CustomerImportEntity> customerImportEntities, Path path) throws Exception {

        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));

        writer.writeNext(getUnsuccessfulImportHeaders());

        for (CustomerImportEntity entity : customerImportEntities) {
            String[] nextLine = {
                    entity.getBusinessName(),
                    entity.getBusinessAddressOne(),
                    entity.getBusinessAddressTwo(),
                    entity.getBusinessCity(),
                    entity.getBusinessPostcode(),
                    entity.getLtdRegistrationNumber(),
                    entity.getCompanyRegistrationNumber(),
                    (entity.getSoleTrader() != null ? entity.getSoleTrader().toString() : ""),
                    entity.getStContactName(),
                    entity.getStAddressOne(),
                    entity.getStAddressTwo(),
                    entity.getStCity(),
                    entity.getStPostcode(),
                    entity.getContactNumber(),
                    entity.getMobileNumber(),
                    entity.getEmailAddress(),
                    entity.getMpr(),
                    entity.getMpanLineOne(),
                    entity.getMpanLineTwo(),
                    entity.getGasMeterUtility(),
                    entity.getGasSupplier(),
                    entity.getGasUnitRate(),
                    entity.getGasStandingCharge(),
                    entity.getGasStandingChargeFrequency(),
                    entity.getGasStartDate(),
                    entity.getGasEndDate(),
                    entity.getGasEstimatedAnnualQuantity(),
                    entity.getElectricMeterUtility(),
                    entity.getElectricitySupplier(),
                    entity.getElectricityDayRate(),
                    entity.getElectricityNightRate(),
                    entity.getElectricityEveningWeekendRate(),
                    entity.getElectricityFeedInTariff(),
                    entity.getElectricityStandingCharge(),
                    entity.getElectricityStandingChargeFrequency(),
                    entity.getElectricityStartDate(),
                    entity.getElectricityEndDate(),
                    entity.getElectricityEstimatedAnnualQuantity(),
                    entity.getCampaign(),
                    entity.getErrorMessage()
            };
            writer.writeNext(nextLine);
        }

        writer.close();
    }

    private String[] getUnsuccessfulImportHeaders() {

        String[] header = {
                "Business Name",
                "Address Line 1",
                "Address Line 2",
                "City",
                "Postcode",
                "LTD Registration Number",
                "Company Registration Number",
                "Sole Trader",
                "ST Contact Name",
                "ST Address Line 1",
                "ST Address Line 2",
                "ST City",
                "ST Postcode",
                "Contact Number",
                "Mobile Number",
                "Email Address",
                "MPR",
                "MPAN Line One",
                "MPAN Line Two",
                "Gas Meter Utility",
                "Gas Supplier",
                "Gas Unit Rate",
                "Gas Standing Charge",
                "Gas Standing Charge Frequency",
                "Gas Start Date",
                "Gas End Date",
                "Gas Estimated Annual Quantity",
                "Electric Meter Utility",
                "Electricity Supplier",
                "Electricity Day Rate",
                "Electricity Night Rate",
                "Electricity Evening/Weekend Rate",
                "Electricity Feed In Tariff",
                "Electricity Standing Charge",
                "Electricity Standing Charge Frequency",
                "Electricity Start Date",
                "Electricity End Date",
                "Electricity Estimated Annual Quantity",
                "Campaign",
                "Error Message"
        };

        return header;

    }
}
