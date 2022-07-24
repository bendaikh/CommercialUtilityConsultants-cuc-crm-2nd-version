package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.Contract;
import mycrm.models.ContractSearch;
import mycrm.models.ContractSearchResult;
import mycrm.models.EnergyContract;
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

//https://docs.jboss.org/hibernate/search/4.5/reference/en-US/html/search-query.html#search-query


@Repository
@Transactional
public class EnergyContractSearchRepository {

    private static final Logger logger = LogManager.getLogger();

    @Value("${contracts.results.page.size}")
    private int pageSize;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @PersistenceContext
    private EntityManager entityManager;

    private final SearchHelper searchHelper;

    @Autowired
    public EnergyContractSearchRepository(SearchHelper searchHelper) {
        this.searchHelper = searchHelper;
    }

    public ContractSearchResult search(ContractSearch contractSearch, int page) {
        logger.info("Starting contract search");

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilderResponse queryBuilderResponse = buildQuery(contractSearch, fullTextEntityManager);

        org.apache.lucene.search.Query searchQuery = queryBuilderResponse
                .getBooleanQuery()
                .build();

        javax.persistence.Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(searchQuery, EnergyContract.class)
                .setSort(queryBuilderResponse.getSort());

        // get count before paginating
        long searchedCustomerCount = jpaQuery.getResultList().size();

        //ignore pagination if -1
        if (page != -1 && page > 0) {
            int pageNumber = page;

            jpaQuery.setFirstResult((pageNumber - 1) * pageSize);
            jpaQuery.setMaxResults(pageSize);
        }

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List<Contract> results = jpaQuery.getResultList();

        List<Contract> filteredDoNotContactResults = results.stream()
                .filter(contract -> !contract.getCustomer().doNotContact)
                .collect(Collectors.toList());

        Query gasTotalCustomers = entityManager.createQuery("Select count(c.id) from GasCustomerContract c");
        Query elecTotalCustomers = entityManager.createQuery("Select count(c.id) from ElecCustomerContract c");

        ContractSearchResult searchResult = new ContractSearchResult();
        searchResult.setReturnedContracts(filteredDoNotContactResults);
        searchResult.setReturnedContractCount(searchedCustomerCount);
        searchResult.setTotalContractCount(((long) gasTotalCustomers.getSingleResult() + (long) elecTotalCustomers.getSingleResult()));
        searchResult.setTotalPages(searchHelper.getTotalPages(pageSize, searchedCustomerCount));

        return searchResult;
    }

    private QueryBuilderResponse buildQuery(ContractSearch contractSearch, FullTextEntityManager fullTextEntityManager) {

        logger.info("Customer Contract - ContractSearch {}", contractSearch.toString());

        User user = contractSearch.getLoggedInUser();
        Broker brokerUser = user.getBroker();
        Broker pendingBroker = contractSearch.getPendingBroker();

        QueryBuilderResponse queryBuilderResponse = new QueryBuilderResponse();

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(EnergyContract.class)
                .get();

        boolean validSearch = false;

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        if (StringUtils.hasText(contractSearch.getQ())) {

            logger.info("Searching for Q: {}", contractSearch.getQ());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("customer.customerReference", "customer.businessName", "customerSite.siteName",
                                    "customerSite.siteAddr", "customerSite.siteCity", "customer.firstName",
                                    "customer.lastName", "customerSite.registrationNo",
                                    "customerSite.sitePostcodeOut", "customerSite.sitePostcodeIn", "accountNumber")
                            .matching(contractSearch.getQ())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.getBroker() != null) {

            logger.info("Searching filter by Broker: {}", contractSearch.getBroker().getId());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("broker.brokerReference")
                            .matching(contractSearch.getBroker().getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.getSupplier() != null) {

            logger.info("Searching filter by Supplier: {}", contractSearch.getSupplier().getId());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .phrase()
                            .withSlop(0)
                            .onField("supplier.businessName")
                            .sentence("\"" + contractSearch.getSupplier().getBusinessName() + "\"")
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.getCampaign() != null && StringUtils.hasText(contractSearch.getCampaign())) {
            logger.info("Searching filter by Campaign: {}", contractSearch.getCampaign());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .phrase()
                            .withSlop(0)
                            .onField("campaign")
                            .sentence("\"" + contractSearch.getCampaign() + "\"")
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }


        if (StringUtils.hasText(contractSearch.getLogType())) {

            logger.info("Searching filter by Log type: {}", contractSearch.getLogType());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("logType")
                            .matching(contractSearch.getLogType())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.getFromDate() != null && contractSearch.getFromDate().length() > 0) {

            logger.info("Searching filter by from date: {}", contractSearch.getFromDate());

            validSearch = true;

            logger.info("From date search: {}", contractSearch.getFromDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("startDate")
                                .ignoreFieldBridge()
                                .above(formatter.parse(contractSearch.getFromDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (contractSearch.getToDate() != null && contractSearch.getToDate().length() > 0) {

            logger.info("Searching filter by to date: {}", contractSearch.getToDate());

            validSearch = true;

            logger.info("To date search: {}", contractSearch.getToDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(contractSearch.getToDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //
            }
        }

        if (StringUtils.hasText(contractSearch.getSupplyType())) {

            logger.info("Searching filter by supply: {}", contractSearch.getSupplyType());

            validSearch = true;

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onFields("supplyType")
                            .matching(contractSearch.getSupplyType())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.isShowInternalBrokerOwnContractsOnly() && user.isBroker()) {
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

        if (contractSearch.isShowExternalBrokerOwnContractsOnly() && user.isExternalBroker()) {
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
        if (contractSearch.isCustomerContractSearch()) {

            if (!validSearch) {
                logger.info("Conducting Customer Contract specific search - nothing yet searched for");
                //show all results
                booleanQuery.add(queryBuilder.all().createQuery(), BooleanClause.Occur.MUST);
            }
        }

        if (contractSearch.isShowInternalBrokerOwnAndPendingContracts() && user.isBroker()) {
            logger.info("Showing Internal Broker Own and Pending Contracts");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.isShowExternalBrokerOwnAndPendingContracts() && user.isExternalBroker()) {
            logger.info("Showing External Broker Own and Pending Contracts");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("broker.brokerReference")
                            .matching(brokerUser.getBrokerReference() + " " + pendingBroker.getBrokerReference())
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.isRenewalSearch()) {
            logger.info("Filtering renewals where Contract Renewed is false");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("contractRenewed")
                            .matching(false)
                            .createQuery(),
                    BooleanClause.Occur.MUST);

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("doNotRenew")
                            .matching(false)
                            .createQuery(),
                    BooleanClause.Occur.MUST);

        }

        if (contractSearch.isLostRenewalSearch()) {
            logger.info("Filtering lost renewals where lost contract is true");

            booleanQuery.add(queryBuilder
                            .keyword()
                            .onField("lostRenewal")
                            .matching(true)
                            .createQuery(),
                    BooleanClause.Occur.MUST);
        }

        if (contractSearch.getMonthsRemaining() > 0) {
            logger.info("Filtering contracts by {} months remaining to date {}",
                    contractSearch.getMonthsRemaining(),
                    contractSearch.getMonthRemainingDate());

            try {
                booleanQuery.add(queryBuilder
                                .range()
                                .onField("endDate")
                                .ignoreFieldBridge()
                                .below(formatter.parse(contractSearch.getMonthRemainingDate()))
                                .excludeLimit().createQuery(),
                        BooleanClause.Occur.MUST);
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }

        booleanQuery.add(queryBuilder
                        .keyword()
                        .onFields("customer.doNotContact")
                        .matching(false).createQuery(),
                BooleanClause.Occur.MUST);

        logger.info("Do not contact removed where true");

        queryBuilderResponse.setBooleanQuery(booleanQuery);
        queryBuilderResponse.setSort(buildSort(contractSearch, queryBuilder));

        // return an empty query if external broker is denied access
        if (contractSearch.isDenyExternalBrokerAccess() && user.isExternalBroker()) {
            logger.info("External broker access denied - returning empty query");
            queryBuilderResponse.setBooleanQuery(new BooleanQuery.Builder());
        }

        return queryBuilderResponse;
    }

    private Sort buildSort(ContractSearch contractSearch, QueryBuilder queryBuilder) {
        Sort sort = queryBuilder
                .sort()
                .byField("dateCreated").desc()
                .onMissingValue().sortLast()
                .createSort();

        if (StringUtils.hasText(contractSearch.getQ())) {
            sort = Sort.RELEVANCE;
        }

        if (contractSearch.isRenewalSearch() || contractSearch.isLeadSearch()) {
            sort = queryBuilder
                    .sort()
                    .byField("endDate").asc()
                    .onMissingValue().sortLast()
                    .createSort();
        }

        if (contractSearch.isCallbackSearch()) {
            sort = queryBuilder
                    .sort()
                    .byField("callbackDateTime").asc()
                    .onMissingValue().sortLast()
                    .createSort();
        }

        return sort;
    }
}
