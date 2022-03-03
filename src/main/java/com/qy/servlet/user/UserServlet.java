package com.qy.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.qy.dao.user.UserDao;
import com.qy.pojo.Role;
import com.qy.pojo.User;
import com.qy.service.role.RoleServiceImpl;
import com.qy.service.user.UserService;
import com.qy.service.user.UserServiceImpl;
import com.qy.util.Constants;
import com.qy.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//实现Servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method=req.getParameter("method");
        if(method!=null&&method.equals("savepwd")){
            this.updatePwd(req,resp);
        }else if(method!=null&&method.equals("pwdmodify")){
            this.pwdModify(req,resp);
        }else if(method!=null&&method.equals("query")){
            this.query(req,resp);
        }else if(method!=null&&method.equals("add")){
            this.add(req,resp);
        }else if(method!=null&&method.equals("view")){
            this.view(req,resp);
        }else if(method!=null&&method.equals("deluser")){
            this.delete(req,resp);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id =Integer.parseInt(req.getParameter("uid"));
        UserServiceImpl userService=new UserServiceImpl();

        boolean flag= userService.delUser(id);
        if (flag)
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        else
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);

    }

    private void view(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int userId = Integer.parseInt(req.getParameter("uid"));

          UserServiceImpl userService = new UserServiceImpl();


        User user=userService.viewUser(userId);
        req.setAttribute("user",user);
        req.getRequestDispatcher("userview.jsp").forward(req, resp);

    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        UserServiceImpl userService = new UserServiceImpl();
        boolean flag=userService.addUser(user);
        if (flag)
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        else
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //修改密码
    public void updatePwd(HttpServletRequest req,HttpServletResponse resp){
        Object o= req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag=false;
        if(o!=null&& !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService=new UserServiceImpl();
            flag= userService.updatePwd(((User) o).getId(), newpassword);
            if(flag){
                req.setAttribute("message","修改密码成功，请退出，使用新密码登录！");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                req.setAttribute("message","密码修改失败");
            }
        }else{
            req.setAttribute("message","密码格式错误");
        }

        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证旧密码
    public void pwdModify(HttpServletRequest req,HttpServletResponse resp){
        Object o=req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword=req.getParameter("oldpassword");

        Map<String ,String> resultMap=new HashMap<String ,String >();

        if(o==null){//session失效
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        }else{
            String userPassword = ((User) o).getUserPassword();
            if(oldpassword.equals(userPassword)){
                resultMap.put("result","true");
            }else{
                resultMap.put("result","flase");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryUserName=req.getParameter("queryname");
        String temp=req.getParameter("queryUserRole");
        String pageIndex=req.getParameter("pageIndex");
        int queryUserRole=0;

        UserServiceImpl userService = new UserServiceImpl();
        RoleServiceImpl roleService = new RoleServiceImpl();
        int pageSize=5;
        int currentPageNo=1;

        if(queryUserName==null){
            queryUserName="";
        }
        if(!StringUtils.isNullOrEmpty(temp)){
            queryUserRole=Integer.parseInt(temp);
        }
        if(pageIndex!=null){
            currentPageNo=Integer.parseInt(pageIndex);
        }
        int totalCount=userService.getUserCount(queryUserName,queryUserRole);

        PageSupport pageSupport=new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount =pageSupport.getTotalPageCount();

        if(currentPageNo<1){
            currentPageNo=1;
        }else if(currentPageNo>totalPageCount){
            currentPageNo=totalPageCount;
        }

        List<User> userList=userService.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
        List<Role> roleList=roleService.getRoleList();

        req.setAttribute("userList",userList);
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        req.getRequestDispatcher("userlist.jsp").forward(req, resp);

    }

}
