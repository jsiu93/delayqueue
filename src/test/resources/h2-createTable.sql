DROP TABLE IF EXISTS t_delay_task;
CREATE TABLE t_delay_task (
 TASK_ID varchar(20) NOT NULL COMMENT '任务ID',
 RELATIVE_ID varchar(20) NULL COMMENT '关联ID,比如订单ID\会员卡ID\排期ID等',
 CLASSIFIER varchar(20) NOT NULL COMMENT '任务分类',
 TASK_NAME varchar(100) NOT NULL COMMENT '任务名称',
 TASK_SERVICE varchar(100) NOT NULL COMMENT '任务服务名称，需要实现Taskable接口',
 STATE varchar(3) NOT NULL COMMENT '待运行/运行中/已成功/已失败/已取消',
 RUN_AT datetime NOT NULL COMMENT '何时运行，此参数可以设置延时',
 TIMEOUT tinyint NOT NULL DEFAULT 0 COMMENT '超时时间（秒）',
 START_TIME datetime NULL COMMENT '开始运行时间',
 END_TIME datetime NULL COMMENT '结束运行时间',
 RESULT_STATE varchar(300) NULL COMMENT '任务执行状态',
 RESULT_STORE varchar(100) NOT NULL COMMENT '结果存储方式',
 RESULT TEXT  NULL COMMENT '任务执行详细结果',
 VAR1 varchar(30) NULL COMMENT '参数1',
 VAR2 varchar(30) NULL COMMENT '参数2',
 VAR3 varchar(30) NULL COMMENT '参数3',
 ATTACHMENT   TEXT  NULL COMMENT '附件',
 CREATE_TIME datetime NOT NULL  COMMENT '创建时间',
 PRIMARY KEY (TASK_ID)
)  COMMENT = '任务表';

CREATE INDEX IDX1_T_DELAY_TASK ON t_delay_task(RELATIVE_ID);
CREATE INDEX IDX2_T_DELAY_TASK ON t_delay_task(CLASSIFIER);