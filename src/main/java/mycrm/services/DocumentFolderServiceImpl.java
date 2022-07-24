package mycrm.services;

import mycrm.models.DocumentFolder;
import mycrm.models.DocumentFolderList;
import mycrm.repositories.DocumentFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class DocumentFolderServiceImpl implements DocumentFolderService {
    private final DocumentFolderRepository documentFolderRepository;

    @Autowired
    public DocumentFolderServiceImpl(DocumentFolderRepository documentFolderRepository) {
        this.documentFolderRepository = documentFolderRepository;
    }

    @Override
    public DocumentFolder save(DocumentFolder documentFolder) {
        return this.documentFolderRepository.save(documentFolder);
    }

    @Override
    public List<DocumentFolder> findAll() {
        return this.documentFolderRepository.findAll();
    }

    @Override
    public List<DocumentFolderList> documentsFolderList() {
        List<DocumentFolderList> documentFolderLists = new ArrayList<>();
        List<DocumentFolder> parents = findParents();

        parents.forEach(documentFolder -> documentFolderLists.add(DocumentFolderList
                .builder()
                .folder(documentFolder)
                .subfolders(findChildren(documentFolder.getId()))
                .build()
        ));

        return documentFolderLists;
    }

    @Override
    public String folderNameById(Long heading) {
        DocumentFolder folder = this.documentFolderRepository.findOne(heading);

        return folder.getFolderName();
    }

    @Override
    public DocumentFolder saveSubfolder(Long parentId, String subfolderName) {
        return this.documentFolderRepository.save(DocumentFolder
                .builder()
                .folderName(subfolderName)
                .parentFolderId(parentId)
                .build());
    }

    @Override
    public DocumentFolder findById(Long id) {
        return this.documentFolderRepository.findOne(id);
    }

    @Override
    public List<DocumentFolder> findParentFolders() {
        return this.documentFolderRepository.findParents();
    }

    @Override
    public boolean hasSubFolders(DocumentFolder documentFolder) {
        return findChildren(documentFolder.getId()).size() > 0;
    }

    @Override
    public void deleteFolder(Long id) {
        this.documentFolderRepository.delete(id);
    }

    private List<DocumentFolder> findParents() {
        return this.documentFolderRepository.findParents();
    }

    private List<DocumentFolder> findChildren(Long id) {
        return this.documentFolderRepository.findChildren(id);
    }
}
