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

    @Test
    public void updateTest(){

        //존재하는 번호로 테스트
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if(result.isPresent()){

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");

            guestbookRepository.save(guestbook);
        }

    }

    //단일항목 검색
    @Test
    public void testQuery1(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook; //Q도메인 클래스를 얻어옴 => 동적인 처리, 필드들을 변수로 활용 가능

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //where문에 들어가는 조건들을 넣어주는 컨테이너

        BooleanExpression expression = qGuestbook.title.contains(keyword); //조건을 필드값과 결합하여 생성, import 주의

        builder.and(expression); //where문에 and나 or같은 키워드와 결합

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //조회 처리(페이징 처리도)

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

    // 다중 항목 검색
    // title 혹은 content에 특정한 keyword가 있고 gno가 0보다 크다.
    @Test
    public void testQuery2(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent); // 2개의 BooleanExpression이 결합하는 부분

        builder.and(exAll);

        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

}
