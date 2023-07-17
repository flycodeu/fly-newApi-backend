package com.fly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.common.ErrorCode;
import com.fly.constant.CommonConstant;
import com.fly.constant.LengthConstant;
import com.fly.constant.UserConstant;
import com.fly.exception.BusinessException;
import com.fly.model.entity.User;
import com.fly.model.enums.UserRoleEnum;
import com.fly.model.request.DeleteRequest;
import com.fly.model.request.User.UserAddRequest;
import com.fly.model.request.User.UserQueryRequest;
import com.fly.model.request.User.UserUpdateRequest;
import com.fly.model.vo.LoginPhoneVo;
import com.fly.model.vo.UserVO;
import com.fly.service.UserService;
import com.fly.mapper.UserMapper;
import com.fly.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author admin
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-07-13 18:24:51
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static final String SALT = "fly";
    public static final String USERNAME = "user_" + RandomUtil.randomString(4);
    public static final String PHONEUSERNAME = "phone_" + RandomUtil.randomString(4);
    public static final String USERAVATAR = "https://picsum.photos/200/300";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        checkAccountAndPasswordAndCheckPassword(userAccount, userPassword, checkPassword);
        synchronized (userAccount.intern()) {
            //1. 判断是否重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            Long count = userMapper.selectCount(queryWrapper);
            if (count != 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "您的账号已经被注册了");
            }
            //2. 密码加密
            String entryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //3.添加用户
            User user = new User();
            String accessKey = SALT + userAccount + RandomUtil.randomString(5);
            String secretKey = SALT + userAccount + RandomUtil.randomString(5);

            user.setUserName(USERNAME);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            user.setUserPassword(userPassword);
            user.setUserAccount(userAccount);
            user.setUserAvatar(USERAVATAR);
            user.setUserPassword(entryPassword);

            boolean saveUser = this.save(user);
            if (!saveUser) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册错误");
            }
            return user.getId();
        }
    }

    // 3. 生成随机token
    public static final String token = UUID.randomUUID().toString();
    public static final String tokenKey = RedisConstants.LOGIN_TOKEN + token;

    @Override
    public String userLogin(String userAccount, String userPassword) {
        checkAccountAndPassword(userAccount, userPassword);
        String entryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", entryPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不存在此用户");
        }
        if (user.getUserRole().equals(UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您已被系统拉黑，请稍后再试");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        //request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, userVO);

        // 使用token
        Map<String, Object> userMap = BeanUtil.beanToMap(userVO);

        // 4. 对象保存到Hash存储
        redisTemplate.opsForHash().putAll(tokenKey, userMap);
        redisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TIME, TimeUnit.MINUTES);

        return token;
    }

    @Override
    public String userLoginByPhone(LoginPhoneVo loginPhoneVo) {
        String phoneNum = loginPhoneVo.getPhoneNum();
        if (RegexUtils.isPhoneInvalid(phoneNum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入的手机号不正确");
        }
        String code = loginPhoneVo.getCode();
        // 1. redis获取验证码
        String key = RedisConstants.LOGIN_CODE_KEY + phoneNum;
        String saveCode = (String) redisTemplate.opsForValue().get(key);
        // 2. 比较验证码是否相同
        if (!Objects.equals(saveCode, code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入的验证码不正确");
        }
        UserVO userVO = basicLoginByPhone(phoneNum);
        // 转换成map
        Map<String, Object> map = BeanUtil.beanToMap(userVO);

        // 4. 对象保存到Hash存储
        redisTemplate.opsForHash().putAll(tokenKey, map);
        redisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TIME, TimeUnit.MINUTES);
        // 5. 相同就返回token
        return token;
    }

    /**
     * 登录注册二合一
     *
     * @param phoneNum
     * @return
     */
    public UserVO basicLoginByPhone(String phoneNum) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phoneNum", phoneNum);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            // 用户为空就注册
            synchronized (phoneNum.intern()) {
                User newUser = new User();
                String accessKey = SALT + phoneNum + RandomUtil.randomString(5);
                String secretKey = SALT + phoneNum + RandomUtil.randomString(5);
                newUser.setUserName(PHONEUSERNAME);
                newUser.setUserAvatar(USERAVATAR);
                newUser.setAccessKey(accessKey);
                newUser.setUserAccount(PHONEUSERNAME);
                newUser.setSecretKey(secretKey);
                newUser.setPhoneNum(phoneNum);
                newUser.setUserPassword(RandomUtil.randomString(6));

                boolean save = this.save(newUser);
                if (!save) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册错误");
                }
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(newUser, userVO);
                return userVO;
            }
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public String sendCode(String phoneNum) {
        if (phoneNum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入手机号");
        }
        if (RegexUtils.isPhoneInvalid(phoneNum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号不合法");
        }
        String code = RandomUtil.randomString(4);
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phoneNum, code, RedisConstants.LOGIN_CODE_TIME, TimeUnit.MINUTES);
        return code;
    }

    @Override
    public User getLoginUser() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(tokenKey);
        UserVO userVO = BeanUtil.fillBeanWithMap(entries, new UserVO(), false);
        User user = new User();
        BeanUtils.copyProperties(userVO, user);

        return user;
    }

    @Override
    public UserVO getLoginUserRedis(String token) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(RedisConstants.LOGIN_TOKEN + token);
        return BeanUtil.fillBeanWithMap(entries, new UserVO(), false);
    }

    @Override
    public boolean logout() {
        Long delete = redisTemplate.opsForHash().delete(tokenKey);
        return delete == 1;
    }

    @Override
    public Long addUser(UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请填写完整参数");
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加用户失败");
        }
        return user.getId();
    }

    @Override
    public boolean deleteUser(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        Long id = deleteRequest.getId();
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除对象为空");
        }
        boolean b = this.removeById(user);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest) {
        String phonePattern = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$";
        String emailPattern = "^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$";
        String phoneNum = userUpdateRequest.getPhoneNum();
        String email = userUpdateRequest.getEmail();
        String userName = userUpdateRequest.getUserName();

        if (phoneNum != null) {
            boolean matches = phoneNum.matches(phonePattern);
            if (!matches) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
            }
        }
        if (email != null) {
            boolean matches = email.matches(emailPattern);
            if (!matches) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
            }
        }

        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);

        return this.updateById(user);
    }

    @Override
    public Page<UserVO> listPageUsers(UserQueryRequest userQueryRequest) {
        long current = 1;
        long size = 10;
        User queryUser = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, queryUser);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.FORBIDDEN_ERROR, "禁止爬虫");

        //模糊查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(queryUser);

        // 判断查询条件是否为空
        if (userQueryRequest != null && (StringUtils.isNotBlank(userQueryRequest.getUserAccount())
                || StringUtils.isNotBlank(userQueryRequest.getUserName()))) {
            // 设置模糊查询条件
            queryWrapper.and(wrapper -> wrapper
                    .like(StringUtils.isNotBlank(userQueryRequest.getUserAccount()), "userAccount", userQueryRequest.getUserAccount())
                    .like(StringUtils.isNotBlank(userQueryRequest.getUserName()), "userName", userQueryRequest.getUserName())
            );
        }

        Page<User> userPage = this.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> voPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVoList = userPage.getRecords().stream().map(user -> {
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());
        voPage.setRecords(userVoList);

        return voPage;
    }


    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userRole = userQueryRequest.getUserRole();
        String userAccount = userQueryRequest.getUserAccount();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 检查用户账号，密码，检验密码，抽象方法
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     */
    public void checkAccountAndPasswordAndCheckPassword(String userAccount, String userPassword, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }
        if (userPassword.length() < LengthConstant.USERPASSEORDLENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, String.format("密码长度不能小于%d位数", LengthConstant.USERPASSEORDLENGTH));
        }
        if (userAccount.length() < LengthConstant.USERACCOUNTLENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, String.format("用户账号长度不能小于%d位", LengthConstant.USERACCOUNTLENGTH));
        }
    }

    /**
     * 检验账号密码
     *
     * @param userAccount
     * @param userPassword
     */
    public void checkAccountAndPassword(String userAccount, String userPassword) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        if (userPassword.length() < LengthConstant.USERPASSEORDLENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, String.format("密码长度不能小于%d位数", LengthConstant.USERPASSEORDLENGTH));
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, String.format("用户账号长度不能小于%d位", LengthConstant.USERACCOUNTLENGTH));
        }
    }

}




