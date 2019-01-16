DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS(
  ID UUID PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES
(
  ID UUID PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS
(
  ID UUID PRIMARY KEY NOT NULL,
  TITLE VARCHAR(255) NOT NULL,
  GENRE UUID NULLABLE ,

  FOREIGN KEY (GENRE) REFERENCES GENRES(ID)
);

DROP TABLE IF EXISTS BOOKS_AUTHORS;
CREATE TABLE BOOKS_AUTHORS
(
  ID UUID PRIMARY KEY NOT NULL,
  BOOK UUID,
  AUTHOR UUID,

  FOREIGN KEY (BOOK) REFERENCES BOOKS(ID),
  FOREIGN KEY (AUTHOR) REFERENCES AUTHORS(ID)
);

