package mycrm.services;

import mycrm.models.DocumentFolder;
import mycrm.models.DocumentFolderList;

import java.util.List;

public interface DocumentFolderService {
    DocumentFolder save(DocumentFolder documentFolder);

    List<DocumentFolder> findAll();

    List<DocumentFolderList> documentsFolderList();

    String folderNameById(Long heading);

    DocumentFolder saveSubfolder(Long parentId, String subfolderName);

    DocumentFolder findById(Long id);

    List<DocumentFolder> findParentFolders();

    boolean hasSubFolders(DocumentFolder documentFolder);

    void deleteFolder(Long id);
}
