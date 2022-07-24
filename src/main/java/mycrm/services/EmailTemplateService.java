package mycrm.services;

import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.DiallerAPIResponse;
import mycrm.models.EnergyContract;
import mycrm.upload.DiallerDatasetType;

public interface EmailTemplateService {
    void sendTaggedCustomerNoteNotification(CustomerNote customerNote) throws Exception;

    void sendTaggedCustomerChildNoteNotification(CustomerChildNote customerChildNote) throws Exception;

    boolean sendRenewalEmail(String customerReference, String emailAddress, String accountNumber);

    void sendMissingOutEmail() throws Exception;

    void sendWelcomeEmail(EnergyContract energyContract) throws Exception;

    boolean sendObjectedEmail(String customerReference, String emailAddress, String accountNumber);

    boolean sendGoLiveEmail(String customerReference, String emailAddress, String accountNumber);

    void sendDiallerFailedEmailAlert(DiallerAPIResponse diallerAPIResponse, DiallerDatasetType type) throws Exception;

    void sendDiallerUploadNotNeeded(String message) throws Exception;

    void sendDiallerSuccessEmailAlert(DiallerAPIResponse response, DiallerDatasetType type) throws Exception;

    boolean sendSoldEmail(String customerReference, String emailAddress, String accountNumber);
}
