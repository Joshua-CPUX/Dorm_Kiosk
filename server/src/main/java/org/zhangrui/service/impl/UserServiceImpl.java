package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.config.JwtUtil;
import org.zhangrui.mapper.UserMapper;
import org.zhangrui.model.dto.UserLoginDTO;
import org.zhangrui.model.dto.UserRegisterDTO;
import org.zhangrui.model.dto.WxLoginDTO;
import org.zhangrui.model.entity.User;
import org.zhangrui.model.vo.UserVO;
import org.zhangrui.service.IActivationCodeService;
import org.zhangrui.service.IUserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final IActivationCodeService activationCodeService;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.appsecret}")
    private String appsecret;

    @Override
    public UserVO login(UserLoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        UserVO vo = convertToVO(user);
        vo.setToken(jwtUtil.generateToken(String.valueOf(user.getId())));
        return vo;
    }

    @Override
    public UserVO register(UserRegisterDTO dto) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setRole(0);
        user.setStatus(1);
        userMapper.insert(user);

        UserVO vo = convertToVO(user);
        vo.setToken(jwtUtil.generateToken(String.valueOf(user.getId())));
        return vo;
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public UserVO wxLogin(WxLoginDTO dto) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid, appsecret, dto.getCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> wxResponse = restTemplate.getForObject(url, Map.class);

        if (wxResponse == null || wxResponse.containsKey("errcode")) {
            String errMsg = wxResponse != null ? (String) wxResponse.get("errmsg") : "微信服务异常";
            throw new BusinessException("微信登录失败: " + errMsg);
        }

        String openid = (String) wxResponse.get("openid");
        if (!StringUtils.hasText(openid)) {
            throw new BusinessException("获取openid失败");
        }

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, openid));

        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : "微信用户");
            user.setAvatar(dto.getAvatarUrl());
            user.setRole(0);
            user.setStatus(1);
            userMapper.insert(user);
        } else {
            if (user.getStatus() != 1) {
                throw new BusinessException("账号已被禁用");
            }
            // 更新昵称和头像（如果提供了新的）
            if (StringUtils.hasText(dto.getNickname())) {
                user.setNickname(dto.getNickname());
                user.setAvatar(dto.getAvatarUrl());
                userMapper.updateById(user);
            }
        }

        UserVO vo = convertToVO(user);
        vo.setToken(jwtUtil.generateToken(String.valueOf(user.getId())));
        return vo;
    }

    @Override
    public void activateOwner(Long userId, String activationCode) {
        boolean valid = activationCodeService.validateAndUse(activationCode);
        if (!valid) {
            throw new BusinessException("激活码无效或已使用");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getRole() == 1) {
            throw new BusinessException("已经是店主了");
        }
        user.setRole(1);
        userMapper.updateById(user);
    }

    @Override
    public Page<UserVO> getUserList(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword).or().like(User::getNickname, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> userPage = userMapper.selectPage(page, wrapper);

        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }

    public void validateOwner(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getRole() != 1) {
            throw new BusinessException("无店主权限");
        }
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setPhone(user.getPhone());
        vo.setBalance(user.getBalance());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        return vo;
    }
}
