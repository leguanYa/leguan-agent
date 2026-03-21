package com.leguan.agent.tool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "跳动.pdf";
        String content = "欢迎来到这里，请选择你的身份";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
