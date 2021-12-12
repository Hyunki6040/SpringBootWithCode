package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity //이 클래스는 JPA로 관리되는 엔티티 객체
@Table(name= "tbl_memo") //DB상에서 테이블을 설정한다. 인덱스 등 생성도 가능. 속성이 없으면 클래스 명으로 테이블 생성
@ToString
@Getter //Lombok Getter Method 생성
@Builder //객체를 생성 가능
@AllArgsConstructor //컴파일 에러 처리
@NoArgsConstructor //컴파일 에러 처리
public class Memo {

    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK를 자동으로 생성. (Oracle- 번호 table, Mysql - A.I)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;


}
