package com.enniu.qa.apm.dao;

import com.enniu.qa.apm.model.Role;
import org.ngrinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2015/8/12 0012.
 */

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    public List<User> findByRole(Role role);

    public User findByUserName(String name);
}
