package com.iyonger.apm.web.dao;

import com.iyonger.apm.web.model.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2015/8/11 0011.
 */
@Repository
public interface TrafficDao extends JpaRepository<Traffic,Long> {

    public Traffic findById(long id);
}
