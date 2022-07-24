package mycrm.models;

public class CustomerSearch {

    private String q;
    private boolean showDoNotContact;
    private int pageNumber;

    public CustomerSearch() {
        super();
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        if (q != null) {
            this.q = q.toLowerCase();
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isShowDoNotContact() {
        return showDoNotContact;
    }

    public void setShowDoNotContact(boolean showDoNotContact) {
        this.showDoNotContact = showDoNotContact;
    }

    @Override
    public String toString() {
        return "CustomerSearch{" +
                "q='" + q + '\'' +
                ", showDoNotContact=" + showDoNotContact +
                ", pageNumber=" + pageNumber +
                '}';
    }
}
