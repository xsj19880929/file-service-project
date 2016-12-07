package core.file.service;

import com.google.common.io.Files;
import core.file.domain.FileMeta;
import core.framework.database.JPAAccess;
import core.framework.database.Query;
import core.framework.database.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * @author rainbow.cai
 */
@Service
public class FileMetaService {
    @Inject
    JPAAccess jpaAccess;

    public List<FileMeta> list(int offset, int fetchSize) {
        return jpaAccess.find(Query.create("from FileMeta where status=1 order by lastUpdateTime ").from(offset).fetch(fetchSize));
    }

    public List<FileMeta> listByGroup(int offset, int fetchSize, String group) {
        return jpaAccess.find(QueryBuilder.query("from FileMeta ").append("status", 1).append("group", group).skipEmptyFields().orderBy("lastUpdateTime").desc().from(offset).fetch(fetchSize).build());
    }

    public List<FileMeta> search(String searchKey, String searchText, int offset, int fetchSize) {
        return jpaAccess.find(Query.create("from FileMeta where " + searchKey + " like :" + searchKey + " and status=1").param(searchKey, searchText).from(offset).fetch(fetchSize));
    }

    public int count() {
        return ((Long) jpaAccess.findOne(Query.create("select count(f.id) from FileMeta f where status=1"))).intValue();
    }

    public int countByGroup(String group) {
        return ((Long) jpaAccess.findOne(Query.create("select count(f.id) from FileMeta f where status=1 and group =:group").param("group", group))).intValue();
    }

    public int searchCount(String searchKey, String searchText) {
        return ((Long) jpaAccess.findOne(Query.create("select count(f.id) from FileMeta f where " + searchKey + " like :" + searchKey + " and status=1").param(searchKey, searchText))).intValue();
    }

    @Transactional
    public void saveOrUpdate(FileMeta fileMeta) {
        FileMeta oldFileMeta = this.getByPath(fileMeta.getPath());
        if (oldFileMeta == null) {
            this.save(fileMeta);
        } else {
            fileMeta.setId(oldFileMeta.getId());
            fileMeta.setCreateTime(oldFileMeta.getCreateTime());
            fileMeta.setType(Files.getFileExtension(fileMeta.getPath()));
            this.update(fileMeta);
        }
    }

    public FileMeta getByPath(String path) {
        return jpaAccess.findOne(Query.create("from FileMeta where path=:path").param("path", path));
    }

    @Transactional
    public void save(FileMeta fileMeta) {
        fileMeta.setType(Files.getFileExtension(fileMeta.getPath()));
        jpaAccess.save(fileMeta);
    }

    @Transactional
    public void update(FileMeta fileMeta) {
        fileMeta.setLastUpdateTime(new Date());
        jpaAccess.update(fileMeta);
    }
}
