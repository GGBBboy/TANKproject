package com.qy.service.role;

import com.qy.dao.BaseDao;
import com.qy.dao.role.RoleDao;
import com.qy.dao.role.RoleDaoImpl;
import com.qy.pojo.Role;
import org.junit.Test;

import javax.crypto.interfaces.PBEKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;
    public RoleServiceImpl(){roleDao=new RoleDaoImpl();}

    public List<Role> getRoleList() {
        Connection connection=null;
        List<Role> roleList = new ArrayList<Role>();

        try {
            connection=BaseDao.getConnection();
            roleList=roleDao.getRoleList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return roleList;
    }


    public void test(){

        List<Role> roleList=getRoleList();
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }
    }
}
