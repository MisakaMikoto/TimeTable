package com.misakamikoto.login.mapper;

import com.misakamikoto.login.model.LoginVO;
import org.springframework.stereotype.Repository;

/**
 * The interface Login mapper.
 */
@Repository
public interface LoginMapper {
    /**
     * Find user login vo.
     *
     * @param id the id
     * @return the login vo
     */
    LoginVO findUser(String id);

    /**
     * Create user.
     *
     * @param loginVO the login vo
     */
    void createUser(LoginVO loginVO);
}
