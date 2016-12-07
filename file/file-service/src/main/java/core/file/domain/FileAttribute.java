package core.file.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author nick.guo
 */
@Entity
@Table(name = "file_attribute")
public class FileAttribute {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
    @Column(name = "file_id")
    private Long fileId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
