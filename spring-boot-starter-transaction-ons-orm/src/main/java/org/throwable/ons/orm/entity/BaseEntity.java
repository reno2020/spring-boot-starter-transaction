package org.throwable.ons.orm.entity;

import org.throwable.mapper.core.common.annotation.Column;
import org.throwable.mapper.core.common.annotation.Id;
import org.throwable.ons.core.common.SerializableEntity;

import java.time.LocalDateTime;


/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 12:14
 */

public class BaseEntity extends SerializableEntity {

    @Id(value = "id")
    private Long id;

    @Column(value = "is_del")
    private byte isDel;

    @Column(value = "version")
    private Long version;

    @Column(value = "create_time")
    private LocalDateTime createTime;

    @Column(value = "creator")
    private String creator;

    @Column(value = "edit_time")
    private LocalDateTime editTime;

    @Column(value = "editor")
    private String editor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getIsDel() {
        return isDel;
    }

    public void setIsDel(byte isDel) {
        this.isDel = isDel;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getEditTime() {
        return editTime;
    }

    public void setEditTime(LocalDateTime editTime) {
        this.editTime = editTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
