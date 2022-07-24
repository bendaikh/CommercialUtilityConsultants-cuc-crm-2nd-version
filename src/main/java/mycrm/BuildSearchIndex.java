package mycrm;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BuildSearchIndex implements ApplicationListener<ApplicationReadyEvent> {
	
	private static Logger logger = LogManager.getLogger();

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		try {
			
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.createIndexer().startAndWait();

		} catch (InterruptedException e) {
			logger.info("An error occurred trying to build the search index: " + e.toString());
		}
		return;

	}

}
