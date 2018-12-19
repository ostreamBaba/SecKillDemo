package com.viscu.seckill.swagger.pojo;

/**
 * @ Create by ostreamBaba on 18-12-19
 * @ 描述
 */
public class User {

    private Long id;
    private String name;
    private int age;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}