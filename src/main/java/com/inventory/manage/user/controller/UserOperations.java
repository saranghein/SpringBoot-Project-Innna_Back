package com.inventory.manage.user.controller;

import org.springframework.http.ResponseEntity;

import com.inventory.manage.user.dto.UserRequest.ChangePasswordDto;
import com.inventory.manage.user.dto.UserRequest.ChangeUserInfoDto;
import com.inventory.manage.user.dto.UserRequest.SignupDto;
import com.inventory.manage.user.dto.UserRequest.ValidateIdDto;
import com.inventory.manage.user.dto.UserRequest.ValidateNicknameDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserOperations {
    ResponseEntity<?> signup(SignupDto signupDto);

    ResponseEntity<?> validateId(ValidateIdDto validateIdDto);

    ResponseEntity<?> validateNickname(ValidateNicknameDto validateNicknameDto);
    
    ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> exit(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> getMypageInfo();

    ResponseEntity<?> getMyInfo();

    ResponseEntity<?> updateUserInfo(ChangeUserInfoDto updateUserInfoDto);

    ResponseEntity<?> updatePassword(ChangePasswordDto updatePasswordDto);

    ResponseEntity<?> getTotalUserCount();
}