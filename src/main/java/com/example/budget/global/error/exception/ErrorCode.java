package com.example.budget.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Budget
    BUDGET_ALREADY_EXISTS(400, "BDG001", "해당하는 예산 설정이 이미 존재합니다"),
    INVALID_REQUEST_BUDGET_FIELDS(400, "BDG002", "예산 설정 요청 필드가 유효하지 않습니다"),
    // Expenditure
    BUDGET_NOT_EXISTS(400, "EX001", "이번 달에 해당하는 예산이 존재하지 않습니다"),
    EXPENDITURE_NOT_FOUND(400, "EX002", "해당하는 지출 내역이 존재하지 않습니다"),
    START_DATE_IS_BEFORE_END_DATE(400, "EX003", "시작 날짜가 종료 날짜보다 빠릅니다"),
    MIN_IS_BIGGER_THAN_MAX(400, "EX004", "최솟값이 최댓값보다 큽니다"),
    // Client
    CLIENT_NOT_FOUND(400, "C001", "해당하는 고객이 존재하지 않습니다"),
    CLIENT_PASSWORD_NOT_MATCH(400, "C002", "비밀번호가 일치하지 않습니다"),
    // Trade
    ORDER_NOT_FOUND(400, "TOO1", "Order Not Found"),
    TRADE_SIGNAL_UNKNOWN(400, "T002", "Trade Signal Unknown"),
    // Auth
    NOT_EXISTS_AUTH(400, "ATH001", "권한 정보가 없습니다");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
