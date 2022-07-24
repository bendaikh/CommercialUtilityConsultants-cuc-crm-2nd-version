package mycrm.services;

import mycrm.functions.UserHelper;
import mycrm.models.Broker;
import mycrm.models.CourtesyCallSearch;
import mycrm.models.CourtesyCallTask;
import mycrm.models.User;
import mycrm.repositories.CourtesyCallTaskRepository;
import mycrm.search.SearchHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
@Primary
public class CourtesyCallTaskServiceImpl implements CourtesyCallTaskService {

    private static Logger logger = LogManager.getLogger();

    private final SearchHelper searchHelper;

    private final UserHelper userHelper;

    private final CourtesyCallTaskRepository courtesyCallTaskRepository;
    @Value("${courtesy.call.results.page.size}")
    private int courtesyCallPageSize;

    public CourtesyCallTaskServiceImpl(CourtesyCallTaskRepository courtesyCallTaskRepository,
                                       SearchHelper searchHelper,
                                       UserHelper userHelper) {
        this.courtesyCallTaskRepository = courtesyCallTaskRepository;
        this.searchHelper = searchHelper;
        this.userHelper = userHelper;
    }

    @Override
    public CourtesyCallSearch findAll(int pageNumber) {
        User user = userHelper.getLoggedInUser();

        Broker broker = user.isExternalBroker() && user.getBroker() != null ? user.getBroker() : null;

        logger.info("User is: {}, External Broker is {}", user.getUsername(), user.isExternalBroker());

        Iterable<CourtesyCallTask> allCourtesyCallTasks = broker != null ? this.courtesyCallTaskRepository.findByBroker(broker) :
                this.courtesyCallTaskRepository.findAll();

        int totalPages = searchHelper.getTotalPages(courtesyCallPageSize, StreamSupport.stream(allCourtesyCallTasks.spliterator(), false).count());
        int searchPageNumber = 1;
        if (pageNumber != -1 && pageNumber > 0) {
            searchPageNumber = pageNumber - 1;
        }

        PageRequest pageRequest = new PageRequest(searchPageNumber, courtesyCallPageSize);

        Page<CourtesyCallTask> courtesyCallTasks = broker != null ? this.courtesyCallTaskRepository.findByBroker(broker, pageRequest) :
                this.courtesyCallTaskRepository.findAll(pageRequest);

        return new CourtesyCallSearch(totalPages, pageNumber, courtesyCallTasks.getContent());
    }
}
