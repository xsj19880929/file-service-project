package core.file.service;

import core.file.SpringTest;
import core.file.domain.FileMeta;
import org.junit.Test;

import javax.inject.Inject;

public class FileMetaServiceTest extends SpringTest {

    @Inject
    FileMetaService fileMetaService;

    @Test
    public void test() {
        FileMeta fileMeta = new FileMeta();
        fileMeta.setPath("/file/");
        fileMeta.setFileName("test");
        fileMeta.setSize(1l);
        //fileMeta.setHost(request.getRemoteHost());
        fileMetaService.save(fileMeta);

        //FileMeta fileMeta1 = fileMetaService.getByPath("/file/");

        FileMeta fileMeta2 = new FileMeta();
        fileMeta2.setPath("/file/");
        fileMeta2.setFileName("test");
        fileMeta2.setSize(1l);

        fileMetaService.saveOrUpdate(fileMeta2);
    }

}