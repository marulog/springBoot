package jpabook.jpashop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateMemberRequest {
    @NotEmpty // @Valid 제약조건 확인
    private String name;
}
