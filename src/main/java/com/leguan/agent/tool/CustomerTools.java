package com.leguan.agent.tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leguan.agent.entity.UserInfo;
import com.leguan.agent.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class CustomerTools {

    @Resource
    private UserInfoMapper userInfoMapper;

    // returnDirect = true设置这个属性代表是立即返回不用在经过模型了，防止有些重要的信息暴露给AI
    @Tool(description = "Retrieve user information", returnDirect = true)
    UserInfo getCustomerInfo(ToolContext toolContext) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        Long userId = (Long) toolContext.getContext().get("userId");
        queryWrapper.eq("user_id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        return userInfo;
    }

}
