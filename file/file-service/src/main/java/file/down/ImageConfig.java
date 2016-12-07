package file.down;

import core.api.file.FileServerSettings;
import core.api.image.client.ImageUploadClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * @author nick.guo
 */
@Configuration
@ComponentScan(basePackageClasses = ImageConfig.class)
public class ImageConfig {
    @Inject
    Environment env;

    @Bean
    public FileServerSettings fileServerSettings() {
        FileServerSettings fileServerSettings = new FileServerSettings();
        fileServerSettings.setServerUrl(env.getProperty("file.uploadServer"));
        return fileServerSettings;
    }


    @Bean
    public ImageUploadClient imageUploadClient() {
        return new ImageUploadClient();
    }
}
