package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.*;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
        // @RestController에 의해서 뷰 이름이 아니라 HTTP 응답 바디로 넣어짐
        // 해당 객첸느 JSON으로 직렬화됨
        //@RequestBody -> 요청의 JSON -> Java 객체로 변환
    }

    @PutMapping("api/v2/members/{id}")
    public UpdateResponseResponse updateMemberV2
            (@PathVariable Long id,
             @RequestBody @Valid UpdateMemberRequest request){
//        Member findMember = memberService.findOne(id); -> 트랜잭션 종료
//        findMember.setName(request.getName()); -> 준영속 상태라서 변경감지 x
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateResponseResponse(id, findMember.getName());
    }

    @GetMapping("api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    @GetMapping("api/v2/members")
    public Result membersV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
}
