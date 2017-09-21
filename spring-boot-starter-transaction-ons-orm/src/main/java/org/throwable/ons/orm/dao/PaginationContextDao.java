package org.throwable.ons.orm.dao;

import org.apache.ibatis.annotations.Mapper;
import org.throwable.mapper.DogeMapper;
import org.throwable.ons.orm.entity.PaginationContext;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/21 0:40
 */
@Mapper
public interface PaginationContextDao extends DogeMapper<Long, PaginationContext> {
}
