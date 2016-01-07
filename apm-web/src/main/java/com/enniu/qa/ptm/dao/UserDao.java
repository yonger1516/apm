package com.enniu.qa.ptm.dao;

import com.enniu.qa.ptm.model.Role;
import org.ngrinder.model.User;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2015/8/12 0012.
 */

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    public List<User> findByRole(Role role);

    public User findByUserName(String name);
}
