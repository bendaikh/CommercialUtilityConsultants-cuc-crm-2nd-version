package mycrm.services;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadFile(MultipartFile multiPartFile) throws Exception;
}
