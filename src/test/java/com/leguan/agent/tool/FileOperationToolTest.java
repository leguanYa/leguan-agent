package com.leguan.agent.tool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest
public class FileOperationToolTest {

    @Test
    public void testReadFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "乐观.txt";
        String result = tool.readFile(fileName);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testWriteFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "乐观.txt";
        String content = "关于学习相关的知识集锦";
        String result = tool.writeFile(fileName, content);
        assertNotNull(result);
    }
}
