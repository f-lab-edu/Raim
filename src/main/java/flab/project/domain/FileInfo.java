package flab.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FileInfo extends BaseEntity {

    @Id
    private String id;
    private String fileName;
    private String fileDirectory;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public FileInfo(String id, String fileName, String fileDirectory, ImageType imageType, User user) {
        this.id = id;
        this.fileName = fileName;
        this.fileDirectory = fileDirectory;
        this.imageType = imageType;
        this.user = user;
    }
}
