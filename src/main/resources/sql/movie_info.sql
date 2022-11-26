CREATE TABLE `movie_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `movie_name` varchar(200) NOT NULL DEFAULT '' COMMENT '电影名称',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='电影信息表';