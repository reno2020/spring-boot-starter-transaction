package org.throwable.ons.orm.entity;

import org.throwable.mapper.core.common.annotation.Column;
import org.throwable.mapper.core.common.annotation.Id;
import org.throwable.mapper.core.common.annotation.Table;
import org.throwable.ons.core.common.SerializableEntity;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/21 0:27
 */
@Table("t_pagination_context")
public class PaginationContext extends SerializableEntity{

	@Id(value = "id")
	private Long id;
	@Column(value = "page_number")
	private Long pageNumber;
	@Column(value = "page_size")
	private Long pageSize;
	@Column(value = "category")
	private String category;
	@Column(value = "finished")
	private Boolean finished;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
}
