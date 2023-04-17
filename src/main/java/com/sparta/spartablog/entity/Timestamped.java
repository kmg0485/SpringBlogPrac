package com.sparta.spartablog.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //JPA Entity 클래스들이 BaseTime class를 상속할 경우 class의 필드인 createddate, modifieddate를 인식하도록 함.
//EntityListeners: Entity에서 이벤트가 발생할 때마다 특정 로직을 실행시킬 수 있는 어노테이션
//AuditingEntityListener: 클래스가 callback listener로 지정되어 Entity에서 이벤트가 발생할때마다 특정 로직 수행.
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
