package io.donghun.blog.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // 유저 관련
    USER_CREATED("회원가입이 완료되었습니다."),
    USER_LOGIN("로그인이 완료되었습니다."),
    USER_READ("계정이 성공적으로 조회되었습니다."),

    POST_CREATED("게시글이 성공적으로 등록되었습니다."),
    POST_UPDATED("게시글이 성공적으로 수정되었습니다."),
    POST_DELETED("게시글이 성공적으로 삭제되었습니다.");

    private final String successMessage;
}
