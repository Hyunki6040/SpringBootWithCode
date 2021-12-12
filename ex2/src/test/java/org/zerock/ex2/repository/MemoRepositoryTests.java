package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        //의존성 주입 확인. .getClass():실제 객체 호출. => 스프링 내부에도 자동으로 생성(AOP)되는 클래스:동적 프록시 방식
        System.out.println(memoRepository.getClass().getName());
    }

    //Insert
    @Test
    public void testInsertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    //여러건 조회
    @Test
    public void testSelect(){

        //데이터베이스에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("==============================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    //단일건 조회
    @Transactional //Transaction 처리를 위한 Annotation
    @Test
    public void testSelect2(){

        //데이터베이스에 존재하는 mno
        Long mno = 100L;

        //실제 객체를 필요한 순간까지 SQL을 실행하지는 않음
        Memo memo = memoRepository.getOne(mno);

        System.out.println("===========================");

        System.out.println(memo);
    }

    //update
    @Test
    public void testUpdate() {
        //id 일치하는 지 확인 후 insert 혹은 update 처리
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        //select -> update(or insert). JPA는 엔티티 객체들을 메모리상에서 보관하려고 하기 때문
        System.out.println(memoRepository.save(memo));

    }

    //delete
    @Test
    public void testDelete() {

        Long mno = 100L;

        //select -> delete. return void. Exception 처리
        memoRepository.deleteById(mno);
    }

    //paging 처리
    @Test
    public void testPageDefault() {

        //1페이지 10개
        //내가 실수한 부분. Pageable import 주의. java.awt.print.Pageable를 가져왔다.
        Pageable pageable = PageRequest.of(0,10);

        //limit 조회 -> 전체 갯수 count(데이터가 충분하다면)
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("-----------------------------------------");

        System.out.println("Total Pages : "+result.getTotalPages()); //총 몇페이지
        System.out.println("Total Count: "+result.getTotalElements()); //전체 개수
        System.out.println("Page Number: "+result.getNumber()); //현재 페이지 번호 0부터 시작
        System.out.println("Page Size: "+result.getSize()); //페이지당 데이터 개수
        System.out.println("has next page?:"+result.hasNext()); //다음 페이지
        System.out.println("first page?:"+result.isFirst()); //시작 페이지(0) 여부

        System.out.println("-----------------------------------------");

        for(Memo memo : result.getContent()){
            System.out.println(memo);
        }

    }

    @Test
    public void testSort(){

        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); //and를 이용한 연결

        //역순 정렬. desc
        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethod(){

        List<Memo> list = memoRepository.
                findByMnoBetweenOrderByMnoDesc(70L,80L);

        for( Memo memo : list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable(){

        Pageable pageable = PageRequest.of(1, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods(){

        memoRepository.deleteMemoByMnoLessThan(10L);

    }

    @Test
    public void testQueryAnnotation(){

        List<Memo> list = memoRepository.getListDesc();

        for( Memo memo : list){
            System.out.println(memo);
        }
    }

    @Test
    public void  testUpdateQueryAnnotation(){

        int result = memoRepository.updateMemoText(100L, "Update Text");

        if(result == 1){
            System.out.println("Updated");
        }
    }

    @Test
    public void testUpdateQueryAnnotationByObject(){

        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        int result = memoRepository.updateMemoTextObject(memo);

        if(result == 1){
            System.out.println("Updated");
        }
    }

    @Test
    public void testQueryAnnotationWithPageable(){

        Pageable pageable = PageRequest.of(1, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.getListWithQuery(10L, pageable);

        result.get().forEach(memo -> System.out.println(memo));

    }

    @Test
    public void testQueryAnnotationWithePageableObject(){

        Pageable pageable = PageRequest.of(1, 10, Sort.by("mno").descending());

        Page<Object[]> result = memoRepository.getListWithQueryObject(10L, pageable);

        result.get().forEach(memo -> System.out.println(memo));

    }
}
