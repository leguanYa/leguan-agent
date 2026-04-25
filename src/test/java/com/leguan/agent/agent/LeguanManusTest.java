package com.leguan.agent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class LeguanManusTest {


    @Resource
    private LeguanManus leguanManus;

    @Test
    public void test() {


        String userPrompt = """  
                我的另一半居住在上海浦东新区，请帮我找到合适的约会地点
                并结合一些网络图片，制定一份详细的计划
                并以 PDF 格式输出""";

        String result = leguanManus.run(userPrompt);
        Assertions.assertNotNull(result);
    }

}