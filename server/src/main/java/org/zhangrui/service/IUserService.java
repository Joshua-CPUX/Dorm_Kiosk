package org.zhangrui.service;

import org.zhangrui.model.dto.UserLoginDTO;
import org.zhangrui.model.dto.UserRegisterDTO;
import org.zhangrui.model.vo.UserVO;

public interface IUserService {

    UserVO login(UserLoginDTO dto);

    UserVO register(UserRegisterDTO dto);

    UserVO getUserInfo(Long userId);
}
