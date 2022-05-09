package org.zerock.guestbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity //이 클래스는 JPA로 관리되는 엔티티 객체
@Getter //Lombok Getter Method 생성
@Builder //객체를 생성 가능
@AllArgsConstructor //컴파일 에러 처리
@NoArgsConstructor //컴파일 에러 처리
@ToString
public class Guestbook extends BaseEntity{

    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK를 자동으로 생성. (Oracle- 번호 table, Mysql - A.I)
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

}
