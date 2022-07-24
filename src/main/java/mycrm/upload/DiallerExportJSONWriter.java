package mycrm.upload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
public class DiallerExportJSONWriter {

    private static Logger logger = LogManager.getLogger();

    private static FileWriter file;

    public String buildJSON(Set<DiallerExportEntity> diallerExportEntities, DiallerDatasetType datasetType) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String nowDate = dtf.format(now);

        String filename = "C:\\cecfiles\\exports\\json\\" + nowDate + "-" + datasetType.getMethod() + ".json";

        writeJSON(diallerExportEntities, filename);
        logger.info("Successfully created JSON of Dialler exports: {}", filename);
        return filename;
    }

    private void writeJSON(Set<DiallerExportEntity> diallerExportEntities, String filename) {
        JSONArray diallerExportJSONArray = new JSONArray();

        diallerExportEntities.forEach(export -> {
            JSONObject diallerExport = new JSONObject();
            diallerExport.put("id", export.getId());
            diallerExport.put("leadowner", export.getLeadOwner());
            diallerExport.put("title", export.getSalutation());
            diallerExport.put("firstname", export.getFirstName());
            diallerExport.put("lastname", export.getLastName());
            diallerExport.put("workphone", export.getPhone());
            diallerExport.put("mobilephone", export.getMobile());
            diallerExport.put("address1", export.getAddr1());
            diallerExport.put("address2", export.getAddr2());
            diallerExport.put("address4", export.getCity());
            diallerExport.put("country", export.getCountry());
            diallerExport.put("postcode", export.getPostCode());
            diallerExport.put("email", export.getEmail());
            diallerExport.put("company", export.getCompany());
            diallerExport.put("leadsource", export.getLeadSource());
            diallerExport.put("leadstatus", export.getLeadStatus());
            diallerExport.put("outcomecode", export.getOutcomeCode());
            diallerExport.put("Agent_Specific", export.getAgentSpecific());
            diallerExport.put("AgentRef", export.getAgentRef());
            diallerExport.put("callback", export.getCallback());
            diallerExport.put("crm_link", export.getCrmLink());
            diallerExport.put("table", export.getCampaign());
            diallerExport.put("dataset", export.getDataset());
            diallerExport.put("CustomerReference", export.getCustomerReference());

            diallerExportJSONArray.add(diallerExport);
        });

        try {
            file = new FileWriter(filename);
            file.write(diallerExportJSONArray.toJSONString());
            logger.info("Written JSON File");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error saving Dialler JSON file: error={}", e.getMessage());
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
