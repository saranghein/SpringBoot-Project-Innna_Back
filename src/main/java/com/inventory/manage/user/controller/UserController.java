package com.inventory.manage.user.controller;

import com.inventory.manage.common.util.CookieGenerator;
import com.inventory.manage.user.dto.UserRequest.*;
import com.inventory.manage.user.dto.UserRequest.ValidateNicknameDto;
import com.inventory.manage.user.dto.UserRequest.SignupDto;
import com.inventory.manage.user.dto.UserRequest.ValidateIdDto;
import com.inventory.manage.user.dto.UserResponse.*;
import com.inventory.manage.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserOperations {
    private final UserService userService;
    private final CookieGenerator cookieGenerator;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupDto) {
        try {
            userService.createUser(signupDto);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입에 실패했습니다");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다");
    }

    @Override
    @PostMapping("/validate-id")
    public ResponseEntity<?> validateId(@RequestBody ValidateIdDto validateIdDto) {
        try {
            if (userService.validateId(validateIdDto)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 아이디입니다");
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 아이디입니다");
    }

    @Override
    @PostMapping("/validate-nickname")
    public ResponseEntity<?> validateNickname(@RequestBody ValidateNicknameDto validateNicknameDto) {
        try {
            if (userService.validateNickname(validateNicknameDto)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 닉네임입니다");
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 닉네임입니다");
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            TokenDto tokens = userService.reissue(request.getCookies());

            response.setHeader("Authorization", "bearer " + tokens.getAccessToken());
            response.addCookie(cookieGenerator.generateCookie("refresh", tokens.getRefreshToken(), 24 * 60 * 60));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("토큰이 재발급되었습니다");
    }

    @Override
    @DeleteMapping("/exit")
    public ResponseEntity<?> exit(HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.deleteUser();
            response.addCookie(cookieGenerator.generateCookie("refresh", null, 0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("회원탈퇴가 완료되었습니다");
    }

    @Override
    @GetMapping("/mypage")
    public ResponseEntity<?> getMypageInfo() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getMypageInfo());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다");
        }
    }

    @Override
    @GetMapping("/mypage/info")
    public ResponseEntity<?> getMyInfo() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다");
        }
    }

    @Override
    @PutMapping("/mypage/info")
    public ResponseEntity<?> updateUserInfo(@RequestBody ChangeUserInfoDto updateUserInfoDto) {
        try {
            userService.updateUserInfo(updateUserInfoDto);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("정보가 변경되었습니다");
    }

    @Override
    @PostMapping("/mypage/password")
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordDto updatePasswordDto) {
        try {
            userService.updatePassword(updatePasswordDto);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 변경되었습니다");
    }

    @Override
    @GetMapping("/admin/user-count")
    public ResponseEntity<?> getTotalUserCount() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getTotalUserCount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
