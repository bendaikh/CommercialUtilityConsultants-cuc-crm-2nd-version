package mycrm.repositories;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;

public class QueryBuilderResponse {

    private BooleanQuery.Builder booleanQuery;
    private Sort sort;

    public BooleanQuery.Builder getBooleanQuery() {
        return booleanQuery;
    }

    public void setBooleanQuery(BooleanQuery.Builder booleanQuery) {
        this.booleanQuery = booleanQuery;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "QueryBuilderResponse{" +
                "booleanQuery=" + booleanQuery +
                ", sort=" + sort +
                '}';
    }
}
