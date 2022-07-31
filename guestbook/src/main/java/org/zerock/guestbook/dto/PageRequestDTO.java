package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
// 서비스계층에서 파라미터
public class PageRequestDTO {

    private int page;
    private int size;
    private String type;
    private String keyword;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){ // 정렬은 나중에 다양한 상황에서 쓰기 위해서

        return PageRequest.of(page - 1, size, sort); // JPA에서 페이지 번호가 0부터 시작, 1페이지 일 경우 0

    }
}
