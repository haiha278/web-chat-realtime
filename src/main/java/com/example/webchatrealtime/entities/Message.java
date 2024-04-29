package com.example.webchatrealtime.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "message")
public class Message {
    @Id
    @Column(name = "messageId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversationId")
    private Conversation conversationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "message")
    private List<Attachment> attachmentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "message")
    private List<ReadStatus> readStatusList;
}
