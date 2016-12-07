package core.api.file.client;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import core.api.file.FileServerSettings;
import core.api.file.controller.FileUploadResponse;
import core.framework.util.JSONBinder;
import core.framework.util.TimeLength;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author nick.guo
 */
@Component
public class FileUploadClient {
    private final TimeLength timeOut = TimeLength.minutes(2);
    HttpClient httpClient;
    @Inject
    FileServerSettings fileSettings;

    @PostConstruct
    public void init() {
        httpClient = build();
    }

    @PreDestroy
    public void destroy() throws IOException {
        ((CloseableHttpClient) httpClient).close();
    }

    private CloseableHttpClient build() {
        try {
            HttpClientBuilder builder = HttpClients.custom();
            builder.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .setSslcontext(new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build());
            // builder will use PoolingHttpClientConnectionManager by default
            builder.setDefaultSocketConfig(SocketConfig.custom()
                .setSoKeepAlive(true).build());
            builder.setDefaultRequestConfig(RequestConfig.custom()
                .setSocketTimeout((int) timeOut.toMilliseconds())
                .setConnectTimeout((int) timeOut.toMilliseconds()).build());
            return builder.build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    public FileUploadResponse upload(MultipartFile file) throws Exception {
        return this.upload(file, "");
    }

    public FileUploadResponse upload(MultipartFile file, Boolean isPrivate) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename(), "", isPrivate);
    }

    public FileUploadResponse upload(MultipartFile file, String tag, Boolean isPrivate) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename(), tag, "", isPrivate);
    }

    public FileUploadResponse upload(MultipartFile file, String group) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename(), group);
    }

    public FileUploadResponse upload(MultipartFile file, String group, String tag) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename(), tag, group, false);
    }

    public FileUploadResponse upload(InputStream in, String originalFilename) throws Exception {
        return this.upload(in, originalFilename, "");
    }

    public FileUploadResponse upload(InputStream in, String originalFilename, String group) throws Exception {
        return this.upload(in, originalFilename, group, false);
    }

    public FileUploadResponse upload(InputStream in, String originalFilename, String group, Boolean isPrivate) throws Exception {
        return this.upload(in, originalFilename, "", group, isPrivate);
    }

    public FileUploadResponse upload(InputStream in, String originalFilename, String tag, String group, Boolean isPrivate) throws Exception {
        HttpPost post = new HttpPost(fileSettings.getServerUrl() + "/file/upload");
        post.setEntity(MultipartEntityBuilder.create().addBinaryBody("file", in, ContentType.MULTIPART_FORM_DATA, originalFilename).addBinaryBody("group", group.getBytes())
            .addBinaryBody("tag", Strings.isNullOrEmpty(tag) ? fileSettings.getTag().getBytes() : tag.getBytes())
            .addBinaryBody("isPrivate", isPrivate.toString().getBytes()).build());
        HttpResponse response = httpClient.execute(post);
        Preconditions.checkState(HttpStatus.SC_OK == response.getStatusLine().getStatusCode());
        return JSONBinder.fromJSON(FileUploadResponse.class, EntityUtils.toString(response.getEntity()));
    }

    public FileUploadResponse upload(File file) throws Exception {
        return this.upload(FileUtils.openInputStream(file), file.getName());
    }


}
