package core.api.file.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author rainbow.cai
 */
public interface FileMetaAPIService {
    @RequestMapping(value = "/file-meta", method = RequestMethod.PUT)
    @ResponseBody
    Map<String, Object> saveFile(@RequestBody FileMetaCreateRequest fileCreateRequest);


    @RequestMapping(value = "/file-meta", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteFile(@RequestParam("file") String file);

    @RequestMapping(value = "/file-meta/list", method = RequestMethod.GET)
    @ResponseBody
    ListImage listByGroup(@RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "fetchSize", defaultValue = "20") int fetchSize, @RequestParam("group") String group);
}

