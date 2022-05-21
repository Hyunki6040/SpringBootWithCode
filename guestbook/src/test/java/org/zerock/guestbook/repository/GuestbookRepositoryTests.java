package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    //JPA insert 테스트
    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content..." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    //JPA update 테스트
    @Test
    public void updateTest(){

        Optional<Guestbook> result = guestbookRepository.findById(300L); // 존재하는 번호로 테스트

        if(result.isPresent()){

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Change Title....");
            guestbook.changeContent("Change Content...");

            guestbookRepository.save(guestbook);
        }
    }

    //Querydsl 단일 항목 검색 테스트
    @Test
    public void testQuery1(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // 동적처리를 위한 Q도메인 클래스. Entity 클래스에 선언된 title, content같은 필드들을 변수로 활용
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        // where문에 들어가는 조건들을 넣어주는 컨테이너 역할
        BooleanBuilder builder = new BooleanBuilder();

        // 원하는 조건은 필드 값과 같이 결합해서 생성
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // 만들어진 조건을 and나 or같은 키워드와 결합
        builder.and(expression);

        // BooleanBuilder는 GuestBookRepository에 추가된 QuerydslPredicateExcutor 인터페이스 findAll을 사용할 수 있음
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    //Querydsl 다중항목 검색 테스트
    @Test
    public void testQuery2() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        // 2개의 BooleanExpression을 결합
        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        // gno가 0보다 크다
        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

}
