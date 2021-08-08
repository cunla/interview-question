CREATE TABLE reply (
  id BIGINT PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  question_id BIGINT REFERENCES question(id),
  author TEXT NOT NULL,
  message TEXT NOT NULL
);
