## 账户余额表，存储账户余额信息
create table if not exists account(
    account_no varchar(64) not null ,
    amount DOUBLE ,
    freezed_amount DOUBLE ,
    primary key (account_no)
);

create table if not exists  account_point(
    tx_id varchar(128) not null,
    account_no varchar(256) not null ,
    point DOUBLE ,
    status int,
    primary key (tx_id)
);

## 账号操作流水表，记录每一笔分布式事务操作的账号、操作金额、操作类型（扣钱/加钱），TCC模式下才会使用到此表
create table if not exists  account_transaction(
    tx_id varchar(128) not null,
    account_no varchar(256) not null,
    amount DOUBLE,
    type varchar(256) not null,
    primary key (tx_id)
);

create table if not exists `dtx_branch_info` (
    `action_id` varchar(128) NOT NULL COMMENT '分支事务号', 
    `tx_id` varchar(128)  NOT NULL  COMMENT '主事务号',
    `status` varchar(4)  COMMENT '事务状态',
    `log_info` blob DEFAULT NULL COMMENT 'undo/redo log',
    `biz_type` varchar(32) DEFAULT NULL COMMENT '发起方业务类型',
    `instance_id` varchar(32) NOT NULL COMMENT '实例id',
    `context` varchar(2000) DEFAULT NULL COMMENT '分支上下文',
    `feature` varchar(2000) DEFAULT NULL COMMENT '扩展属性',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '修改时间',

     PRIMARY KEY (`action_id`),
     KEY `idx_txId_action_id` (`action_id`,`tx_id`,`instance_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='分支记录日志'
;

create table if not exists `dtx_row_lock` (
  `action_id` varchar(128) NOT NULL COMMENT '分支事务号',
  `tx_id` varchar(128) NOT NULL COMMENT '主事务号',
  `table_name` varchar(64) DEFAULT NULL COMMENT '表名称',
  `row_key` varchar(250) NOT NULL COMMENT '行唯一key',
  `instance_id` varchar(32) NOT NULL COMMENT '实例id',
  `context` varchar(2000) DEFAULT NULL COMMENT '分支上下文',
  `feature` varchar(2000) DEFAULT NULL COMMENT '扩展属性',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`row_key`),
  KEY `idx_row_lock_txid_action_id` (`action_id`,`tx_id`,`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='行锁'
;


create table if not exists `dtx_tcc_action` (
  `action_id` varchar(96) NOT NULL COMMENT '分支事务号',
  `action_name` varchar(64) DEFAULT NULL COMMENT '参与者名称',
  `tx_id` varchar(128) NOT NULL COMMENT '主事务号',
  `action_group` varchar(32) DEFAULT NULL COMMENT 'action group',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `param_data` varchar(4000) DEFAULT NULL COMMENT '一阶段方法参数数据',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `sharding_key` varchar(128) DEFAULT NULL COMMENT '分库分表字段',
  PRIMARY KEY (`action_id`) ,
  UNIQUE KEY  `idx_tx_id` (`tx_id`, `action_name`)
) 
;