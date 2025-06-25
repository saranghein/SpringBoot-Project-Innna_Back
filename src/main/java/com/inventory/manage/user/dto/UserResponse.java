package com.inventory.manage.user.dto;



import com.inventory.manage.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class UserResponse {
    @Data
    @AllArgsConstructor
    @Schema(description = "닉네임 응답 dto")
    public static class MypageInfoDto {
        @Schema(description = "유저 닉네임")
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "토큰 응답 dto")
    public static class TokenDto {
        @Schema(description = "액세스 토큰")
        private String accessToken;
        @Schema(description = "리프레시 토큰")
        private String refreshToken;
    }

    @Data
    @Schema(description = "닉네임 응답 dto")
    public static class UserInfoDto {
        @Schema(description = "유저 닉네임")
        private String nickname;

        public UserInfoDto(User user) {
            this.nickname = user.getNickname();
        }
    }
    @Getter
    @AllArgsConstructor
    public static class UserCount {
        private Long userCount;
    }
}
