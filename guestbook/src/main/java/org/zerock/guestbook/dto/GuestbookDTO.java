package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//DTO는 일회성으로 데이터를 주고받는 용도로 사용되는 객체 like 우편물, 상자
//엔티티와의 공통점. 순수하게 데이터를 담고 있다는 점
//엔티티와의 차이점. 목적자체가 데이터의 전달이므로 읽고 쓰는 것이 모두 허용. 일회성으로 사용
//엔티티 객채는 단순히 데이터를 담는 객체가 아니라 실제 데이터베스와 관련이 있고 내부적으로 엔티티 매니저가 관리 => DTO와 분리해서 처리

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestbookDTO {

    private long gno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate, modDate;
}
