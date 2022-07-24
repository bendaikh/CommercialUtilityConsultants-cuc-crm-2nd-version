package mycrm.rest.clients;

import mycrm.models.TpsCheck;

public interface TpsCheckRestClient {
    TpsCheck callTpsApiService(String number);
}
