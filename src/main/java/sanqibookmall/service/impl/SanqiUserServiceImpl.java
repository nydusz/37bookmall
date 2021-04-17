package sanqibookmall.service.impl;

import sanqibookmall.common.Constants;
import sanqibookmall.common.ServiceResultEnum;
import sanqibookmall.controller.vo.SanqiUserVO;
import sanqibookmall.dao.MallUserMapper;
import sanqibookmall.entity.MallUser;
import sanqibookmall.service.SanqiUserService;
import sanqibookmall.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class SanqiUserServiceImpl implements SanqiUserService {

    @Autowired
    private MallUserMapper mallUserMapper;

    @Override
    public PageResult getUsersPage(PageQueryUtil pageUtil) {
        List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String register(String loginName, String password, String major) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        registerUser.setMajor(major);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            SanqiUserVO sanqiUserVO = new SanqiUserVO();
            BeanUtil.copyProperties(user, sanqiUserVO);
            //设置购物车中的数量n
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, sanqiUserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public SanqiUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession) {
        SanqiUserVO userTemp = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallUser userFromDB = mallUserMapper.selectByPrimaryKey(userTemp.getUserId());
        if (userFromDB != null) {
            userFromDB.setNickName(_Utils.cleanString(mallUser.getNickName()));
            userFromDB.setAddress(_Utils.cleanString(mallUser.getAddress()));
            userFromDB.setIntroduceSign(_Utils.cleanString(mallUser.getIntroduceSign()));
            userFromDB.setMajor(_Utils.cleanString(mallUser.getMajor()));
            if (mallUserMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
                SanqiUserVO sanqiUserVO = new SanqiUserVO();
                userFromDB = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                BeanUtil.copyProperties(userFromDB, sanqiUserVO);
                httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, sanqiUserVO);
                return sanqiUserVO;
            }
        }
        return null;
    }

    @Override
    public Boolean lockUsers(Integer[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}
