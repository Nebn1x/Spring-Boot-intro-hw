INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Title1', 'Author1', 'Isbn1', 100, 'Description1', 'CoverImage1', FALSE),
       (2, 'Title2', 'Author2', 'Isbn2', 200, 'Description2', 'CoverImage2', FALSE),
       (3, 'Title3', 'Author3', 'Isbn3', 300, 'Description3', 'CoverImage3', FALSE);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 1),
       (3, 2);