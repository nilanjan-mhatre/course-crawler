package com.frontend.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frontend.dto.CourseDTO;
import com.frontend.dto.SubtopicDTO;
import com.frontend.dto.TopicDTO;
import com.frontend.entities.Course;
import com.frontend.repository.CourseRepository;
import com.frontend.utils.ObjectMapperUtils;

@Service
@Transactional
public class WebCrawlerService {

    @Autowired
    CourseRepository courseRepository;

    ObjectMapperUtils objectMapper = new ObjectMapperUtils();

    public void crawlCourse(String course) throws IOException {

        Runnable r1 = () -> {
            String url = "https://www.tutorialspoint.com/index.htm";
            String baseURL = "https://www.tutorialspoint.com/";
            Set<String> visited = new HashSet<>();
            visited.add(baseURL);
            Set<String> ignore = new HashSet<>();
            ignore.add(url);
            ignore.add(baseURL);

            String source = processURLBFS(url, baseURL, course, visited, ignore);
            System.out.println("Final: " + source);
            if(source != null) {
                populateData(course, source);
            }
        };

        Runnable r2 = () -> {
            String url = "https://www.w3schools.com/";
            String baseURL = "https://www.w3schools.com/";
            Set<String> visited = new HashSet<>();
            Set<String> ignore = new HashSet<>();
            ignore.add(url);
            ignore.add(baseURL);

            String source = processURLBFS(url, baseURL, course, visited, ignore);
            System.out.println("Final: " + source);
            if(source != null) {
                populateData(course, source);
            }
        };

        ExecutorService service = new ThreadPoolExecutor(2, 3, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        service.submit(r1);
        service.submit(r2);
    }

    public void populateData(String title, String source) {
        ObjectMapperUtils objectMapper = new ObjectMapperUtils();
        System.out.println(source);
        try {
            CourseDTO courseDTO = new CourseDTO(title, source);
            processHomeURL(courseDTO);
            Course course = objectMapper.map(courseDTO, Course.class);
            courseRepository.save(course);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String processURLBFS(String start, String baseURL, String topic, Set<String> visited, Set<String> ignore)  {
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        while(!queue.isEmpty()) {
            String link = queue.poll();
            if(link != null && !visited.contains(link) && hasEqualDomain(link, baseURL)) {
                System.out.println(link);
                visited.add(link);
                Document document;
                try {
                    document = Jsoup.connect(link).get();
                } catch (IOException e) {
                    return null;
                }
                String title = document.title();
                if(!ignore.contains(link)
                        && title != null
                        && title.equalsIgnoreCase(topic + " Tutorial")) {
                    return link;
                }
                Elements links = document.select("a");
                for(int i=0; i<links.size(); i++) {
                    String newLink = links.get(i).attr("abs:href");
                    queue.add(newLink);
                }
            }
        }
        return null;
    }

    public boolean hasEqualDomain(String link, String baseURL) {
        return link.contains(baseURL);
    }

    public void processHomeURL(CourseDTO course) throws IOException {
        String className = "sidebar";
        Document document = Jsoup.connect(course.getSource()).get();
        Element sidebar = findElementByClassName(className, document);
        if(sidebar != null) {
            List<TopicDTO> topics = new LinkedList<>();
            Elements topicLinks = sidebar.select("a");
            int size = Math.min(topicLinks.size(), 10);
            for(int i=0; i<size; i++) {
                Element ele = topicLinks.get(i);
                System.out.println(ele.attr("abs:href"));
                String name = getHtml(ele.outerHtml());
                if(name != null && name.length() > 1) {
                    TopicDTO topic = new TopicDTO();
                    topic.setUrl(ele.attr("abs:href"));
                    topic.setName(refineName(name, course.getTitle()));
                    topic.setCourse(course);
                    topics.add(topic);
                }
            }
            course.setTopics(topics);
            parseTopicLinks(course, topics);
        }
    }

    private Element findElementByClassName(String classname, Document document) {
        Elements links = document.select("aside");
        Elements divs = document.select("body > div");
        links.addAll(divs);
        links.addAll(divs.select("div"));
        Element sidebar = null;
        Element element = null;
        for(int i=0; i<links.size(); i++) {
            element = links.get(i);
            for(String name: element.classNames()) {
                if(name.contains(classname)) {
                    sidebar = element;
                    break;
                }
            }
            if(sidebar != null) {
                break;
            }
        }
        return sidebar;
    }

    public String refineName(String name, String keyword) {
        int i = keyword.length();
        while(i<name.length() && !Character.isLetter(name.charAt(i))) {
            i++;
        }
        return name.substring(i);
    }

    public void parseTopicLinks(CourseDTO course, List<TopicDTO> topicLinks) throws IOException {
        for(TopicDTO topic: topicLinks) {
            parseLink(topic);
        }
    }

    public void parseLink(TopicDTO topic) throws IOException {
        String className = "main";
        Document document = Jsoup.connect(topic.getUrl()).get();
        Element content = findElementByClassName(className, document);

        Elements elements = content.select("h1,h2,p");
        StringBuilder str = new StringBuilder();
        String name = topic.getName();
        List<SubtopicDTO> subTopics = new LinkedList<>();

        for(int i=0; i<elements.size(); i++) {
            Element ele = elements.get(i);
            if(ele.tagName().equals("p")) {
                str.append(getHtml(ele.outerHtml()));
            } else if(ele.tagName().contains("h")) {
                if(str.length() > 0) {
                    SubtopicDTO subtopic = new SubtopicDTO(name, str.toString());
                    subtopic.setTopic(topic);
                    subTopics.add(subtopic);
                    name = getHtml(ele.outerHtml());
                    str.delete(0, str.length());
                }
            }
        }
        topic.setSubtopics(subTopics);
    }

    public String getHtml(String html) {
        StringBuilder str = new StringBuilder();
        String s = html;
        boolean flag = true;
        for(int i=0; i<s.length(); i++) {
            char ch = s.charAt(i);
            if(ch == '<') {
                flag = false;
            } else if(ch == '>') {
                flag = true;
            } else if(flag) {
                str.append(ch);
            }
        }
        return str.toString();
    }
}
