# Fundamental Schema

# --- !Ups

CREATE TABLE messages (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'the unique id',
  username VARCHAR(63) NOT NULL COMMENT 'The user to whom this message send',
  content VARCHAR(255) NOT NULL COMMENT 'The content of this message',
  expiration_date TIMESTAMP NOT NULL COMMENT 'The timestamp when this message will expire',
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs

DROP TABLE messages;