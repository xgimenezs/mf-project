package com.mf.xgim.repository;

import com.mf.xgim.model.SiteResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.service
 * @since 1.0.0
 */
@Repository
public interface ISiteResultRepository extends CrudRepository<SiteResult, String> {



}
