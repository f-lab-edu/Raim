package flab.project.domain;

import lombok.Getter;

@Getter
public enum ImageType {

    PROFILE("profile"),
    BACKGROUND("background");

    private String directoryName;

    ImageType(String directoryName) {
        this.directoryName = directoryName;
    }
}
