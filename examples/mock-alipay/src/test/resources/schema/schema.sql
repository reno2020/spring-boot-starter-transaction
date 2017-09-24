CREATE TABLE t_order(
  id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  order_id VARCHAR(60),
  amount INT,
  order_status INT
);
