CREATE TABLE `user_chat_message`
(
    `id`              bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`         bigint       DEFAULT NULL COMMENT '用户id',
    `conversation_id` varchar(255) DEFAULT NULL COMMENT '用户会话id',
    `message_type`    varchar(50)  DEFAULT NULL COMMENT '消息类型',
    `text`            longtext COMMENT '内容',
    `metadata`        text COMMENT '元信息',
    `create_time`     datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`     datetime     DEFAULT NULL COMMENT '更新时间',
    `is_delete`       tinyint      DEFAULT NULL COMMENT '逻辑删除:0=未删,1=删除',
    PRIMARY KEY (`id`),
    KEY               `idx_user_id` (`user_id`),
    KEY               `idx_conversation_id` (`conversation_id`),
    KEY               `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户消息记录表';