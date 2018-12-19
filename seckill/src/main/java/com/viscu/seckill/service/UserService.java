package com.viscu.seckill.service;

import com.viscu.seckill.domain.User;
import com.viscu.seckill.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Service
public class UserService {

    @Autowired(required = false)
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    //@Transactional
    public boolean tx(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("222");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u1.setName("111");
        userDao.insert(u2);

        return true;
    }

}
