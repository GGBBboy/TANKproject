package com.qy.dao.role;

import com.qy.dao.BaseDao;
import com.qy.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        List<Role> roleList = new ArrayList<Role>();

        if(connection!=null){
            String sql="select * from smbms_role";
            Object[] params = {};
            rs= BaseDao.execute(connection,pstm,rs,sql,params);
            while (rs.next()){
                Role role=new Role();
                role.setRoleName(rs.getString("roleName"));
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                roleList.add(role);
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return roleList;
    }
}
