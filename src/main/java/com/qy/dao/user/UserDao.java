package com.qy.dao.user;

import com.qy.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到要登陆的用户
    public User getLoginUser(Connection connection,String userCode) throws SQLException;
    //修改当前用户密码
    public int updatePwd(Connection connection,int id,String password) throws  SQLException;
    //查询用户数量
    public int getUserCount(Connection connection,String username,int userRole) throws SQLException;
    //通过用户查询
    public List<User> getUserList(Connection connection,String userName,int userRole,int currentPageNo,int pageSize) throws Exception;
    //添加用户
    public int addUser(Connection connection,User user) throws SQLException;
    //查看用户
    public User viewUser(Connection connection,int id) throws SQLException;
    //删除用户
    public int delUser(Connection connection,int id) throws SQLException;
}
