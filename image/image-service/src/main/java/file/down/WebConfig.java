package file.down;

import core.api.image.ImageServerSettings;
import core.api.image.client.ImageUploadClient;
import core.framework.web.DefaultServiceConfig;
import file.down.service.FileRepository;
import file.down.service.FolderFileRepository;
import file.down.service.ImageScalar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.inject.Inject;
import java.io.File;

/**
 * @author rainbow.cai
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class)
public class WebConfig extends DefaultServiceConfig {

    @Inject
    Environment env;

    @Bean
    public ImageScalar imageScalar() {
        return new ImageScalar();
    }

    @Bean
    public ImageSettings imageSettings() {
        ImageSettings fileSettings = new ImageSettings();
        fileSettings.setDir(new File(env.getRequiredProperty("file.dir")));
        return fileSettings;
    }

    @Bean
    public FileRepository fileRepository() {
        return new FolderFileRepository(imageSettings().getDir());
    }

    @Bean
    public ImageServerSettings imageServerSettings() {
        ImageServerSettings imageServerSettings = new ImageServerSettings();
        imageServerSettings.setUploadServerUrl(env.getProperty("file.uploadServer"));
        return imageServerSettings;
    }


    @Bean
    public ImageUploadClient imageUploadClient() {
        return new ImageUploadClient();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/www/**").addResourceLocations("/www/");
        registry.addResourceHandler("index.html").addResourceLocations("/");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor());
    }

}
