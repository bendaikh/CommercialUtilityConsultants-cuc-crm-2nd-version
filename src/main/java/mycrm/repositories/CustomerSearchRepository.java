package mycrm.repositories;

import mycrm.models.Customer;
import mycrm.models.CustomerSearch;
import mycrm.models.CustomerSearchResult;
import mycrm.search.SearchHelper;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CustomerSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final SearchHelper searchHelper;

    @Autowired
    public CustomerSearchRepository(SearchHelper searchHelper) {
        this.searchHelper = searchHelper;
    }

    public CustomerSearchResult search(CustomerSearch customerSearch, int page) {

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        org.apache.lucene.search.Query searchQuery = buildQuery(customerSearch, fullTextEntityManager)
                .getBooleanQuery()
                .build();

        // wrap Lucene query in an Hibernate Query object
        javax.persistence.Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(searchQuery, Customer.class)
                .setSort(buildQuery(customerSearch, fullTextEntityManager)
                        .getSort());

        int pageNumber = page;
        int pageSize = 40;

        long searchedCustomerCount = jpaQuery.getResultList().size();

        jpaQuery.setFirstResult((pageNumber - 1) * pageSize);
        jpaQuery.setMaxResults(pageSize);

        @SuppressWarnings("unchecked")
        List<Customer> results = jpaQuery.getResultList();

        Query totalCustomers = entityManager.createQuery("Select count(c.id) from Customer c");

        //results.sort(Comparator.comparing(Customer::getDateCreated).reversed());

        CustomerSearchResult searchResult = new CustomerSearchResult();
        searchResult.setReturnedCustomers(results);
        searchResult.setReturnedCustomerCount(searchedCustomerCount);
        searchResult.setTotalCustomers((long) totalCustomers.getSingleResult());
        searchResult.setTotalPages(searchHelper.getTotalPages(pageSize, searchedCustomerCount));

        return searchResult;
    }

    private QueryBuilderResponse buildQuery(CustomerSearch customerSearch, FullTextEntityManager fullTextEntityManager) {
        QueryBuilderResponse queryBuilderResponse = new QueryBuilderResponse();

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Customer.class)
                .get();

        Sort sort;

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        if (StringUtils.hasText(customerSearch.getQ())) {


            booleanQuery.add(queryBuilder.keyword()
                    //.wildcard()
                    .onFields("customerReference", "businessName", "businessAddr", "businessCity", "firstName",
                            "lastName", "stAddr", "stCity")
                    .matching(customerSearch.getQ())
                    .createQuery(), BooleanClause.Occur.SHOULD);

            booleanQuery.add(queryBuilder.keyword().onFields("businessPostcodeOut", "businessPostcodeIn", "stPostcodeOut", "stPostcodeIn")
                    .matching(customerSearch.getQ()).createQuery(), BooleanClause.Occur.SHOULD);

            sort = Sort.RELEVANCE;
        } else {

            booleanQuery.add(queryBuilder.all().createQuery(), BooleanClause.Occur.MUST);

            sort = queryBuilder
                    .sort()
                    .byField("dateCreated").desc()
                    .onMissingValue().sortLast()
                    .createSort();
        }

        booleanQuery.add(queryBuilder
                        .keyword()
                        .onFields("doNotContact")
                        .matching(customerSearch.isShowDoNotContact()).createQuery(),
                BooleanClause.Occur.MUST);


        queryBuilderResponse.setBooleanQuery(booleanQuery);
        queryBuilderResponse.setSort(sort);
        return queryBuilderResponse;
    }
}
