package mycrm.models;

import java.util.List;

public class CourtesyCallSearch {

    private final int totalPages;
    private final int pageNumber;
    private final List<CourtesyCallTask> courtesyCallTaskList;

    public CourtesyCallSearch(int totalPages, int pageNumber, List<CourtesyCallTask> courtesyCallTaskList) {
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.courtesyCallTaskList = courtesyCallTaskList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public List<CourtesyCallTask> getCourtesyCallTaskList() {
        return courtesyCallTaskList;
    }
}