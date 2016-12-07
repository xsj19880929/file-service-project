package core.file;

import core.file.service.FileRepository;
import core.file.service.FolderFileRepository;
import core.framework.web.DefaultServiceConfig;
import core.framework.web.site.SiteSettings;
import core.framework.web.site.cookie.CookieContext;
import core.framework.web.site.cookie.CookieInterceptor;
import core.framework.web.site.scheme.HTTPSchemeEnforceInterceptor;
import core.framework.web.site.session.SecureSessionContext;
import core.framework.web.site.session.SessionContext;
import core.framework.web.site.session.SessionInterceptor;
import file.down.ImageConfig;
import file.down.service.ImageScalar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.inject.Inject;
import java.io.File;

/**
 * @author rainbow.cai
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class)
@EnableTransactionManagement(proxyTargetClass = true)
@Import(ImageConfig.class)
public class WebConfig extends DefaultServiceConfig {
    @Inject
    Environment env;

    @Bean
    public ImageScalar imageScalar() {
        return new ImageScalar();
    }

    @Bean
    SiteSettings siteSettings() {
        return new SiteSettings();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    CookieContext cookieContext() {
        return new CookieContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    SessionContext sessionContext() {
        return new SessionContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    SecureSessionContext secureSessionContext() {
        return new SecureSessionContext();
    }

    @Bean
    public CookieInterceptor cookieInterceptor() {
        return new CookieInterceptor();
    }

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    public HTTPSchemeEnforceInterceptor httpSchemeEnforceInterceptor() {
        return new HTTPSchemeEnforceInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/www/**").addResourceLocations("/www/");
        registry.addResourceHandler("index.html").addResourceLocations("/");
        //registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor());
        registry.addInterceptor(httpSchemeEnforceInterceptor());
        registry.addInterceptor(cookieInterceptor());
        registry.addInterceptor(sessionInterceptor());
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public FileSettings fileSettings() {
        FileSettings fileSettings = new FileSettings();
        fileSettings.setDir(new File(env.getRequiredProperty("file.dir")));
        return fileSettings;
    }

    @Bean
    public FileRepository fileRepository() {
        return new FolderFileRepository(fileSettings().getDir());
    }


}
