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
public class VoipContractSearchRepository {
    @Value("${contracts.results.page.size}")
    private int pageSize;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Sort buildSort(VoipContractSearch voipContractSearch, QueryBuilder queryBuilder) {
        Sort sort = queryBuilder
                .sort()
                .byField("dateCreated").desc()
                .onMissingValue().sortLast()
                .createSort();
        if (StringUtils.hasText(voipContractSearch.getQ())) {
            sort = Sort.RELEVANCE;
        }
        return sort;
    }
    @PersistenceContext
    private EntityManager entityManager;
    private final SearchHelper searchHelper;
    @Autowired
    public VoipContractSearchRepository(SearchHelper searchHelper) {
        this.searchHelper = searchHelper;
    }

    private static final Logger logger = LogManager.getLogger();

    public VoipSearchResult search(VoipContractSearch voipContractSearch, int page) {

        logger.info("Starting broadband search");

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilderResponse queryBuilderResponse = buildQuery(voipContractSearch, fullTextEntityManager);
        org.apache.lucene.search.Query searchQuery = queryBuilderResponse
                .getBooleanQuery()
                .build();

        javax.persistence.Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(searchQuery, VoipContract.class)
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
        List<VoipContract> results = jpaQuery.getResultList();
        List<VoipContract> filteredDoNotContactResults = results.stream()
                .filter(contract -> !contract.getCustomerSite().getCustomer().doNotContact)
                .collect(Collectors.toList());
        Query totalContracts = entityManager.createQuery("Select count(m.id) from VoipContract m");
        return VoipSearchResult
                .builder()
                .returnedContracts(filteredDoNotContactResults)
                .returnedContractCount(searchedCustomerCount)
                .totalContractCount((long) totalContracts.getSingleResult())
                .totalPages(searchHelper.getTotalPages(pageSize, searchedCustomerCount))
                .build();
    }

    private QueryBuilderResponse buildQuery(VoipContractSearch voipContractSearch, FullTextEntityManager fullTextEntityManager) {
        logger.info("Voip Contracts - VoipContractSearch {}",
                voipContractSearch.toString());

        QueryBuilderResponse queryBuilderResponse = new QueryBuilderResponse();
        User user = voipContractSearch.getLoggedInUser();
        Broker brokerUser = user.getBroker();
        Broker pendingBroker = voipContractSearch.getPendingBroker();
        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(VoipContract.class)
                .get();
        boolean validSearch = false;

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        if (StringUtils.hasText(voipContractSearch.getQ())) {
            logger.info("Searching for Q: {}", voipContractSearch.getQ());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("customerSite.customer.customerReference", "customerSite.customer.businessName", "customerSite.siteName",
                                    "customerSite.siteAddr", "customerSite.siteCity", "customerSite.customer.firstName",
                                    "customerSite.customer.lastName", "customerSite.registrationNo",
                                    "customerSite.sitePostcodeOut", "customerSite.sitePostcodeIn", "currentSupplier")
                            .matching(voipContractSearch.getQ())
                            .createQuery(),
                    BooleanClause.Occur.MUST);

        }

        if (voipContractSearch.getBroker() != null) {

            logger.info("Searching filter by Broker: {}", voipContractSearch.getBroker().getId());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("broker.brokerReference")
                            .matching(voipContractSearch.getBroker().getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (StringUtils.hasText(voipContractSearch.getLogType())) {

            logger.info("Searching filter by Log type: {}", voipContractSearch.getLogType());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("logType")
                            .matching(voipContractSearch.getLogType())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (voipContractSearch.getFromDate() != null && voipContractSearch.getFromDate().length() > 0) {

            logger.info("Searching filter by from date: {}", voipContractSearch.getFromDate());

            validSearch = true;

            logger.info("From date search: {}", voipContractSearch.getFromDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("startDate")
                                .ignoreFieldBridge()
                                .above(formatter.parse(voipContractSearch.getFromDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (voipContractSearch.getToDate() != null && voipContractSearch.getToDate().length() > 0) {

            logger.info("Searching filter by to date: {}", voipContractSearch.getToDate());

            validSearch = true;

            logger.info("To date search: {}", voipContractSearch.getToDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(voipContractSearch.getToDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (voipContractSearch.isShowInternalBrokerOwnContractsOnly() && user.isBroker()) {

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

        if (voipContractSearch.isShowExternalBrokerOwnContractsOnly() && user.isExternalBroker()) {

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
        if (voipContractSearch.isCustomerContractSearch()) {

            if (!validSearch) {
                logger.info("Conducting Customer Contract specific search - nothing yet searched for");
                //show all results
                booleanQuery.add(queryBuilder.all().createQuery(), BooleanClause.Occur.MUST);
            }
        }

        if (voipContractSearch.isShowInternalBrokerOwnAndPendingContracts() && user.isBroker()) {

            logger.info("Filtering contracts to only include pending and broker specific contracts");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (voipContractSearch.isShowExternalBrokerOwnAndPendingContracts() && user.isExternalBroker()) {

            logger.info("Filtering contracts to only include external broker specific contracts and pending");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (voipContractSearch.isRenewalSearch()) {
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

        //            not in the merchant services contract
        if (voipContractSearch.isLostRenewalSearch()) {
            logger.info("Filtering lost voip renewals");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("doNotRenew")
                            .matching(true)
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (voipContractSearch.getMonthsRemaining() > 0) {

            logger.info("Filtering contracts by {} months remaining to date {}",
                    voipContractSearch.getMonthsRemaining(),
                    voipContractSearch.getMonthRemainingDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(voipContractSearch.getMonthRemainingDate()))
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
        queryBuilderResponse.setSort(buildSort(voipContractSearch, queryBuilder));
        // return an empty query if external broker is denied access
        if (voipContractSearch.isDenyExternalBrokerAccess() && user.isExternalBroker()) {

            logger.info("External broker access denied - returning empty query");
            //return an empty query
            //return new BooleanQuery.Builder();
            queryBuilderResponse.setBooleanQuery(new BooleanQuery.Builder());
        }

        return queryBuilderResponse;

    }

}
