package com.frontend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.frontend.dto.TopicDTO;
import com.frontend.service.WebCrawlerService;
import com.web_crawler.jsoup_web_crawler.App;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Mock
    WebCrawlerService crawlerService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void refineNameTest() {
        App a = new App();
        Assert.assertEquals(a.refineName("Java - Home", "Java"), "Home");
    }

    @Test
    public void parseLinkTest() throws IOException {
        App a = new App();
        TopicDTO topic = new TopicDTO();
        topic.setUrl("https://www.w3schools.com/java/java_type_casting.asp");
        a.parseLink(topic);
    }

    @Test
    public void getHtmlTest() {
        String str = "<a class='videolink' href='/java_online_training/index.asp' target='_blank'><img src='/java/images/java-video-tutorials.jpg' alt='Java Video Tutorials'></a>";
        Assert.assertEquals(crawlerService.getHtml(str), null);
    }
}
