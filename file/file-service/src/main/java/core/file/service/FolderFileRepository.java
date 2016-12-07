package core.file.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import core.api.file.token.TokenBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

/**
 * @author chi
 */
public class FolderFileRepository implements FileRepository {
    private static final String PATH_SUFFIX = "/private/";
    private final File dir;

    public FolderFileRepository(File dir) {
        this.dir = dir;
    }

    @Override
    public InputStream get(String path) {
        File file = new File(dir, path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public String put(InputStream inputStream, String fileExtension) {
        return this.put(inputStream, fileExtension, false);
    }

    @Override
    public String put(InputStream inputStream, String fileExtension, Boolean isPrivate) {
        return this.put(inputStream, "", fileExtension, isPrivate);
    }

    @Override
    public String put(InputStream inputStream, String tag, String fileExtension, Boolean isPrivate) {
        try {
            byte[] content = ByteStreams.toByteArray(inputStream);

            String path = DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + Hashing.md5().hashBytes(content).toString() + '.' + fileExtension;
            if (!Strings.isNullOrEmpty(tag)) {
                path = tag + "/" + path;
            }
            if (isPrivate) {
                path = "private/" + path;
            }
            File file = new File(dir, path);
            Files.createParentDirs(file);
            Files.write(content, file);
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream get(String path, String cookie, String tokenStr) {
        if (!path.startsWith(PATH_SUFFIX)) return get(path);
        if (StringUtils.isEmpty(tokenStr)) throw new RuntimeException(" token is require");
        if (StringUtils.isEmpty(cookie)) throw new RuntimeException(" cookie is require");

        try {
            TokenBuilder.Token token = new TokenBuilder(new TokenBuilder.Token(tokenStr)).decode();
            if (cookie.equals(token.getCookie()) && path.equals(token.getPath())) {
                Date date = DateUtils.parseDate(token.getDate(), "yyyyMMddHHmmss");
                if (Seconds.secondsBetween(new DateTime(date), DateTime.now()).getSeconds() > 3600) {
                    throw new RuntimeException(" token is time out");
                }
                return get(path);
            } else {
                throw new RuntimeException(" token is Illegal");
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}