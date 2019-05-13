package com.frontend.controllers;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frontend.dto.CourseDTO;
import com.frontend.service.CourseService;
import com.frontend.service.WebCrawlerService;

@Controller
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    WebCrawlerService crawlerService;

    @GetMapping("/course")
    public String getCourse(@RequestParam("course") String course, Model model) {
        CourseDTO courseDTO = courseService.findCoursesByName(course);
        model.addAttribute("course", courseDTO);
        return "course_info";
    }

    @GetMapping("/")
    public String getDefault(Model model) {
        Set<String> courseSet = courseService.findCourses();
        model.addAttribute("courses", courseSet);
        return "home";
    }

    @GetMapping("/crawl")
    @ResponseBody
    public String crawlCourse(@RequestParam("course") String course) throws IOException {
        if(course != null && course.length() > 0) {
            crawlerService.crawlCourse(course);
        }
        return "SUCCESS";
    }

    @GetMapping("/populateData")
    public void populateData() throws IOException {
        crawlerService.populateData("Java", "https://www.w3schools.com/java/");
    }
}
