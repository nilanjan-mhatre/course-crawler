package com.web_crawler.jsoup_web_crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        String url = "https://www.tutorialspoint.com/index.htm";
        String baseURL = "https://www.tutorialspoint.com/";
        Set<String> visited = new HashSet<>();
        visited.add(baseURL);
        Set<String> ignore = new HashSet<>();
        ignore.add(url);
        ignore.add(baseURL);
        String topic = "Java";

        System.out.println(new App().processURL(url, baseURL, topic, visited, ignore));
    }

    public String processURL(String link, String baseURL, String topic, Set<String> visited, Set<String> ignore)  {
        String url = null;
        if(link != null && !visited.contains(link) && hasEqualDomain(link, baseURL)) {
            System.out.println(link);
            visited.add(link);
            Document document;
            try {
                document = Jsoup.connect(link).get();
            } catch (IOException e) {
                return null;
            }
            String[] title = document.title().split(" ");
            if(!ignore.contains(link)
                    && title != null
                    && title.length >= 2
                    && title[0].equalsIgnoreCase(topic)
                    && title[title.length - 1].equalsIgnoreCase("tutorial")) {
                return link;
            }
            Elements links = document.select("a");
            for(int i=0; i<links.size(); i++) {
                url = processURL(links.get(i).attr("abs:href"), baseURL, topic, visited, ignore);
                if(url != null) {
                    break;
                }
            }
        }
        return url;
    }

    public boolean hasEqualDomain(String link, String baseURL) {
        return link.contains(baseURL);
    }

    public void processHomeURL(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        Elements links = document.select("aside");
        links.addAll(document.select("div"));
        Element sidebar = null;
        Element element = null;
        for(int i=0; i<links.size(); i++) {
            element = links.get(i);
            for(String name: element.classNames()) {
                if(name.contains("sidebar")) {
                    sidebar = element;
                    break;
                }
            }
            if(sidebar != null) {
                break;
            }
        }
        if(sidebar != null) {
            List<String> subtopicLinks = new LinkedList<>();
            Elements subtopic = sidebar.select("a");
            int size = Math.min(subtopic.size(), 10);
            for(int i=0; i<size; i++) {
                subtopicLinks.add(subtopic.get(i).attr("abs:href"));
            }
            parseLinks(subtopicLinks);
        }
    }

    public void parseLinks(List<String> subtopicLinks) {
        for(String url: subtopicLinks) {
        }
    }
}
