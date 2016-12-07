package core.file.web;

import core.api.file.api.FileMetaAPIService;
import core.api.file.api.FileMetaCreateRequest;
import core.api.file.api.ListImage;
import core.file.domain.FileMeta;
import core.file.service.FileMetaService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@RestController
public class FileMetaController implements FileMetaAPIService {
    @Inject
    FileMetaService fileMetaService;

    @Override
    public Map<String, Object> saveFile(@RequestBody FileMetaCreateRequest fileCreateRequest) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void deleteFile(@RequestParam("file") String path) {
        FileMeta fileMeta = fileMetaService.getByPath(path);
        if (fileMeta == null) {
            fileMeta = new FileMeta();
            fileMeta.setPath(path);
            fileMeta.setCreateTime(new Date());
            fileMeta.setLastUpdateTime(new Date());
            fileMeta.setFileName(path);
            fileMeta.setStatus(0);
            fileMeta.setLastUpdateTime(new Date());
            fileMetaService.save(fileMeta);
        } else {
            fileMeta.setStatus(0);
            fileMeta.setLastUpdateTime(new Date());
            fileMetaService.update(fileMeta);
        }
    }

    @Override
    public ListImage listByGroup(@RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "fetchSize", defaultValue = "20") int fetchSize, @RequestParam("group") String group) {
        List<FileMeta> list = fileMetaService.listByGroup(offset, fetchSize, group);
        ListImage listImage = new ListImage();
        for (FileMeta fileMeta : list) {
            listImage.addPath(fileMeta.getPath());
        }
        listImage.setState("SUCCESS");
        listImage.setStart(offset);
        listImage.setTotal(fileMetaService.countByGroup(group));
        return listImage;
    }
}
