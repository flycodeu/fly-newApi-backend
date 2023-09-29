#  用户表
create table user
(
    money         double       default 0                 null comment '用户余额',
    id            bigint auto_increment comment 'id'
        primary key,
    userName      varchar(256)                           null comment '用户昵称',
    userAccount   varchar(256)                           not null comment '账号',
    userAvatar    varchar(1024)                          null comment '用户头像',
    gender        tinyint                                null comment '性别',
    userRole      varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword  varchar(512)                           not null comment '密码',
    isDelete      tinyint      default 0                 not null comment '是否删除',
    accessKey     varchar(256)                           null comment 'accessKey',
    secretKey     varchar(256)                           null comment 'secretKey',
    phoneNum      varchar(20)                            null comment '手机号码',
    email         varchar(100)                           null comment '个人邮箱',
    downloadCount tinyint      default 2                 null comment '下载sk次数',
    updateTime    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    createTime    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint uni_userAccount
        unique (userAccount)
)
    comment '用户';


# 接口表
create table interface_info_new
(
    methodName     varchar(1024)                                               null comment '接口方法名字',
    sdkPath        varchar(1024)                                               null comment 'sdk路径',
    id             bigint auto_increment comment '主键'
        primary key,
    name           varchar(256)                                                not null comment '接口名称',
    description    varchar(256)                                                null comment '接口描述',
    port           int                                                         not null comment '端口号',
    IPAddress      varchar(128)  default 'localhost'                           not null comment 'Ip地址',
    url            varchar(512)                                                not null comment '接口地址url',
    requestHeader  varchar(1024) default '{"Content-Type":"application/json"}' null comment '请求头',
    responseHeader varchar(1024) default '{"Content-Type":"application/json"}' null comment '响应头',
    status         int           default 0                                     not null comment '接口状态（0-关闭，1-开启）',
    method         varchar(256)                                                not null comment '请求类型',
    userId         bigint                                                      not null comment '创建人',
    createTime     datetime      default CURRENT_TIMESTAMP                     not null comment '创建时间',
    updateTime     datetime      default CURRENT_TIMESTAMP                     not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDeleted      tinyint       default 0                                     not null comment '是否删除(0-未删, 1-已删)',
    requestParams  text                                                        null comment '请求参数',
    invokeCount    bigint        default 100                                   null comment '每个接口的调用次数',
    price          double        default 0.01                                  null comment '每百条的接口调用价格'
)
    comment '接口信息';


# 用户接口表
create table user_interface_info
(
    id              bigint auto_increment comment '主键'
        primary key,
    userId          bigint                             not null comment '调用用户Id',
    interfaceInfoId bigint                             not null comment '接口Id',
    totalNum        int      default 0                 not null comment '总调用次数',
    leftNum         int      default 0                 not null comment '剩余调用次数',
    status          int      default 0                 not null comment '0-正常 ，1-禁用',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
)
    comment '用户调用接口关系表';


# 订单表
create table order_api
(
    alipayTradeNo    varchar(1024)                      null comment '阿里支付订单id返回',
    id               bigint auto_increment comment 'id'
        primary key,
    delayTime        datetime                           null comment '延迟时间',
    userId           int                                not null comment '用户id',
    interfaceInfoId  int                                not null comment '接口id',
    totalMoney       double                             not null comment '总金额',
    price            double                             not null comment '单价',
    buyCount         bigint   default 1                 not null comment '购买次数',
    status           tinyint  default 0                 null comment '订单状态(0未支付，1已支付)',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    isDelete         tinyint  default 0                 null,
    orderSn          varchar(1024)                      null comment '订单编号',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    interfaceIfoName varchar(1024)                      null comment '接口名字'
)
    comment '订单';
