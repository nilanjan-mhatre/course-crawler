package com.frontend.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long topicId;

    private String name;

    private String key;

    private CourseDTO course;

    private List<SubtopicDTO> subtopics;

    private String url;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        if(key == null) {
            key = name.replace(" ", "_");
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public List<SubtopicDTO> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<SubtopicDTO> subtopics) {
        this.subtopics = subtopics;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
