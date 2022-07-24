package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerNote;
import mycrm.models.User;

import java.util.List;

public interface BrokerNoteService {

    List<BrokerNote> findAll();

    BrokerNote findById(Long id);

    List<BrokerNote> findByBroker(Broker broker);

    BrokerNote create(BrokerNote brokerNote);

    BrokerNote edit(BrokerNote brokerNote);

    void deleteById(Long id);

    List<BrokerNote> findAllIncompleteBrokerNotesByTaggedUser(User user);
	void updateBrokerNote(long id);
    
}
