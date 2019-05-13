package com.frontend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frontend.dto.CourseDTO;
import com.frontend.dto.SubtopicDTO;
import com.frontend.dto.TopicDTO;
import com.frontend.entities.Course;
import com.frontend.repository.CourseRepository;
import com.frontend.utils.ObjectMapperUtils;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    ObjectMapperUtils objectMapper = new ObjectMapperUtils();

    public Set<String> findCourses() {
        List<Course> courses = courseRepository.findAll();
        Set<String> names = new HashSet<>();
        courses.forEach(c -> names.add(c.getTitle()));
        return names;
    }

    public CourseDTO findCoursesByName(String name) {
        List<Course> courses = courseRepository.findCoursesByName(name);
        CourseDTO finalCourse = refineTopics(courses);

        return finalCourse;
    }

    public CourseDTO refineTopics(List<Course> courses) {
        List<TopicDTO> topics = new ArrayList<>();
        List<TopicDTO> refinedTopics = new LinkedList<>();
        CourseDTO finalCourse = null;
        for(Course c: courses) {
            CourseDTO courseDTO = objectMapper.map(c, CourseDTO.class);
            if(finalCourse == null ) {
                finalCourse = courseDTO;
            }
            topics.addAll(courseDTO.getTopics());
        }
        int size = topics.size();
        boolean[] visited = new boolean[size];
        Arrays.fill(visited, false);

        List<SubtopicDTO> subtopics = null;
        TopicDTO topicDTO = null;
        int i, j;
        for(i=0; i<size; i++) {
            if(!visited[i]) {
                visited[i] = true;
                topicDTO = topics.get(i);
                subtopics = new LinkedList<>(topicDTO.getSubtopics());
                for(j=i+1; j<size; j++) {
                    if(matchTopic(topicDTO, topics.get(j))) {
                        visited[j] = true;
                        subtopics.addAll(topics.get(j).getSubtopics());
                    }
                }
                topicDTO.setSubtopics(subtopics);
                refinedTopics.add(topicDTO);
            }
        }

        finalCourse.setTopics(refinedTopics);
        return finalCourse;
    }

    private boolean matchTopic(TopicDTO topicDTO1, TopicDTO topicDTO2) {
        String[] words1 = topicDTO1.getName().split(" ");
        String[] words2 = topicDTO2.getName().split(" ");
        for(String w1: words1) {
            for(String w2: words2) {
                if(w1.equalsIgnoreCase(w2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
