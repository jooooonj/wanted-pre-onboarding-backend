package com.wanted.boardAPI.domain.member.repository;

import com.wanted.boardAPI.domain.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    protected MemberRepository memberRepository;

    @Test
    @DisplayName("findByEmail")
    void findByEmail() throws Exception {
        //given
        Member member = Member.builder()
                .email("abcd@1234")
                .password("1234")
                .build();

        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByEmail("abcd@1234").orElse(null);

        //then
        Assertions.assertThat(findMember).isNotNull();
        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());

    }
}