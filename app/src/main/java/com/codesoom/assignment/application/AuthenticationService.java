package com.codesoom.assignment.application;

import com.codesoom.assignment.controllers.ControllerErrorAdvice;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.AuthenticationFailException;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증을 담당합니다.
 */
@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;
    private UserRepository userRepository;

    @Autowired
    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 주어진 회원을 로그인 처리하고, 액세스 토큰을 리턴합니다.
     *
     * @param userLoginData 회원 로그인 정보
     * @return 생성된 액세스 토큰
     * @throws AuthenticationFailException 주어진 회원 로그인 정보가 유효하지 않을 경우
     */
    @Transactional
    public String login(UserLoginData userLoginData) {
        User user = findUserByEmail(userLoginData.getEmail());

        boolean wrongPassword = false;
        if (user.authenticate(userLoginData.getPassword()) == wrongPassword) {
            throw new AuthenticationFailException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.encode(user.getId());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationFailException("입력하신 이메일에 해당하는 회원이 존재하지 않습니다."));
    }

    /**
     * 주어진 액세스 토큰을 파싱한 뒤 파싱된 값을 리턴합니다.
     *
     * @param accessToken 액세스 토큰
     * @return 파싱된 값
     */
    public Long parseToken(String accessToken) {
        return jwtUtil.decode(accessToken)
                .get("userId", Long.class);
    }
}
