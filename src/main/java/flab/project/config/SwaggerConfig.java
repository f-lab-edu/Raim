package flab.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Kakaotalk Api")
                .description("Kakaotalk 백엔드를 클론해보는 F-lab 프로젝트")
                .version("1.0.0");
    }

    @Bean
    public GroupedOpenApi userGroup() {
        List<Tag> tags = List.of(
                new Tag().name("User API").description("User 회원 가입 및 인증 등을 제공하는 API")
        );

        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/api/users/**")
                .addOpenApiCustomizer(openApi -> openApi.setTags(tags))
                .build();
    }

    @Bean
    public GroupedOpenApi termGroup() {
        List<Tag> tags = List.of(
                new Tag().name("Term API").description("약관 정보 확인 API")
        );

        return GroupedOpenApi.builder()
                .group("Term")
                .pathsToMatch("/api/term/**")
                .addOpenApiCustomizer(openApi -> openApi.setTags(tags))
                .build();
    }

    @Bean
    public GroupedOpenApi chatGroup() {
        List<Tag> tags = List.of(
                new Tag().name("Chat API").description("채팅 API")
        );

        return GroupedOpenApi.builder()
                .group("Chat")
                .pathsToMatch("/api/chat/**")
                .addOpenApiCustomizer(openApi -> openApi.setTags(tags))
                .build();
    }
}
