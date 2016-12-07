package core.api.image.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author rainbow.cai
 */
public interface FileMetaAPIService {
    @RequestMapping(value = "/file-meta", method = RequestMethod.PUT)
    @ResponseBody
    Map<String, Object> saveOrUpdate(@RequestBody FileMetaUpdateRequest fileMetaUpdateRequest);
}
