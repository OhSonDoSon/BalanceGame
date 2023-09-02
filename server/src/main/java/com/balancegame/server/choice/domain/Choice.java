package com.balancegame.server.choice.domain;

import com.balancegame.server.choice.enums.ChoiceValue;
import com.balancegame.server.common.domain.BaseDateEntity;
import com.balancegame.server.member.domain.Member;
import com.balancegame.server.vote.domain.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "choice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Choice extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChoiceValue choiceValue;

}
