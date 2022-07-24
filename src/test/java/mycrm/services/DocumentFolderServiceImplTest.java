package mycrm.services;

import mycrm.models.DocumentFolder;
import mycrm.models.DocumentFolderList;
import mycrm.repositories.DocumentFolderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentFolderServiceImplTest {

    @InjectMocks
    private DocumentFolderServiceImpl documentFolderService;

    @Mock
    private DocumentFolderRepository documentFolderRepository;

    @Test
    public void shouldSaveDocumentFolder() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        when(documentFolderRepository.save(any(DocumentFolder.class))).thenReturn(documentFolder);

        DocumentFolder savedDocumentFolder = documentFolderService.save(documentFolder);

        assertEquals(documentFolder.getFolderName(), savedDocumentFolder.getFolderName());
    }

    @Test
    public void shouldFindAllDocumentFolders() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        List<DocumentFolder> list = new ArrayList<>();
        list.add(documentFolder);

        when(documentFolderRepository.findAll()).thenReturn(list);

        List<DocumentFolder> documentFolderList = documentFolderService.findAll();

        assertEquals(list.get(0).getFolderName(), documentFolderList.get(0).getFolderName());
    }

    @Test
    public void shouldReturnDocumentFolderList() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        DocumentFolderList documentFolderList = DocumentFolderList
                .builder()
                .folder(documentFolder)
                .build();

        List<DocumentFolder> parentList = new ArrayList<>();
        parentList.add(documentFolder);

        List<DocumentFolderList> list = new ArrayList<>();
        list.add(documentFolderList);

        when(documentFolderRepository.findParents()).thenReturn(parentList);

        List<DocumentFolderList> returnedDocumentFolderList = documentFolderService.documentsFolderList();

        assertEquals(list.get(0).getFolder().getFolderName(),
                returnedDocumentFolderList.get(0).getFolder().getFolderName());
    }

    @Test
    public void shouldReturnFolderNameById() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        when(documentFolderRepository.findOne(any(Long.class))).thenReturn(documentFolder);

        String headingName = documentFolderService.folderNameById(1L);

        assertEquals("Folder", headingName);
    }

    @Test
    public void shouldReturnFolderById() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        when(documentFolderRepository.findOne(any(Long.class))).thenReturn(documentFolder);

        DocumentFolder returnedDocumentFolder = documentFolderService.findById(1L);

        assertEquals("Folder", returnedDocumentFolder.getFolderName());
    }

    @Test
    public void shouldSaveSubFolders() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        when(documentFolderRepository.save(any(DocumentFolder.class))).thenReturn(documentFolder);

        DocumentFolder returnedDocumentFolder = documentFolderService.saveSubfolder(0L, "Sub Folder");

        assertEquals(documentFolder.getFolderName(), returnedDocumentFolder.getFolderName());
    }

    @Test
    public void shouldReturnParentFolders() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        List<DocumentFolder> parentList = new ArrayList<>();
        parentList.add(documentFolder);

        when(documentFolderRepository.findParents()).thenReturn(parentList);

        List<DocumentFolder> parentFolders = documentFolderService.findParentFolders();

        assertEquals(parentList.get(0).getFolderName(), parentFolders.get(0).getFolderName());
    }

    @Test
    public void shouldCheckIfThereAreSubFolders() {
        DocumentFolder documentFolder = DocumentFolder
                .builder()
                .folderName("Folder")
                .parentFolderId(0L)
                .build();

        List<DocumentFolder> documentFolderList = new ArrayList<>();

        when(documentFolderRepository.findChildren(0L)).thenReturn(documentFolderList);

        boolean hasSubFolders = documentFolderService.hasSubFolders(documentFolder);

        assertFalse(hasSubFolders);
    }

    @Test
    public void shouldDeleteFolder() {
        documentFolderService.deleteFolder(0L);

        verify(documentFolderRepository, times(1)).delete(any(Long.class));
    }
}
