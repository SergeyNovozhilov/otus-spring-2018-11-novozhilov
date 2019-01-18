insert into GENRES (id, name) values(RANDOM_UUID(), 'Humor');
insert into GENRES (id, name) values(RANDOM_UUID(), 'Novel');
insert into GENRES (id, name) values(RANDOM_UUID(), 'Thriller');

insert into AUTHORS (id, name) values(RANDOM_UUID(), 'Jack London');
insert into AUTHORS (id, name) values(RANDOM_UUID(), 'Steven King');
insert into AUTHORS (id, name) values(RANDOM_UUID(), 'Jerome C. Jerome');
insert into AUTHORS (id, name) values(RANDOM_UUID(), 'Ambrose Bierce');

insert into BOOKS (id, title, genre) values(RANDOM_UUID(), 'Book by Jack London', (select id from GENRES where name='Novel'));
insert into BOOKS (id, title, genre) values(RANDOM_UUID(), 'Book by Jack London 2', (select id from GENRES where name='Humor'));
insert into BOOKS (id, title, genre) values(RANDOM_UUID(), 'Book by Steven King and Ambrose Bierce', (select id from GENRES where name='Thriller'));
insert into BOOKS (id, title, genre) values(RANDOM_UUID(), 'Book by Jerome C. Jerome', (select id from GENRES where name='Humor'));
insert into BOOKS (id, title, genre) values(RANDOM_UUID(), 'Book by Jerome C. Jerome Ch', (select id from GENRES where name='Thriller'));

insert into BOOKS_AUTHORS (id, book, author) values(RANDOM_UUID(), (select id from BOOKS where title='Book by Jack London'),(select id from AUTHORS where name='Jack London'));
insert into BOOKS_AUTHORS (id, book, author) values(RANDOM_UUID(), (select id from BOOKS where title='Book by Jack London 2'),(select id from AUTHORS where name='Jack London'));
insert into BOOKS_AUTHORS (id, book, author) values(RANDOM_UUID(), (select id from BOOKS where title='Book by Steven King and Ambrose Bierce'),(select id from AUTHORS where name='Steven King'));
insert into BOOKS_AUTHORS (id, book, author) values(RANDOM_UUID(), (select id from BOOKS where title='Book by Steven King and Ambrose Bierce'),(select id from AUTHORS where name='Ambrose Bierce'));
insert into BOOKS_AUTHORS (id, book, author) values(RANDOM_UUID(), (select id from BOOKS where title='Book by Jerome C. Jerome Ch'),(select id from AUTHORS where name='Jerome C. Jerome'));







