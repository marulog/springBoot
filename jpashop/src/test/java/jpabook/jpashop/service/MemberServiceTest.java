package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class) // test 실행 할 때  스프링과 연동하여 실행
@SpringBootTest // 스프링부트를 띄운 상태로 테스트를 돌리고 싶다
@Transactional // test환경에서는 테스트 끝나면 롤백 처리
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberRepositoryOld memberRepository;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("park");


        //when
        Long id = memberService.join(member);


        //then
        Assertions.assertEquals(member, memberRepository.find(id));

    }

    @Test
    public void 종복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("park1");

        Member member2 = new Member();
        member2.setName("park1");

        //when
        memberService.join(member1);


        //then
       Assertions.assertThrows(IllegalStateException.class,
               ()-> memberService.join(member2));
    }
}