package mycrm.repositories;


import mycrm.models.*;
import mycrm.search.SearchHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
@Transactional
public class WaterContractSearchRepository {
    @Value("${contracts.results.page.size}")
    private int pageSize;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Sort buildSort(WaterContractSearch waterContractSearch, QueryBuilder queryBuilder) {
        Sort sort = queryBuilder
                .sort()
                .byField("dateCreated").desc()
                .onMissingValue().sortLast()
                .createSort();
        if (StringUtils.hasText(waterContractSearch.getQ())) {
            sort = Sort.RELEVANCE;
        }
        return sort;
    }
    @PersistenceContext
    private EntityManager entityManager;
    private final SearchHelper searchHelper;
    @Autowired
    public WaterContractSearchRepository(SearchHelper searchHelper) {
        this.searchHelper = searchHelper;
    }

    private static final Logger logger = LogManager.getLogger();

    public WaterSearchResult search(WaterContractSearch waterContractSearch, int page) {

        logger.info("Starting broadband search");

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilderResponse queryBuilderResponse = buildQuery(waterContractSearch, fullTextEntityManager);
        org.apache.lucene.search.Query searchQuery = queryBuilderResponse
                .getBooleanQuery()
                .build();

        javax.persistence.Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(searchQuery, WaterContract.class)
                .setSort(queryBuilderResponse.getSort());
        // get count before paginating
        long searchedCustomerCount = jpaQuery.getResultList().size();

//        //ignore pagination if -1
        if (page != -1 && page > 0) {
            int pageNumber = page;

            jpaQuery.setFirstResult((pageNumber - 1) * pageSize);
            jpaQuery.setMaxResults(pageSize);
        }
        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<WaterContract> results = jpaQuery.getResultList();
        List<WaterContract> filteredDoNotContactResults = results.stream()
                .filter(contract -> !contract.getCustomerSite().getCustomer().doNotContact)
                .collect(Collectors.toList());
        Query totalContracts = entityManager.createQuery("Select count(m.id) from WaterContract m");
        return WaterSearchResult
                .builder()
                .returnedContracts(filteredDoNotContactResults)
                .returnedContractCount(searchedCustomerCount)
                .totalContractCount((long) totalContracts.getSingleResult())
                .totalPages(searchHelper.getTotalPages(pageSize, searchedCustomerCount))
                .build();
    }

    private QueryBuilderResponse buildQuery(WaterContractSearch waterContractSearch, FullTextEntityManager fullTextEntityManager) {
        logger.info("Water Contracts - WaterContractSearch {}",
                waterContractSearch.toString());

        QueryBuilderResponse queryBuilderResponse = new QueryBuilderResponse();
        User user = waterContractSearch.getLoggedInUser();
        Broker brokerUser = user.getBroker();
        Broker pendingBroker = waterContractSearch.getPendingBroker();
        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(WaterContract.class)
                .get();
        boolean validSearch = false;

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        if (StringUtils.hasText(waterContractSearch.getQ())) {
            logger.info("Searching for Q: {}", waterContractSearch.getQ());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("customerSite.customer.customerReference", "customerSite.customer.businessName", "customerSite.siteName",
                                    "customerSite.siteAddr", "customerSite.siteCity", "customerSite.customer.firstName",
                                    "customerSite.customer.lastName", "customerSite.registrationNo",
                                    "customerSite.sitePostcodeOut", "customerSite.sitePostcodeIn", "currentSupplier")
                            .matching(waterContractSearch.getQ())
                            .createQuery(),
                    BooleanClause.Occur.MUST);

        }

        if (waterContractSearch.getBroker() != null) {

            logger.info("Searching filter by Broker: {}", waterContractSearch.getBroker().getId());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("broker.brokerReference")
                            .matching(waterContractSearch.getBroker().getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (StringUtils.hasText(waterContractSearch.getLogType())) {

            logger.info("Searching filter by Log type: {}", waterContractSearch.getLogType());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("logType")
                            .matching(waterContractSearch.getLogType())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (waterContractSearch.getFromDate() != null && waterContractSearch.getFromDate().length() > 0) {

            logger.info("Searching filter by from date: {}", waterContractSearch.getFromDate());

            validSearch = true;

            logger.info("From date search: {}", waterContractSearch.getFromDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("startDate")
                                .ignoreFieldBridge()
                                .above(formatter.parse(waterContractSearch.getFromDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (waterContractSearch.getToDate() != null && waterContractSearch.getToDate().length() > 0) {

            logger.info("Searching filter by to date: {}", waterContractSearch.getToDate());

            validSearch = true;

            logger.info("To date search: {}", waterContractSearch.getToDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(waterContractSearch.getToDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (waterContractSearch.isShowInternalBrokerOwnContractsOnly() && user.isBroker()) {

            logger.info("User {} is internal broker {}",
                    user.getUsername(),
                    user.isBroker());

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (waterContractSearch.isShowExternalBrokerOwnContractsOnly() && user.isExternalBroker()) {

            logger.info("User {} is external broker {}",
                    user.getUsername(),
                    user.isExternalBroker());

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        //if contract search and no search is conducted then show all
        //remember external contracts should be excluded from there
        if (waterContractSearch.isCustomerContractSearch()) {

            if (!validSearch) {
                logger.info("Conducting Customer Contract specific search - nothing yet searched for");
                //show all results
                booleanQuery.add(queryBuilder.all().createQuery(), BooleanClause.Occur.MUST);
            }
        }

        if (waterContractSearch.isShowInternalBrokerOwnAndPendingContracts() && user.isBroker()) {

            logger.info("Filtering contracts to only include pending and broker specific contracts");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (waterContractSearch.isShowExternalBrokerOwnAndPendingContracts() && user.isExternalBroker()) {

            logger.info("Filtering contracts to only include external broker specific contracts and pending");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (waterContractSearch.isRenewalSearch()) {
            System.out.println("++++++++++++++++++++++++++ it works here 15");

//            logger.info("Filtering renewals where Contract Renewed is false");

//            not in the merchant services contract
//            booleanQuery.add(queryBuilder
//                            .keyword()
//                            .onField("contractRenewed")
//                            .matching(false)
//                            .createQuery(),
//                    BooleanClause.Occur.MUST);

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("doNotRenew")
                            .matching(false)
                            .createQuery(),
                    BooleanClause.Occur.MUST);

        }

        if (waterContractSearch.isLostRenewalSearch()) {
            logger.info("Filtering lost utility renewals");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("doNotRenew")
                            .matching(true)
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (waterContractSearch.getMonthsRemaining() > 0) {

            logger.info("Filtering contracts by {} months remaining to date {}",
                    waterContractSearch.getMonthsRemaining(),
                    waterContractSearch.getMonthRemainingDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(waterContractSearch.getMonthRemainingDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }

        booleanQuery.add(queryBuilder
                        .keyword()
                        .onFields("customerSite.customer.doNotContact")
                        .matching(false).createQuery(),
                BooleanClause.Occur.MUST);

        logger.info("Do not contact removed where true");

        queryBuilderResponse.setBooleanQuery(booleanQuery);
        queryBuilderResponse.setSort(buildSort(waterContractSearch, queryBuilder));
        // return an empty query if external broker is denied access
        if (waterContractSearch.isDenyExternalBrokerAccess() && user.isExternalBroker()) {

            logger.info("External broker access denied - returning empty query");
            //return an empty query
            //return new BooleanQuery.Builder();
            queryBuilderResponse.setBooleanQuery(new BooleanQuery.Builder());
        }

        return queryBuilderResponse;

    }
}
