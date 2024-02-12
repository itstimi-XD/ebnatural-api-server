package ebnatural.bizcurator.apiserver.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("v1.0.0 definition")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI springBizCuratorAPI(){
        return new OpenAPI()
                .info(new Info().title("BizCurator API")
                .description("BizCurator 프로젝트 API 명세서 입니다.")
                .version("v1.0.0")
                );
    }
}
