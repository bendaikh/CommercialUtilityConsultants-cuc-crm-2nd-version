package mycrm.upload;

public class DiallerDataset {
    private String dataset;
    private String qid;
    private String d_status;
    private String d_priority;
    private String notes;
    private String callbacks;
    private String crm_account;
    private String crm_campaign;
    private String description;
    private String locked;
    private String loaddate;
    private String sourcefile;
    private String import_table;
    private String campaign;

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getD_status() {
        return d_status;
    }

    public void setD_status(String d_status) {
        this.d_status = d_status;
    }

    public String getD_priority() {
        return d_priority;
    }

    public void setD_priority(String d_priority) {
        this.d_priority = d_priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(String callbacks) {
        this.callbacks = callbacks;
    }

    public String getCrm_account() {
        return crm_account;
    }

    public void setCrm_account(String crm_account) {
        this.crm_account = crm_account;
    }

    public String getCrm_campaign() {
        return crm_campaign;
    }

    public void setCrm_campaign(String crm_campaign) {
        this.crm_campaign = crm_campaign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getLoaddate() {
        return loaddate;
    }

    public void setLoaddate(String loaddate) {
        this.loaddate = loaddate;
    }

    public String getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(String sourcefile) {
        this.sourcefile = sourcefile;
    }

    public String getImport_table() {
        return import_table;
    }

    public void setImport_table(String import_table) {
        this.import_table = import_table;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    @Override
    public String toString() {
        return "DiallerDataset{" +
                "dataset='" + dataset + '\'' +
                ", qid='" + qid + '\'' +
                ", d_status='" + d_status + '\'' +
                ", d_priority='" + d_priority + '\'' +
                ", notes='" + notes + '\'' +
                ", callbacks='" + callbacks + '\'' +
                ", crm_account='" + crm_account + '\'' +
                ", crm_campaign='" + crm_campaign + '\'' +
                ", description='" + description + '\'' +
                ", locked='" + locked + '\'' +
                ", loaddate='" + loaddate + '\'' +
                ", sourcefile='" + sourcefile + '\'' +
                ", import_table='" + import_table + '\'' +
                ", campaign='" + campaign + '\'' +
                '}';
    }
}
