package sample.cafekiosk.spring.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    INIT("주문생성"),
    CANCELED("주문취소"),
    PAYMENT_COMPLETE("결재완료"),
    PAYMENT_FAILED("결재실패"),
    RECEIVED("주문완료"),
    COMPLETED("처리완료");

    private final String text;

}
