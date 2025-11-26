package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //EntityManager JPA의 핵심 객체로
    // 엔티티를 DB에 저장, 수정, 삭제, 조회하는 모든 일을 담당함.
    // persist, find, remove, create
    //애는 @Autowride 말고 @persistenceContext 사용해야됨
//    @PersistenceContext // 엔티티 매니저를 주입시켜줌?
//    private EntityManager em;
    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
      return  em.createQuery("select m from Member m where m.name = :name", Member.class)
              .setParameter("name", name)
              .getResultList();
    }
}
