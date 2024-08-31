package mc.project.online_store.service.util;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void postFile(MultipartFile file, String filePath);

    Resource getFile(String path);

    void deleteFile(String path);
}
