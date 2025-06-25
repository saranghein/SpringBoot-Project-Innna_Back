package com.inventory.manage.user.dto;



import com.inventory.manage.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class UserResponse {
    @Data
    @AllArgsConstructor
    public static class MypageInfoDto {
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    public static class UserInfoDto {
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
