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
public class SolarContractSearchRepository {
    private Sort buildSort(SolarContractSearch solarContractSearch, QueryBuilder queryBuilder) {
        Sort sort = queryBuilder
                .sort()
                .byField("dateCreated").desc()
                .onMissingValue().sortLast()
                .createSort();
        if (StringUtils.hasText(solarContractSearch.getQ())) {
            sort = Sort.RELEVANCE;
        }
        return sort;
    }


    @Value("${contracts.results.page.size}")
    private int pageSize;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @PersistenceContext
    private EntityManager entityManager;
    private final SearchHelper searchHelper;
    @Autowired
    public SolarContractSearchRepository(SearchHelper searchHelper) {
        this.searchHelper = searchHelper;
    }

    private static final Logger logger = LogManager.getLogger();

    public SolarSearchResult search(SolarContractSearch solarContractSearch, int page) {

        logger.info("Starting solar search");

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilderResponse queryBuilderResponse = buildQuery(solarContractSearch, fullTextEntityManager);
        org.apache.lucene.search.Query searchQuery = queryBuilderResponse
                .getBooleanQuery()
                .build();

        javax.persistence.Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(searchQuery, SolarContract.class)
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
        List<SolarContract> results = jpaQuery.getResultList();
        List<SolarContract> filteredDoNotContactResults = results.stream()
                .filter(contract -> !contract.getCustomerSite().getCustomer().doNotContact)
                .collect(Collectors.toList());
        Query totalContracts = entityManager.createQuery("Select count(m.id) from SolarContract m");
        return SolarSearchResult
                .builder()
                .returnedContracts(filteredDoNotContactResults)
                .returnedContractCount(searchedCustomerCount)
                .totalContractCount((long) totalContracts.getSingleResult())
                .totalPages(searchHelper.getTotalPages(pageSize, searchedCustomerCount))
                .build();
    }

    private QueryBuilderResponse buildQuery(SolarContractSearch solarContractSearch, FullTextEntityManager fullTextEntityManager) {
        logger.info("Solar Contracts - SolarContractSearch {}",
                solarContractSearch.toString());

        QueryBuilderResponse queryBuilderResponse = new QueryBuilderResponse();
        User user = solarContractSearch.getLoggedInUser();
        Broker brokerUser = user.getBroker();
        Broker pendingBroker = solarContractSearch.getPendingBroker();
        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(SolarContract.class)
                .get();
        boolean validSearch = false;

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        if (StringUtils.hasText(solarContractSearch.getQ())) {
            logger.info("Searching for Q: {}", solarContractSearch.getQ());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("customerSite.customer.customerReference", "customerSite.customer.businessName", "customerSite.siteName",
                                    "customerSite.siteAddr", "customerSite.siteCity", "customerSite.customer.firstName",
                                    "customerSite.customer.lastName", "customerSite.registrationNo",
                                    "customerSite.sitePostcodeOut", "customerSite.sitePostcodeIn", "currentSupplier")
                            .matching(solarContractSearch.getQ())
                            .createQuery(),
                    BooleanClause.Occur.MUST);

        }

        if (solarContractSearch.getBroker() != null) {

            logger.info("Searching filter by Broker: {}", solarContractSearch.getBroker().getId());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("broker.brokerReference")
                            .matching(solarContractSearch.getBroker().getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (StringUtils.hasText(solarContractSearch.getLogType())) {

            logger.info("Searching filter by Log type: {}", solarContractSearch.getLogType());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("logType")
                            .matching(solarContractSearch.getLogType())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (solarContractSearch.getFromDate() != null && solarContractSearch.getFromDate().length() > 0) {

            logger.info("Searching filter by from date: {}", solarContractSearch.getFromDate());

            validSearch = true;

            logger.info("From date search: {}", solarContractSearch.getFromDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("startDate")
                                .ignoreFieldBridge()
                                .above(formatter.parse(solarContractSearch.getFromDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (solarContractSearch.getToDate() != null && solarContractSearch.getToDate().length() > 0) {

            logger.info("Searching filter by to date: {}", solarContractSearch.getToDate());

            validSearch = true;

            logger.info("To date search: {}", solarContractSearch.getToDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(solarContractSearch.getToDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (solarContractSearch.isShowInternalBrokerOwnContractsOnly() && user.isBroker()) {

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

        if (solarContractSearch.isShowExternalBrokerOwnContractsOnly() && user.isExternalBroker()) {

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
        if (solarContractSearch.isCustomerContractSearch()) {

            if (!validSearch) {
                logger.info("Conducting Customer Contract specific search - nothing yet searched for");
                //show all results
                booleanQuery.add(queryBuilder.all().createQuery(), BooleanClause.Occur.MUST);
            }
        }

        if (solarContractSearch.isShowInternalBrokerOwnAndPendingContracts() && user.isBroker()) {

            logger.info("Filtering contracts to only include pending and broker specific contracts");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (solarContractSearch.isShowExternalBrokerOwnAndPendingContracts() && user.isExternalBroker()) {

            logger.info("Filtering contracts to only include external broker specific contracts and pending");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (solarContractSearch.isRenewalSearch()) {
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

        //            not in the merchant services contract
        if (solarContractSearch.isLostRenewalSearch()) {
            logger.info("Filtering lost broadband renewals");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("doNotRenew")
                            .matching(true)
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (solarContractSearch.getMonthsRemaining() > 0) {

            logger.info("Filtering contracts by {} months remaining to date {}",
                    solarContractSearch.getMonthsRemaining(),
                    solarContractSearch.getMonthRemainingDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(solarContractSearch.getMonthRemainingDate()))
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
        queryBuilderResponse.setSort(buildSort(solarContractSearch, queryBuilder));
        // return an empty query if external broker is denied access
        if (solarContractSearch.isDenyExternalBrokerAccess() && user.isExternalBroker()) {

            logger.info("External broker access denied - returning empty query");
            //return an empty query
            //return new BooleanQuery.Builder();
            queryBuilderResponse.setBooleanQuery(new BooleanQuery.Builder());
        }

        return queryBuilderResponse;

    }
}
