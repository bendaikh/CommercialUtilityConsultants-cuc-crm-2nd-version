package mycrm.search;

import mycrm.repositories.DiallerResponseRepository;
import mycrm.upload.DiallerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DiallerResponseServiceImpl implements DiallerResponseService {
    private final DiallerResponseRepository diallerResponseRepository;

    @Autowired
    public DiallerResponseServiceImpl(DiallerResponseRepository diallerResponseRepository) {
        this.diallerResponseRepository = diallerResponseRepository;
    }

    @Override
    public void saveDiallerResponse(DiallerResponse diallerResponse) {
        this.diallerResponseRepository.save(diallerResponse);
    }
}
