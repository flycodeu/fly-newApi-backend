package com.fly.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flyCommon.model.entity.User;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.request.User.UserAddRequest;
import com.flyCommon.model.request.User.UserQueryRequest;
import com.flyCommon.model.request.User.UserUpdateRequest;
import com.flyCommon.model.vo.LoginEmailVo;
import com.flyCommon.model.vo.LoginPhoneVo;
import com.flyCommon.model.vo.UserAKSKVo;
import com.flyCommon.model.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-07-13 18:24:51
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    String userLogin(String userAccount, String userPassword);

    /**
     * 使用手机号登录
     *
     * @return
     */
    String userLoginByPhone(LoginPhoneVo loginPhoneVo);

    /**
     * 邮箱登录
     * @param loginEmailVo
     * @return
     */
    String userLoginByEmail(LoginEmailVo loginEmailVo);
    /**
     * 发送验证码
     *
     * @return
     */
    String sendCode(String phoneNum);

    /**
     * 获取登录用户
     *
     * @return
     */
    User getLoginUser();

    /**
     * 使用redis进行token
     *
     * @param token
     * @return
     */
    UserVO getLoginUserRedis(String token);

    /**
     * 退出
     *
     * @return
     */
    boolean logout(String token);

    /**
     * 添加用户，管理员权限
     *
     * @param userAddRequest
     * @return
     */
    Long addUser(UserAddRequest userAddRequest);

    /**
     * 删除用户
     *
     * @return
     */
    boolean deleteUser(DeleteRequest deleteRequest);

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest
     * @return
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 分页获取用户信息
     *
     * @param userQueryRequest
     * @return
     */
    Page<UserVO> listPageUsers(UserQueryRequest userQueryRequest);

    /**
     * 查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 脱敏列表
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取vo信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 查看ak,sk
     * @param userAKSKVo
     * @return
     */
    UserAKSKVo getUserAkSkByToken(UserAKSKVo userAKSKVo);

    /**
     * 更新ak,sk
     * @return
     */
    UserAKSKVo updateUserAkSk(UserAKSKVo userAKSKVo);

    List<Map<String, Object>> getUserRegisterOrderByMonth();
}
