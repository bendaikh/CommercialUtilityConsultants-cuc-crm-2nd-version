package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.BrokerNote;
import mycrm.models.User;
import mycrm.repositories.BrokerNoteRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class BrokerNoteServiceJpaImpl implements BrokerNoteService {

	private final BrokerNoteRepository brokerNotesRepo;
	private final UserService userService;

	@Autowired
	public BrokerNoteServiceJpaImpl(BrokerNoteRepository brokerNotesRepo, UserService userService) {
		this.brokerNotesRepo = brokerNotesRepo;
		this.userService = userService;
	}

	@Override
	public List<BrokerNote> findAll() {
		return this.brokerNotesRepo.findAll();
	}

	@Override
	public BrokerNote findById(Long id) {
		return this.brokerNotesRepo.findOne(id);
	}

	@Override
	public BrokerNote create(BrokerNote brokerNote) {
		return this.brokerNotesRepo.save(brokerNote);
	}

	@Override
	public BrokerNote edit(BrokerNote brokerNote) {
		return this.brokerNotesRepo.save(brokerNote);
	}

	@Override
	public void deleteById(Long id) {
		this.brokerNotesRepo.delete(id);
	}

	@Override
	public List<BrokerNote> findByBroker(Broker broker) {
		return this.brokerNotesRepo.findByBroker(broker);
	}

	@Override
	public List<BrokerNote> findAllIncompleteBrokerNotesByTaggedUser(User user) {
		return this.brokerNotesRepo.findAllIncompleteByTaggedUser(user);
	}

	@Override
	public void updateBrokerNote(long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User completedBy = userService.findUserByUsername(auth.getName());

		DateTime dateTime = new org.joda.time.DateTime();

		this.brokerNotesRepo.updateBrokerNote(id, dateTime.toDate(), completedBy);
	}

}
