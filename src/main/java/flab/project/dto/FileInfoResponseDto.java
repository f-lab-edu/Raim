package flab.project.dto;

import flab.project.domain.FileInfo;
import flab.project.domain.ImageType;
import lombok.Getter;

@Getter
public class FileInfoResponseDto {

    private String imageId;
    private String imageDirectory;
    private String fileName;
    private ImageType imageType;

    private FileInfoResponseDto(String imageId, String imageDirectory, String fileName, ImageType imageType) {
        this.imageId = imageId;
        this.imageDirectory = imageDirectory;
        this.fileName = fileName;
        this.imageType = imageType;
    }

    public static FileInfoResponseDto of(FileInfo fileInfo) {
        return new FileInfoResponseDto(fileInfo.getId(), fileInfo.getFileDirectory(), fileInfo.getFileName(),
                fileInfo.getImageType());
    }
}


