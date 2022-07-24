package mycrm.services;

import mycrm.models.LogTypeHistory;
import mycrm.repositories.LogTypeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class LogTypeHistoryServiceImpl implements LogTypeHistoryService {
    private final LogTypeHistoryRepository logTypeHistoryRepository;

    @Autowired
    public LogTypeHistoryServiceImpl(LogTypeHistoryRepository logTypeHistoryRepository) {
        this.logTypeHistoryRepository = logTypeHistoryRepository;
    }

    @Override
    public LogTypeHistory save(LogTypeHistory logTypeHistory) {
        return this.logTypeHistoryRepository.save(logTypeHistory);
    }
}
