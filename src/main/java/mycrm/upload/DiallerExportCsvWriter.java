package mycrm.upload;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
public class DiallerExportCsvWriter {

    public String buildCsv(Set<DiallerExportEntity> diallerExportEntities) throws Exception {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String nowDate = dtf.format(now);

        String filename = "C:\\cecfiles\\exports\\csv\\" + nowDate + ".csv";

        Path path = Paths.get(filename);

        writeCsv(diallerExportEntities, path);

        return filename;
    }

    private void writeCsv(Set<DiallerExportEntity> diallerExportEntities, Path path) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));

        writer.writeNext(getDiallerExportHeaders());

        diallerExportEntities.forEach(export -> {
            String[] nextLine = {
                    export.getId().toString(),
                    export.getLeadOwner(),
                    export.getSalutation(),
                    export.getFirstName(),
                    export.getLastName(),
                    export.getPhone(),
                    export.getMobile(),
                    export.getAddr1(),
                    export.getAddr2(),
                    export.getCity(),
                    export.getCountry(),
                    export.getPostCode(),
                    export.getEmail(),
                    export.getCompany(),
                    export.getLeadSource(),
                    export.getLeadStatus(),
                    export.getOutcomeCode(),
                    export.getAgentSpecific(),
                    export.getAgentRef(),
                    export.getCallback(),
                    export.getCrmLink(),
                    export.getCampaign(),
                    export.getDataset(),
                    export.getCustomerReference()
            };
            writer.writeNext(nextLine);
        });
        writer.close();
    }

    private String[] getDiallerExportHeaders() {
        String[] header = {
                "id",
                "leadowner",
                "salutation",
                "firstname",
                "lastname",
                "phone",
                "mobile",
                "address1",
                "address2",
                "city",
                "country",
                "postcode",
                "email",
                "company",
                "leadsource",
                "leadstatus",
                "outcomecode",
                "Agent_Specific",
                "AgentRef",
                "callback",
                "crm_link",
                "table",
                "dataset",
                "CustomerReference"
        };

        return header;
    }
}
