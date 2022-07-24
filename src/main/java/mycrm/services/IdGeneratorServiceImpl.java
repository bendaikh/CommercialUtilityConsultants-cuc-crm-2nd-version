package mycrm.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class IdGeneratorServiceImpl implements IdGeneratorService {

    @Override
    public String generateId() {
        return "CEC" + System.nanoTime() + "A";
    }

}
