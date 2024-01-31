package flab.project.util;

import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    private static final long MAX_IMAGE_FILE_SIZE = 5_242_800L;
    private static List<String> IMAGE_ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

    public static Path createDirectoryStructure(String baseDirectory, String dir) {
        LocalDate now = LocalDate.now();
        String year = DateTimeFormatter.ofPattern("yyyy").format(now);
        String month = DateTimeFormatter.ofPattern("MM").format(now);
        String day = DateTimeFormatter.ofPattern("dd").format(now);
        String path = year + File.separator + month + File.separator + day;
        Path directory = Paths.get(baseDirectory, dir, path);

        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new KakaoException(ExceptionCode.SERVER_ERROR);
            }
        }

        return directory;
    }

    public static long getFileCount(Path dir) {
        try(Stream<Path> stream = Files.list(dir)) {
            return stream.count();
        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }
    }

    public static void saveImageFile(MultipartFile image, Path dir, String fileName) {
        Path filePath = dir.resolve(fileName);

        try {
            image.transferTo(filePath);
        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }
    }

    public static void checkImageFileSize(long fileSize) {
        if (fileSize > MAX_IMAGE_FILE_SIZE) {
            throw new KakaoException(ExceptionCode.FILE_TOO_LARGE);
        }
    }

    public static void checkImageContentType(String contentType) {
        if (!IMAGE_ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new KakaoException(ExceptionCode.BAD_FILE_TYPE);
        }
    }
}
