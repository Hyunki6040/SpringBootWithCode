package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//Table 생성 X, BaseEntity를 상속했을 때 table 생성
@MappedSuperclass
//JPA 내부의 Persistence Context에서 Entity 객체가 생성/변경되는 것을 감지하는 역할 => regDate, modDate에 적절한 값이 지정
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
abstract class BaseEntity {

    //JPA에서 엔티티의 생성 시간 처리
    @CreatedDate
    //upadatealbe => 해당 엔티티 객체를 데이터베이스에 반영할 때 regdate 칼럼값은 변경X
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    //최종 수정 시간을 자동으로 처리
    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;

}
