package mc.project.online_store.service.impl;

import lombok.RequiredArgsConstructor;
import mc.project.online_store.exception.FileException;
import mc.project.online_store.service.util.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Override
    public void postFile(MultipartFile file, String filePath) {
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new FileException("Cannot create file");
        }
    }

    @Override
    public Resource getFile(String path) {
        Resource resource;

        try {
            Path filePath = Paths.get(path).toAbsolutePath().normalize();
            resource = UrlResource.from(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new FileException("File not found");
            }
        } catch (Exception e) {
            throw new FileException("File not found");
        }

        return resource;
    }

    @Override
    public void deleteFile(String path) {
        (new File(path)).delete();
    }
}
