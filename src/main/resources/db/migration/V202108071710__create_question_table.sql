CREATE TABLE question (
  id BIGINT PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  author TEXT NOT NULL,
  message TEXT NOT NULL,
  reply_count INT NOT NULL
);
