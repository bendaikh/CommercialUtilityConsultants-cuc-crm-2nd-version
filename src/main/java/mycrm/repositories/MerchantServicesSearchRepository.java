package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.MerchantServicesContract;
import mycrm.models.MerchantServicesContractSearch;
import mycrm.models.MerchantServicesSearchResult;
import mycrm.models.User;
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
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
@Transactional
public class MerchantServicesSearchRepository {
    private static final Logger logger = LogManager.getLogger();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private final SearchHelper searchHelper;
    @Value("${contracts.results.page.size}")
    private int pageSize;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public MerchantServicesSearchRepository(SearchHelper searchHelper) {
        this.searchHelper = searchHelper;
    }

    public MerchantServicesSearchResult search(MerchantServicesContractSearch merchantServicesContractSearch, int page) {
        logger.info("Starting merchant services search");

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilderResponse queryBuilderResponse = buildQuery(merchantServicesContractSearch, fullTextEntityManager);

        org.apache.lucene.search.Query searchQuery = queryBuilderResponse
                .getBooleanQuery()
                .build();

        javax.persistence.Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(searchQuery, MerchantServicesContract.class)
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
        List<MerchantServicesContract> results = jpaQuery.getResultList();

        List<MerchantServicesContract> filteredDoNotContactResults = results.stream()
                .filter(contract -> !contract.getCustomerSite().getCustomer().doNotContact)
                .collect(Collectors.toList());

        Query totalContracts = entityManager.createQuery("Select count(m.id) from MerchantServicesContract m");

        return MerchantServicesSearchResult
                .builder()
                .returnedContracts(filteredDoNotContactResults)
                .returnedContractCount(searchedCustomerCount)
                .totalContractCount((long) totalContracts.getSingleResult())
                .totalPages(searchHelper.getTotalPages(pageSize, searchedCustomerCount))
                .build();
    }

    private QueryBuilderResponse buildQuery(MerchantServicesContractSearch merchantServicesContractSearch, FullTextEntityManager fullTextEntityManager) {
        logger.info("Merchant Services Contracts - MerchantServicesContractSearch {}",
                merchantServicesContractSearch.toString());

        QueryBuilderResponse queryBuilderResponse = new QueryBuilderResponse();

        User user = merchantServicesContractSearch.getLoggedInUser();
        Broker brokerUser = user.getBroker();
        Broker pendingBroker = merchantServicesContractSearch.getPendingBroker();

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(MerchantServicesContract.class)
                .get();

        boolean validSearch = false;

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        if (StringUtils.hasText(merchantServicesContractSearch.getQ())) {

            logger.info("Searching for Q: {}", merchantServicesContractSearch.getQ());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("customerSite.customer.customerReference", "customerSite.customer.businessName", "customerSite.siteName",
                                    "customerSite.siteAddr", "customerSite.siteCity", "customerSite.customer.firstName",
                                    "customerSite.customer.lastName", "customerSite.registrationNo",
                                    "customerSite.sitePostcodeOut", "customerSite.sitePostcodeIn", "merchantId", "currentSupplier")
                            .matching(merchantServicesContractSearch.getQ())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (merchantServicesContractSearch.getBroker() != null) {

            logger.info("Searching filter by Broker: {}", merchantServicesContractSearch.getBroker().getId());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("broker.brokerReference")
                            .matching(merchantServicesContractSearch.getBroker().getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (StringUtils.hasText(merchantServicesContractSearch.getLogType())) {

            logger.info("Searching filter by Log type: {}", merchantServicesContractSearch.getLogType());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("logType")
                            .matching(merchantServicesContractSearch.getLogType())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (merchantServicesContractSearch.getFromDate() != null && merchantServicesContractSearch.getFromDate().length() > 0) {

            logger.info("Searching filter by from date: {}", merchantServicesContractSearch.getFromDate());

            validSearch = true;

            logger.info("From date search: {}", merchantServicesContractSearch.getFromDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("startDate")
                                .ignoreFieldBridge()
                                .above(formatter.parse(merchantServicesContractSearch.getFromDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (merchantServicesContractSearch.getToDate() != null && merchantServicesContractSearch.getToDate().length() > 0) {

            logger.info("Searching filter by to date: {}", merchantServicesContractSearch.getToDate());

            validSearch = true;

            logger.info("To date search: {}", merchantServicesContractSearch.getToDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(merchantServicesContractSearch.getToDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (merchantServicesContractSearch.isShowInternalBrokerOwnContractsOnly() && user.isBroker()) {
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

        if (merchantServicesContractSearch.isShowExternalBrokerOwnContractsOnly() && user.isExternalBroker()) {
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
        if (merchantServicesContractSearch.isCustomerContractSearch()) {

            if (!validSearch) {
                logger.info("Conducting Customer Contract specific search - nothing yet searched for");
                //show all results
                booleanQuery.add(queryBuilder.all().createQuery(), BooleanClause.Occur.MUST);
            }
        }

        if (merchantServicesContractSearch.isShowInternalBrokerOwnAndPendingContracts() && user.isBroker()) {
            logger.info("Filtering contracts to only include pending and broker specific contracts");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (merchantServicesContractSearch.isShowExternalBrokerOwnAndPendingContracts() && user.isExternalBroker()) {
            logger.info("Filtering contracts to only include external broker specific contracts and pending");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (merchantServicesContractSearch.isRenewalSearch()) {
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

        if (merchantServicesContractSearch.isLostRenewalSearch()) {
            logger.info("Filtering lost merchant renewals");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("doNotRenew")
                            .matching(true)
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (merchantServicesContractSearch.getMonthsRemaining() > 0) {
            logger.info("Filtering contracts by {} months remaining to date {}",
                    merchantServicesContractSearch.getMonthsRemaining(),
                    merchantServicesContractSearch.getMonthRemainingDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(merchantServicesContractSearch.getMonthRemainingDate()))
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
        queryBuilderResponse.setSort(buildSort(merchantServicesContractSearch, queryBuilder));

        // return an empty query if external broker is denied access
        if (merchantServicesContractSearch.isDenyExternalBrokerAccess() && user.isExternalBroker()) {
            logger.info("External broker access denied - returning empty query");
            //return an empty query
            //return new BooleanQuery.Builder();
            queryBuilderResponse.setBooleanQuery(new BooleanQuery.Builder());
        }

        return queryBuilderResponse;

    }

    private Sort buildSort(MerchantServicesContractSearch merchantServicesContractSearch, QueryBuilder queryBuilder) {
        Sort sort = queryBuilder
                .sort()
                .byField("dateCreated").desc()
                .onMissingValue().sortLast()
                .createSort();

        if (StringUtils.hasText(merchantServicesContractSearch.getQ())) {
            sort = Sort.RELEVANCE;
        }

        return sort;
    }
}
