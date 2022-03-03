package com.qy.dao.user;

import com.mysql.jdbc.StringUtils;
import com.qy.dao.BaseDao;
import com.qy.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    //得到要登陆的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement pstm=null;
        ResultSet rs=null;
        User user=null;

        if(connection!=null){
            String sql="select * from smbms_user where userCode=?";
            Object[] params={userCode};

            rs= BaseDao.execute(connection,pstm,rs,sql,params);

            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return user;
    }
    //修改当前用户密码
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement preparedStatement=null;
        int execute=0;
        if(connection!=null){
            String sql="update smbms_user set userPassword = ? where id = ?";
            Object params[]={password,id};
            execute=BaseDao.execute(connection,preparedStatement,sql,params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return execute;
    }
    //查询用户数量
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        int count=0;

        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            ArrayList<Object> list = new ArrayList<Object>();

            sql.append("select count(*) as count from smbms_user u,smbms_role r where u.userRole=r.id");
            if(username!=null){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }
            if(userRole>0){
                sql.append(" and u.userRole =?");
                list.add(userRole);
            }

            Object[] params = list.toArray();
            System.out.println("UserDaoImpl->getUserCount"+sql.toString());

            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            if(rs.next()){
                count= rs.getInt("count");
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return count;
    }
    //根据条件查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        List<User> userList=new ArrayList<User>();

        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<Object>();

            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo=(currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("UserDaoImpl——>getUserList:" + sql.toString());

            rs=BaseDao.execute(connection,pstm,rs,sql.toString(),params);
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(user);
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return userList;
    }

    public int addUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstm=null;
        int updateRow=0;
        if(connection!=null){
            String sql="insert into smbms_user"+
                    " (userCode, userName, userPassword, gender, birthday, phone, address, userRole, creationDate, createdBy)"+
                    " values (?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                    user.getGender(),user.getBirthday(),  user.getPhone(),user.getAddress(), user.getUserRole(),
                    user.getCreationDate(),user.getCreatedBy()};
            updateRow=BaseDao.execute(connection,pstm,sql,params);
            BaseDao.closeResource(null,pstm,null);
        }
        return updateRow;
    }

    public User viewUser(Connection connection,int id) throws SQLException {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        User user=null;

        if(connection!=null){
            String sql="select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r where u.userRole = r.id and u.id=?";
            Object[] params={id};

            rs=BaseDao.execute(connection,pstm,rs,sql,params);

            if(rs.next()){
                user = new User();
                user.setAddress(rs.getString("address"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            BaseDao.closeResource(null,pstm,rs);
        }
             return user;
    }

    public int delUser(Connection connection, int id) throws SQLException {
        PreparedStatement pstm=null;
        int updateRow=0;

        if(connection!=null){
            String sql="delete from smbms_user where id=?";
            Object[] params={id};

            updateRow=BaseDao.execute(connection,pstm,sql,params);

            BaseDao.closeResource(null,pstm,null);
        }
        return updateRow;
    }


}
