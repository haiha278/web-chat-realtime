package com.example.webchatrealtime.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachmentId")
    private Long attachmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageId", nullable = false)
    private Message message;

    @Column(name = "fileType", nullable = false, length = 50)
    private String fileType;

    @Column(name = "fileUrl", nullable = false)
    private String fileUrl;

    @Column(name = "fileSize", nullable = true)
    private Long fileSize;

    @Column(name = "uploadTime", nullable = false)
    private LocalDateTime uploadTime;
}
