package com.enniu.qa.apm.service;

import com.enniu.qa.apm.configuration.Config;
import com.enniu.qa.apm.dao.UserDao;
import com.enniu.qa.apm.security.SecuredUser;
import org.apache.commons.lang.StringUtils;
import org.ngrinder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by fuyong on 7/23/15.
 */

@Service
public class UserService {
    @Autowired
    UserDao dao;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private Config config;
	/*@Autowired
	private SaltSource saltSource;*/

    private Cache userCache;

    @Autowired
    private FileEntryService scriptService;

    @PostConstruct
    public void init() {
        userCache = cacheManager.getCache("users");
    }

    @Cacheable("users")
    public User getUserById(String userId) {
        return dao.getOne(Long.parseLong(userId));
    }

    public User getUserByName(String userName){
        return dao.findByUserName(userName);
    }

     /**
     * Encoding given user's password.
     *
     * @param user user
     */
    public void encodePassword(User user) {
        if (StringUtils.isNotBlank(user.getPassword())) {
            SecuredUser securedUser = new SecuredUser(user, null);
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
        }
    }


    /**
     * Save user.
     *
     * @param user include id, userID, fullName, role, password.
     * @return User
     */
    @CachePut(value = "users", key = "#user.userId")
    public User save(User user) {
        encodePassword(user);
        return saveWithoutPasswordEncoding(user);
    }


    @Transactional
    @CachePut(value = "users", key = "#user.userId")
    public User saveWithoutPasswordEncoding(User user) {
        final List<User> followers = getFollowers(user.getFollowersStr());
        user.setFollowers(followers);
        if (user.getPassword() != null && StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        }
        final User existing = dao.getOne(Long.parseLong(user.getUserId()));
        if (existing != null) {
            // First expire existing followers.
            final List<User> existingFollowers = existing.getFollowers();
            if (existingFollowers != null) {
                for (User eachFollower : existingFollowers) {
                    userCache.evict(eachFollower.getUserId());
                }
            }
            user = existing.merge(user);
        }
        User createdUser = dao.save(user);
        // Then expires new followers so that new followers info can be loaded.
        for (User eachFollower : followers) {
            userCache.evict(eachFollower.getUserId());
        }
        prepareUserEnv(createdUser);
        return createdUser;
    }

    private void prepareUserEnv(User user) {
        scriptService.prepare(user);
    }

    private List<User> getFollowers(String followersStr) {
        List<User> newShareUsers = new ArrayList<User>();
        String[] userIds = StringUtils.split(StringUtils.trimToEmpty(followersStr), ',');
        for (String userId : userIds) {
            User shareUser = dao.getOne(Long.parseLong(userId.trim()));
            if (shareUser != null) {
                newShareUsers.add(shareUser);
            }
        }
        return newShareUsers;
    }

}
