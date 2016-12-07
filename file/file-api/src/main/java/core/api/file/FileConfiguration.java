package core.api.file;

import core.api.file.api.FileMetaAPIService;
import core.framework.http.HTTPClient;
import core.framework.web.rest.client.APIClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.inject.Inject;

/**
 * @author chi
 */
@Configuration
@ComponentScan(value = "core.api.image", basePackageClasses = FileConfiguration.class)
public class FileConfiguration {
    @Inject
    FileServerSettings fileSettings;

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

   /* @Bean
    public HTTPClient httpClient() {
        return new HTTPClient();
    }*/

    @Bean
    public FileMetaAPIService fileMetaAPIService() {
        APIClientBuilder apiClientBuilder = new APIClientBuilder(fileSettings.getServerUrl(), new HTTPClient());
        return apiClientBuilder.build(FileMetaAPIService.class);
    }
}