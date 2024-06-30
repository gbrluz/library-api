CREATE TABLE author (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        gender VARCHAR(10),
                        birth_year INTEGER NOT NULL,
                        cpf VARCHAR(14) NOT NULL UNIQUE
);

CREATE TABLE book (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      isbn VARCHAR(17) NOT NULL UNIQUE,
                      publication_date DATE NOT NULL
);

CREATE TABLE renter (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        gender VARCHAR(10),
                        number VARCHAR(15) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        birth_date DATE NOT NULL,
                        cpf VARCHAR(14) NOT NULL UNIQUE
);

CREATE TABLE rent (
                      id SERIAL PRIMARY KEY,
                      pickup_date DATE NOT NULL,
                      return_date DATE NOT NULL,
                      renter_id INTEGER NOT NULL,
                      CONSTRAINT fk_renter
                          FOREIGN KEY(renter_id)
                              REFERENCES renter(id)
);

CREATE TABLE book_author (
                             book_id INTEGER NOT NULL,
                             author_id INTEGER NOT NULL,
                             PRIMARY KEY (book_id, author_id),
                             CONSTRAINT fk_book
                                 FOREIGN KEY(book_id)
                                     REFERENCES book(id),
                             CONSTRAINT fk_author
                                 FOREIGN KEY(author_id)
                                     REFERENCES author(id)
);

CREATE TABLE rent_book (
                           rent_id INTEGER NOT NULL,
                           book_id INTEGER NOT NULL,
                           PRIMARY KEY (rent_id, book_id),
                           CONSTRAINT fk_rent
                               FOREIGN KEY(rent_id)
                                   REFERENCES rent(id),
                           CONSTRAINT fk_rent_book
                               FOREIGN KEY(book_id)
                                   REFERENCES book(id)
);
