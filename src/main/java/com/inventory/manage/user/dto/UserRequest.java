package com.inventory.manage.user.dto;

import com.inventory.manage.user.entity.User;
import com.inventory.manage.user.util.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {
    @Getter
    @NoArgsConstructor
    public static class SignupDto {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 6, max = 12, message = "아이디는 최소 6글자, 최대 12글자입니다.")
        private String userId;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자, 최대 16글자입니다.")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 8, message = "닉네임은 최소 2글자, 최대 8글자입니다.")
        private String nickname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .userId(this.userId)
                    .password(passwordEncoder.encode(this.password))
                    .nickname(this.nickname)
                    .role(Role.ROLE_MEMBER).build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ChangePasswordDto {
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자, 최대 16글자입니다.")
        private String password;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자, 최대 16글자입니다.")
        private String newPassword;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangeUserInfoDto {
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 8, message = "닉네임은 최소 2글자, 최대 8글자입니다.")
        private String nickname;
    }

    @Data
    @NoArgsConstructor
    public static class ValidateIdDto {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 6, max = 12, message = "아이디는 최소 6글자, 최대 12글자입니다.")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    public static class ValidateNicknameDto {
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 8, message = "닉네임은 최소 2글자, 최대 8글자입니다.")
        private String nickname;
    }
}
