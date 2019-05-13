package com.frontend.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long courseTitleId;

    private String title;

    private String source;

    private Boolean processed;

    private List<TopicDTO> topics;

    public CourseDTO() {
        super();
    }

    public CourseDTO(String title, String source) {
        super();
        this.title = title;
        this.source = source;
    }

    public Long getCourseTitleId() {
        return courseTitleId;
    }

    public void setCourseTitleId(Long courseTitleId) {
        this.courseTitleId = courseTitleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public List<TopicDTO> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicDTO> topics) {
        this.topics = topics;
    }
}
