package mycrm.repositories;

import mycrm.models.DocumentFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentFolderRepository extends JpaRepository<DocumentFolder, Long> {

    @Query("SELECT f FROM DocumentFolder f WHERE f.parentFolderId=(:id) order by f.id asc")
    List<DocumentFolder> findChildren(@Param("id") Long id);

    @Query("SELECT f FROM DocumentFolder f WHERE f.parentFolderId=0 order by f.id asc")
    List<DocumentFolder> findParents();
}
