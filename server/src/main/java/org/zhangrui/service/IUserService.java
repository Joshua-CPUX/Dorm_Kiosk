package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhangrui.model.dto.UserLoginDTO;
import org.zhangrui.model.dto.UserRegisterDTO;
import org.zhangrui.model.dto.WxLoginDTO;
import org.zhangrui.model.vo.UserVO;

public interface IUserService {

    UserVO login(UserLoginDTO dto);

    UserVO register(UserRegisterDTO dto);

    UserVO getUserInfo(Long userId);

    UserVO wxLogin(WxLoginDTO dto);

    void activateOwner(Long userId, String activationCode);

    Page<UserVO> getUserList(Integer pageNum, Integer pageSize, String keyword);

    void updateUserStatus(Long id, Integer status);
}
