package com.qy.service.user;

import com.qy.dao.BaseDao;
import com.qy.dao.user.UserDao;
import com.qy.dao.user.UserDaoImpl;
import com.qy.pojo.User;
import org.junit.Test;

import javax.xml.parsers.FactoryConfigurationError;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{
    //业务层都要调用Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao=new UserDaoImpl();
    }
    //用户登录
    public User login(String userCode, String password) {
        Connection connection=null;
        User user=null;

        try {
            connection= BaseDao.getConnection();
            user=userDao.getLoginUser(connection,userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }
    //根据用户Id修改密码
    public boolean updatePwd(int id, String pwd) {
        Connection connection=null;
        boolean flag=false;

        try {
            connection=BaseDao.getConnection();
            if(userDao.updatePwd(connection,id,pwd)>0){
                flag=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }
    //获取用户数量
    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;

        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }
    //根据条件查询用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection connection=null;
        List<User> userList = new ArrayList<User>();

        try {
            connection=BaseDao.getConnection();
            userList=userDao.getUserList(connection,userName,userRole,currentPageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userList;
    }

    public boolean addUser(User user) {
        Connection connection=null;
        boolean flag=false;

        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);
            int updateRow=userDao.addUser(connection,user);
            connection.commit();
            if(updateRow>0){
                flag=true;
                System.out.println("UserServiceImpl——>addUser:成功添加用户");
            }
            else{
                System.out.println("UserServiceImpl——>addUser:添加用户失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    public User viewUser(int id) {
        Connection connection=null;
        User user=null;

        try {
            connection=BaseDao.getConnection();
            user=userDao.viewUser(connection,id);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    public boolean delUser(int id) {
        Connection connection=null;
        boolean flag=false;

        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);
            int updateRow=userDao.delUser(connection,id);
            connection.commit();
            if(updateRow>0)
                flag=true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }


    public void test(){
    UserServiceImpl userService = new UserServiceImpl();
    int c=userService.getUserCount(null,2);
    System.out.println(c);
}
}