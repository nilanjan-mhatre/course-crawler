package com.frontend.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="subtopic_info")
public class Subtopic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="sub_topic_id")
    private Long subtopicId;

    @Column
    private String name;

    @Column
    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name="topic_id_fk")
    private Topic topic;

    ////////////////////////////////
    public Long getSubtopicId() {
        return subtopicId;
    }

    public void setSubtopicId(Long subtopicId) {
        this.subtopicId = subtopicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
