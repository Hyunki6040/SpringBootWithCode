package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
// 서비스계층에서 리턴 타입
public class PageResultDTO<DTO, EN>{ //제네릭 타입, DTO와 Entity

    //DTO 리스트
    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    //현재 페이지 번호
    private int page;
    //목록 사이즈
    private int size;

    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    //이전, 다음
    private boolean prev, next;

    //페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){ // Page<Entity>는 생성자, Function은 엔티티 객체들을 DTO로 변환 해주는 기능

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());

    }

    private void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber() + 1; //0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize(); //총 페이지 수

        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10; //끝 페이지수 구하기. (페이지수/10)후 소수점 올림

        start = tempEnd - 9;

        prev = start > 1; //이전버튼 여부

        end = totalPage > tempEnd ? tempEnd: totalPage; //총 페이지수와 끝 페이지수 비교.

        next = totalPage > tempEnd; //다음버튼 여부

        System.out.println("page : " + page+ "\ntempEnd : " + tempEnd + "\nstart : " + start + "\n prev : " + prev + "\n end : " + end + "\n next : " + next);

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
