package core.file.service;

import core.file.domain.FileAttribute;
import core.framework.database.JPAAccess;
import core.framework.database.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author rainbow.cai
 */
@Service
public class FileAttributeService {
    final Logger logger = LoggerFactory.getLogger(FileAttributeService.class);

    @Inject
    JPAAccess jpaAccess;

    @Transactional
    private void save(Long fileId, Map<String, String> attributes) {
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            FileAttribute attributeEntity = new FileAttribute();
            attributeEntity.setFileId(fileId);
            attributeEntity.setKey(entry.getKey());
            attributeEntity.setValue(String.valueOf(entry.getValue()));
            jpaAccess.save(attributeEntity);
        }
    }

    public void update(Long fileId, Map<String, String> attributes) {
        delete(fileId);
        save(fileId, attributes);
    }

    @Transactional
    private void delete(Long fileId) {
        List<FileAttribute> attributeEntities = find(fileId);
        if (attributeEntities == null) {
            logger.debug("Save fileAttributes.");
            return;
        }
        logger.debug("File attribute exits.Update File attributes.");
        for (FileAttribute entity : attributeEntities) {
            jpaAccess.delete(entity);
        }
    }

    private List<FileAttribute> find(Long fileId) {
        return jpaAccess.find(Query.create("from FileAttribute where fileId=:fileId").param("fileId", fileId));
    }
}
