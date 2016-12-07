package core.file.controller;

import core.file.SpringTest;
import core.file.domain.FileMeta;
import core.file.service.FileMetaService;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerTest extends SpringTest {
    @Inject
    FileMetaService fileService;

    @Before
    public void setup() {
        FileMeta fileEntity = new FileMeta();
        fileEntity.setFileName("file1");
        fileEntity.setHost("localhost");
        fileEntity.setPath("/xx/yy.img");
        fileEntity.setStatus(1);
        fileService.save(fileEntity);
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(get("/file?offset=0&fetchSize=10")).andExpect(status().isOk()).andReturn();
//        Assert.assertTrue(!JSONBinder.fromJSON(FindResult.class, mvcResult.getResponse().getContentAsString()).getList().isEmpty());
    }


    @Test
    public void testSearch() throws Exception {
        mockMvc.perform(get("/file/search?search=localhost&offset=0&fetchSize=10")).andExpect(status().isOk()).andReturn();
//        Assert.assertTrue(!JSONBinder.fromJSON(FindResult.class, mvcResult.getResponse().getContentAsString()).getList().isEmpty());
    }

}