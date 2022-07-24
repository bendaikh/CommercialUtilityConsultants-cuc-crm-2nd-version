package mycrm.repositories;

import mycrm.models.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PluginRepository extends JpaRepository<Plugin, Long> {
    Plugin findByApi(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Plugin p SET primaryDataset=(:dataset) where api=(:diallerProviderName)")
    void updatePrimaryDataset(@Param("diallerProviderName") String diallerProviderName, @Param("dataset") String dataset);
}
