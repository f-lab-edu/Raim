package flab.project.service;

import flab.project.domain.FileInfo;
import flab.project.domain.ImageType;
import flab.project.domain.User;
import flab.project.dto.FileInfoResponseDto;
import flab.project.dto.ProfileRequestDto;
import flab.project.dto.ProfileResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.FileInfoRepository;
import flab.project.repository.UserRepository;
import flab.project.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final String UPLOAD_DIR = "src/main/resources/static/images";

    private final UserRepository userRepository;
    private final FileInfoRepository fileInfoRepository;

    @Transactional
    public ProfileResponseDto updateProfile(String userEmail, ProfileRequestDto profileRequestDto,
                                            MultipartFile profileImage,
                                            MultipartFile backgroundImage) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));
        String newStatusMessage = profileRequestDto.getStatusMessage();

        if (profileImage != null) {
            FileInfo newProfileImageUrl = saveImage(user, ImageType.PROFILE, profileImage);
            user.updateProfileImage(newProfileImageUrl.getId());
        }

        if (backgroundImage != null) {
            FileInfo newBackgroundImageUrl = saveImage(user, ImageType.BACKGROUND, backgroundImage);
            user.updateBackgroundImage(newBackgroundImageUrl.getId());
        }

        if (!newStatusMessage.equals(user.getStatusMessage())) {
            user.updateStatusMessage(newStatusMessage);
        }

        userRepository.save(user);

        return ProfileResponseDto.of(user);
    }

    private FileInfo saveImage(User user, ImageType imageType, MultipartFile image) {
        String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());

        FileUtils.checkImageFileSize(image.getSize());
        FileUtils.checkImageContentType(image.getContentType());

        Path directory = FileUtils.createDirectoryStructure(UPLOAD_DIR, imageType.getDirectoryName());

        FileInfo fileInfo = new FileInfo(UUID.randomUUID().toString(), originalFilename, directory.toString(), imageType, user);
        String imageFileName = fileInfo.getId() + "_" + fileInfo.getFileName();

        FileUtils.saveImageFile(image, directory, imageFileName);

        fileInfoRepository.save(fileInfo);

        return fileInfo;
    }

    public List<FileInfoResponseDto> getProfileImages(String userEmail, ImageType imageType) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));

        Sort sort = Sort.by(Direction.DESC, "createdAt");

        return fileInfoRepository.findByUserAndImageType(user, imageType, sort).stream().map(FileInfoResponseDto::of).toList();
    }

    public byte[] getProfileImage(String userEmail, String imageId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));

        FileInfo fileInfo = fileInfoRepository.findById(imageId)
                .orElseThrow(() -> new KakaoException(ExceptionCode.FILE_NOT_FOUND));

        checkUserFileAuthority(user, fileInfo);

        Path imagePath = Path.of(fileInfo.getFileDirectory(), fileInfo.getId() + "_" + fileInfo.getFileName());

        byte[] file = null;
        try {
            if (Files.exists(imagePath)) {
                file = Files.readAllBytes(imagePath);
            }
        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }

        return file;
    }

    public void deleteImage(String userEmail, String imageId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));

        FileInfo fileInfo = fileInfoRepository.findById(imageId)
                .orElseThrow(() -> new KakaoException(ExceptionCode.FILE_NOT_FOUND));

        checkUserFileAuthority(user, fileInfo);

        fileInfoRepository.deleteById(imageId);
    }

    private void checkUserFileAuthority(User user, FileInfo fileInfo) {

        if (user.getId() != fileInfo.getUser().getId()) {
            throw new KakaoException(ExceptionCode.BAD_REQUEST);
        }
    }

    public ProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));

        return ProfileResponseDto.of(user);
    }
}
