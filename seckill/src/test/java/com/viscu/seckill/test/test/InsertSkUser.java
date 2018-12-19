package com.viscu.seckill.test;
import com.viscu.seckill.dao.SkUserDao;
import com.viscu.seckill.domain.SkUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 描述
 */

@Controller
@RequestMapping("/insert")
public class InsertSkUser {

    @Autowired(required = false)
    private SkUserDao skUserDao;

    @RequestMapping("/user")
    @ResponseBody
    public boolean user(){
        SkUser skUser = skUserDao.getById(13421474495L);
        long id = 30000000000L;
        for (int i = 0; i < 100; i++) {
            id+=i*10;
            skUser.setId(id);
            try{
                skUserDao.insert(skUser);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }


}
