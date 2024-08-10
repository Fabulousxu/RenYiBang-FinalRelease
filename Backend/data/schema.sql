DROP DATABASE IF EXISTS renyibang_order;
CREATE DATABASE renyibang_order;
USE renyibang_order;

CREATE TABLE `order`
(
    order_id    BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单id',
    type        TINYINT UNSIGNED DEFAULT 0 NOT NULL COMMENT '订单类型(0:任务订单,1:服务订单)',
    owner_id    BIGINT                     NOT NULL COMMENT '订单发布者id',
    accessor_id BIGINT                     NOT NULL COMMENT '订单接取者id',
    status      TINYINT UNSIGNED DEFAULT 0 NOT NULL COMMENT '订单状态(0:未付款,1:已付款,任务/服务进行中,2:接取者已完成，等待发布者确认,3:发布者已确认完成,4:订单已取消)',
    cost        BIGINT           DEFAULT 0 NOT NULL COMMENT '订单价格(存储100倍价格)',
    item_id     BIGINT                     NOT NULL COMMENT '任务/服务id'
) COMMENT '订单表';

DROP DATABASE IF EXISTS renyibang_user;
CREATE DATABASE renyibang_user;
USE renyibang_user;

CREATE TABLE user
(
    user_id   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    type      TINYINT      DEFAULT 0  NOT NULL COMMENT '用户类型(0:普通用户,1:客服,2:管理员)',
    nickname  VARCHAR(16)  DEFAULT '' NOT NULL COMMENT '用户昵称',
    avatar    LONGTEXT                NOT NULL COMMENT '用户头像',
    intro     TEXT         DEFAULT '' NOT NULL COMMENT '用户介绍',
    rating    TINYINT      DEFAULT 50 NOT NULL COMMENT '用户评分(存储10倍评分,范围0~100)',
    balance   BIGINT       DEFAULT 0  NOT NULL COMMENT '用户余额(存储100倍余额)',
    phone     VARCHAR(14)  DEFAULT '' NOT NULL COMMENT '用户手机号',
    email     VARCHAR(254) DEFAULT '' NOT NULL COMMENT '用户邮箱',
    following INT          DEFAULT 0  NOT NULL COMMENT '关注数',
    follower  INT          DEFAULT 0  NOT NULL COMMENT '粉丝数'
) comment '用户表';

CREATE TABLE auth
(
    user_id  BIGINT PRIMARY KEY COMMENT '用户id',
    password VARCHAR(16) NOT NULL COMMENT '用户密码',
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT '用户密码表';

CREATE TABLE follow
(
    follower_id BIGINT NOT NULL COMMENT '关注者id',
    followee_id BIGINT NOT NULL COMMENT '被关注者id',
    PRIMARY KEY (follower_id, followee_id),
    FOREIGN KEY (follower_id) REFERENCES user (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (followee_id) REFERENCES user (user_id) ON UPDATE CASCADE
) COMMENT '用户关注表';

DROP DATABASE IF EXISTS renyibang_task;
CREATE DATABASE renyibang_task;
USE renyibang_task;

create table task
(
    task_id     bigint auto_increment comment '任务id'
        primary key,
    owner_id    bigint                              not null comment '任务发布者id',
    title       varchar(32)                         not null comment '任务标题',
    images      longtext                            null comment '任务图片',
    description text                                null comment '任务描述',
    price       bigint    default 0                 not null comment '任务价格(存储100倍价格)',
    created_at  timestamp default CURRENT_TIMESTAMP not null comment '任务发布时间',
    max_access  int       default 1                 not null comment '任务最大接取数',
    rating      tinyint   default 50                not null comment '任务评分(存储10倍评分,范围0~100)',
    collected   bigint                              not null comment '任务收藏数',
    status      tinyint   default 0                 not null comment '任务状态'
)
    comment '任务表';

ALTER TABLE task ADD FULLTEXT INDEX title_description (title, description) WITH PARSER ngram;
ALTER TABLE task DROP INDEX title_description;
ALTER TABLE task ADD FULLTEXT INDEX title_description (title, description);



create table task_access
(
    task_access_id bigint auto_increment comment '任务接取候选id'
        primary key,
    task_id        bigint                              not null comment '任务id',
    accessor_id    bigint                              not null comment '接取者id',
    created_at     timestamp default CURRENT_TIMESTAMP not null comment '接取时间',
    status          tinyint default 0                  not null comment '接取任务状态',
    constraint task_access_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务接取候选表';

create index task_id
    on task_access (task_id);

create table task_collect
(
    task_collect_id bigint auto_increment comment '任务收藏id'
        primary key,
    task_id         bigint                              not null comment '任务id',
    collector_id    bigint                              not null comment '收藏者id',
    created_at      timestamp default CURRENT_TIMESTAMP not null comment '收藏时间',
    constraint task_collect_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务收藏表';

create index task_id
    on task_collect (task_id);

create table task_comment
(
    task_comment_id bigint auto_increment comment '任务评论id'
        primary key,
    task_id         bigint                              not null comment '任务id',
    commenter_id    bigint                              not null comment '任务评论者id',
    content         text                                not null comment '任务评论内容',
    created_at      timestamp default CURRENT_TIMESTAMP not null comment '任务评论时间',
    rating          tinyint   default 50                not null comment '任务评论评分(存储10倍评分,范围0~100)',
    liked_number    bigint                              not null comment '任务评论点赞数',
    constraint task_comment_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务评论表(仅有接取任务者才能评论)';

create index task_id
    on task_comment (task_id);

create table task_comment_like
(
    task_comment_like_id bigint auto_increment
        primary key,
    task_comment_id      bigint not null,
    liker_id             bigint not null,
    constraint task_comment_like_task_comment_task_comment_id_fk
        foreign key (task_comment_id) references task_comment (task_comment_id)
);

create table task_message
(
    task_message_id bigint auto_increment comment '任务留言id'
        primary key,
    task_id         bigint                              not null comment '任务id',
    messager_id     bigint                              not null comment '留言者id',
    content         text                                not null comment '消息内容',
    created_at      timestamp default CURRENT_TIMESTAMP not null comment '消息发送时间',
    liked_number    bigint                              not null comment '任务留言点赞数',
    constraint task_message_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务留言表';

create index task_id
    on task_message (task_id);

create table task_message_like
(
    task_message_id      bigint not null,
    liker_id             bigint not null,
    task_message_like_id bigint auto_increment
        primary key,
    constraint task_message_like_task_message_task_message_id_fk
        foreign key (task_message_id) references task_message (task_message_id)
);

DROP DATABASE IF EXISTS renyibang_service;
CREATE DATABASE renyibang_service;
USE renyibang_service;

create table service
(
    service_id  bigint auto_increment comment '服务id'
        primary key,
    owner_id    bigint                              not null comment '服务发布者id',
    title       varchar(32)                         not null comment '服务标题',
    images      longtext                            null comment '服务图片',
    description text                                null comment '服务描述',
    price       bigint    default 0                 not null comment '服务价格(存储100倍价格)',
    created_at  timestamp default CURRENT_TIMESTAMP not null comment '服务发布时间',
    max_access  int       default 1                 not null comment '服务最大接取数',
    rating      tinyint   default 50                not null comment '服务评分(存储10倍评分,范围0~100)',
    collected   bigint                              not null comment '服务收藏数',
    status      tinyint   default 0                 not null comment '服务状态'
)
    comment '服务表';

create table service_access
(
    service_access_id bigint auto_increment comment '服务接取候选id'
        primary key,
    service_id        bigint                              not null comment '服务id',
    accessor_id       bigint                              not null comment '接取者id',
    created_at        timestamp default CURRENT_TIMESTAMP not null comment '接取时间',
    valid             tinyint(1)                          not null comment '接取者是否被拒绝',
    constraint service_access_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务接取候选表';

create index service_id
    on service_access (service_id);

create table service_collect
(
    service_collect_id bigint auto_increment comment '服务收藏id'
        primary key,
    service_id         bigint                              not null comment '服务id',
    collector_id       bigint                              not null comment '收藏者id',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '收藏时间',
    constraint service_collect_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务收藏表';

create index service_id
    on service_collect (service_id);

create table service_comment
(
    service_comment_id bigint auto_increment comment '服务评论id'
        primary key,
    service_id         bigint                              not null comment '服务id',
    commenter_id       bigint                              not null comment '评论者id',
    content            text                                not null comment '评论内容',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '评论时间',
    rating             tinyint   default 50                not null comment '评论评分(存储10倍评分,范围0~100)',
    liked_number       bigint                              not null comment '评论点赞数',
    constraint service_comment_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务评论表(仅有接取服务者才能评论)';

create index service_id
    on service_comment (service_id);

create table service_comment_like
(
    service_comment_like_id bigint auto_increment
        primary key,
    service_comment_id      bigint not null,
    liker_id                bigint not null,
    constraint service_comment_like_service_comment_service_comment_id_fk
        foreign key (service_comment_id) references service_comment (service_comment_id)
);

create table service_message
(
    service_message_id bigint auto_increment comment '服务留言id'
        primary key,
    service_id         bigint                              not null comment '服务id',
    messager_id        bigint                              not null comment '留言者id',
    content            text                                not null comment '消息内容',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '消息发送时间',
    liked_number       bigint                              not null comment '任务留言点赞数',
    constraint service_message_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务留言表';

create index service_id
    on service_message (service_id);

create table service_message_like
(
    service_message_id      bigint not null,
    liker_id                bigint not null,
    service_message_like_id bigint auto_increment
        primary key,
    constraint service_message_like_service_message_service_message_id_fk
        foreign key (service_message_id) references service_message (service_message_id)
);
