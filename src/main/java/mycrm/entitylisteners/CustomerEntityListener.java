package mycrm.entitylisteners;

import mycrm.audit.history.CustomerHistory;
import mycrm.models.Customer;
import mycrm.services.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

public class CustomerEntityListener {
	
	@PostPersist
	public void postPersist(Customer customer) {
		perform(customer, Action.INSERTED);
	}
	
	@PreUpdate
    public void preUpdate(Customer customer) {
        perform(customer, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Customer customer) {
        //perform(customer, Action.DELETED);
    }
	
	@Transactional(TxType.MANDATORY)
    public void perform(Customer customer, Action action) {
		EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
		entityManager.persist(new CustomerHistory(customer, action));
	}

}