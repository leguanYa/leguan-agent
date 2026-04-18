package com.leguan.leguanimagesearchmcpserver.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;
    @Test
    public void testGetImageSimilarity() {
        String computers = imageSearchTool.searchImage("computer");
        System.out.println(computers);
        Assertions.assertNotNull(computers);
    }

}