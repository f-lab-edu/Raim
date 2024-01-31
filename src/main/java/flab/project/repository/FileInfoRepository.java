package flab.project.repository;

import flab.project.domain.FileInfo;
import flab.project.domain.ImageType;
import flab.project.domain.User;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String> {

    List<FileInfo> findByUserAndImageType(User user, ImageType imageType, Sort sort);
}
