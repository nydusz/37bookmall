package sanqibookmall.service;

import sanqibookmall.controller.vo.SanqiUserVO;
import sanqibookmall.entity.MallUser;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;

import javax.servlet.http.HttpSession;

public interface SanqiUserService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getUsersPage(PageQueryUtil pageUtil);

    /**
     * 用户注册
     *
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password,String major);

    /**
     * 登录
     *
     * @param loginName
     * @param passwordMD5
     * @param httpSession
     * @return
     */
    String login(String loginName, String passwordMD5, HttpSession httpSession);

    /**
     * 用户信息修改并返回最新的用户信息
     *
     * @param mallUser
     * @return
     */
    SanqiUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession);

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     *
     * @param ids
     * @param lockStatus
     * @return
     */
    Boolean lockUsers(Integer[] ids, int lockStatus);


}
