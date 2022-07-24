package mycrm.services;

import mycrm.models.CourtesyCallSearch;

public interface CourtesyCallTaskService {
    CourtesyCallSearch findAll(int pageNumber);
}
