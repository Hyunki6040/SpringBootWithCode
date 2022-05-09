package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //테이블로 생성되지 않음
@EntityListeners(value = { AuditingEntityListener.class }) //JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할
@Getter
abstract class BaseEntity { //추상 클래스

    @CreatedDate //JPA에서 엔티티의 생성 시간을 처리
    @Column(name = "regdate", updatable = false) // 해당 엔티티 객체를 데이터베이스에 반영할 때 변경 X
    private LocalDateTime regDate;

    @LastModifiedDate //최종 수정 시간을 자동으로 처리
    @Column(name ="moddate")
    private LocalDateTime modDate;

}
