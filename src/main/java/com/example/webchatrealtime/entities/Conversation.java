package com.example.webchatrealtime.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "conversation")
@Data
public class Conversation {
    @Id
    @Column(name = "conversationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;

    @Column(name = "title")
    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conversationId")
    private List<UserConversation> conversationList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conversationId")
    private List<Message> messageList;
}
