package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    //EntityManager JPA의 핵심 객체로
    // 엔티티를 DB에 저장, 수정, 삭제, 조회하는 모든 일을 담당함.
    // persist, find, remove, create
    @PersistenceContext // 엔티티 매니저를 주입시켜줌?
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
