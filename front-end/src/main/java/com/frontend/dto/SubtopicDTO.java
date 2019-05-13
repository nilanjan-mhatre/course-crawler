package com.frontend.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubtopicDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long subtopicId;

    private String name;

    private String content;

    private TopicDTO topic;

    public SubtopicDTO() {
        super();
    }

    public SubtopicDTO(String name, String content) {
        super();
        this.name = name;
        this.content = content;
    }

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

    public TopicDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicDTO topic) {
        this.topic = topic;
    }
}
