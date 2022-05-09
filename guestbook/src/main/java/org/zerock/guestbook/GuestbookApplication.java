package org.zerock.guestbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA를 이용하면서 AuditingEntityListener(JPA내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할)를 활성화시키기 위해서 추가
public class GuestbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuestbookApplication.class, args);
    }

}
