package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
// 서비스계층에서 리턴 타입
public class PageResultDTO<DTO, EN>{ //제네릭 타입, DTO와 Entity

    private List<DTO> dtoList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){ // Page<Entity>는 생성자, Function은 엔티티 객체들을 DTO로 변환 해주는 기능

        dtoList = result.stream().map(fn).collect(Collectors.toList());

    }
}
