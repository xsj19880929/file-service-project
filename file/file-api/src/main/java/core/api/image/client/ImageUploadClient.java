package core.api.image.client;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import core.api.file.FileServerSettings;
import core.api.image.controller.ImageUploadResponse;
import core.framework.util.JSONBinder;
import core.framework.util.TimeLength;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
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
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author nick.guo
 */
@Component
public class ImageUploadClient {
    private final TimeLength timeOut = TimeLength.minutes(2);
    HttpClient httpClient;
    @Inject
    FileServerSettings fileServerSettings;

    @PostConstruct
    public void init() {
        httpClient = build();
    }

    public ImageUploadResponse upload(MultipartFile file) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename(), "");
    }

    public ImageUploadResponse upload(MultipartFile file, Boolean isPrivate) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename(), "", isPrivate);
    }

    public ImageUploadResponse upload(MultipartFile file, String tag, Boolean isPrivate) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename(), tag, "", isPrivate);
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

    public ImageUploadResponse upload(InputStream in, String originalFilename, String group) throws Exception {
        return this.upload(in, originalFilename, group, false);
    }

    public ImageUploadResponse upload(InputStream in, String originalFilename, String group, Boolean isPrivate) throws Exception {
        return this.upload(in, originalFilename, "", group, isPrivate);
    }

    public ImageUploadResponse upload(InputStream in, String originalFilename, String tag, String group, Boolean isPrivate) throws Exception {
        HttpPost post = new HttpPost(fileServerSettings.getServerUrl() + "/file/upload");
        post.setEntity(MultipartEntityBuilder.create().addBinaryBody("file", in, ContentType.MULTIPART_FORM_DATA, originalFilename)
            .addBinaryBody("group", group.getBytes()).addBinaryBody("isPrivate", isPrivate.toString().getBytes())
            .addBinaryBody("tag", Strings.isNullOrEmpty(tag) ? fileServerSettings.getTag().getBytes() : tag.getBytes()).build());
        HttpResponse response = httpClient.execute(post);
        Preconditions.checkState(HttpStatus.SC_OK == response.getStatusLine().getStatusCode());
        return JSONBinder.fromJSON(ImageUploadResponse.class, EntityUtils.toString(response.getEntity()));
    }


    public InputStream down(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        Preconditions.checkState(HttpStatus.SC_OK == response.getStatusLine().getStatusCode());
        return response.getEntity().getContent();
    }

    /*public static void main(String[] args) throws Exception {
        String url = "http://image.dev.ygccw.com/image/file/20151211/c4729be3e5d50d24d1863deeb026e2ae.jpg?raw=true";
        ImageUploadClient ojb = new ImageUploadClient();
        ojb.httpClient = ojb.build();
        System.out.println(ByteStreams.toByteArray(ojb.down(url)).length);

    }*/
}
