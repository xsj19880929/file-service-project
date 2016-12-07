package core.file.controller;

import core.file.SpringTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import java.io.FileInputStream;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadControllerTest extends SpringTest {
    @Ignore
    @Test
    public void testUpload() throws Exception {


        FileInputStream fis = new FileInputStream("C:\\Users\\rainbow.cai\\Desktop\\test\\test.iml");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
        mockMvc.perform(
                post("/upload")
                        .content(multipartFile.getBytes())
                        .contentType(mediaType))
                .andExpect(status().isOk());
    }


}
