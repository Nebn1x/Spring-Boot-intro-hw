INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (101, 'Title1', 'Author1', 'Isbn1', 100, 'Description1', 'CoverImage1', FALSE),
       (102, 'Title2', 'Author2', 'Isbn2', 200, 'Description2', 'CoverImage2', FALSE),
       (103, 'Title3', 'Author3', 'Isbn3', 300, 'Description3', 'CoverImage3', FALSE);

INSERT INTO books_categories (book_id, category_id)
VALUES (101, 101),
       (102, 101),
       (103, 102);