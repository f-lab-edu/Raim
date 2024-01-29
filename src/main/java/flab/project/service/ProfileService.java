package flab.project.service;

import flab.project.domain.User;
import flab.project.dto.ProfileRequestDto;
import flab.project.dto.ProfileResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final String UPLOAD_DIR = "src/main/resources/static/images";
    private static final String PROFILE = "/profile";
    private static final String BACKGROUND = "/background";

    private static final long MAX_FILE_SIZE = 5_242_800L;

    private static List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

    private final UserRepository userRepository;


    @Transactional
    public ProfileResponseDto updateProfile(String userEmail, ProfileRequestDto profileRequestDto,
                                            MultipartFile profileImage,
                                            MultipartFile backgroundImage) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));
        String newStatusMessage = profileRequestDto.getStatusMessage();

        if (profileImage != null) {
            String newProfileImageUrl = saveImage(PROFILE, profileImage);
            user.updateProfileImage(newProfileImageUrl);
        }

        if (backgroundImage != null) {
            String newBackgroundImageUrl = saveImage(BACKGROUND, backgroundImage);
            user.updateBackgroundImage(newBackgroundImageUrl);
        }

        if (!newStatusMessage.equals(user.getStatusMessage())) {
            user.updateStatusMessage(newStatusMessage);
        }

        userRepository.save(user);

        return ProfileResponseDto.of(user);
    }

    private String saveImage(String dir, MultipartFile image) {
        String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
        String contentType = image.getContentType();

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new KakaoException(ExceptionCode.FILE_TOO_LARGE);
        }

        if (!ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new KakaoException(ExceptionCode.BAD_FILE_TYPE);
        }

        String newFileName = UUID.randomUUID() + "_" + originalFilename;
        Path uploadPath = Paths.get(UPLOAD_DIR + dir);
        Path filePath = uploadPath.resolve(newFileName);

        try {
            if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);

            }

            image.transferTo(filePath);

        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }

        return newFileName;
    }

    public byte[] getProfileImage(String imageType, String imageName) {
        Path imagePath = Path.of(UPLOAD_DIR + imageType + imageName);

        try {
            if (Files.exists(imagePath)) {
                return Files.readAllBytes(imagePath);
            }
        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        } finally {
            throw new KakaoException(ExceptionCode.FILE_NOT_FOUND);
        }
    }

    public void deleteImage(String imageName) {

    }
}
