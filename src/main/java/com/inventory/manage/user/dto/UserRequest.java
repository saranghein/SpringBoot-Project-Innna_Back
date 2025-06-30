package com.inventory.manage.user.dto;

import com.inventory.manage.user.entity.User;
import com.inventory.manage.user.util.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
public class UserRequest {
    @Getter
    @NoArgsConstructor
    @Schema(description = "회원가입 요청 dto")
    public static class SignupDto {
        @Schema(description = "유저 아이디")
        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 6, max = 12, message = "아이디는 최소 6글자, 최대 12글자입니다.")
        private String userId;

        @Schema(description = "유저 비밀번호")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자, 최대 16글자입니다.")
        private String password;

        @Schema(description = "유저 닉네임")
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

    @Schema(description = "비밀번호 변경 dto")
    @Getter
    @NoArgsConstructor
    public static class ChangePasswordDto {
        @Schema(description = "이전 비밀 번호")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자, 최대 16글자입니다.")
        private String password;

        @Schema(description = "변경할 비밀번호")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자, 최대 16글자입니다.")
        private String newPassword;
    }

    @Schema(description = "닉네임 변경 dto")
    @Getter
    @NoArgsConstructor
    public static class ChangeUserInfoDto {
        @Schema(description = "변경할 닉네임")
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 8, message = "닉네임은 최소 2글자, 최대 8글자입니다.")
        private String nickname;
    }

    @Schema(description = "아이디 유효성 검사 dto")
    @Data
    @NoArgsConstructor
    public static class ValidateIdDto {
        @Schema(description = "유저 아이디")
        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 6, max = 12, message = "아이디는 최소 6글자, 최대 12글자입니다.")
        private String userId;
    }

    @Schema(description = "닉네임 유효성 검사 dto")
    @Data
    @NoArgsConstructor
    public static class ValidateNicknameDto {
        @Schema(description = "유저 닉네임")
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 8, message = "닉네임은 최소 2글자, 최대 8글자입니다.")
        private String nickname;
    }
}
