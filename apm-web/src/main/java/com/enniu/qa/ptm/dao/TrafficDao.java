package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2015/8/11 0011.
 */
@Transactional
public interface TrafficDao extends JpaRepository<Traffic,Long> {

    public Traffic findById(long id);
}
