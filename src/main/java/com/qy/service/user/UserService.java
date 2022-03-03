package com.qy.service.user;

import com.qy.pojo.User;

import java.util.List;

public interface UserService {
    //用户登录
    public User login(String userCode,String password);
    //根据用户id修改密码
    public boolean updatePwd(int id,String pwd);
    //获取用户数量
    public int getUserCount(String username,int userRole);
    //根据条件查询用户列表
    public List<User> getUserList(String userName,int userRole,int currentPageNo,int pageSize);
    //增加user
    public boolean addUser(User user);
    //查看用户
    public User viewUser(int id);
    //删除用户
    public boolean delUser(int id);
}
