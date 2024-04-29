package com.example.webchatrealtime.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_conversation")
public class UserConversation {
    @Id
    @Column(name = "userConversationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userConversationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversationId")
    private Conversation conversationId;

    @Column(name = "timeJoined")
    private LocalDateTime timeJoined;

    @Column(name = "role")
    private String role;
}
