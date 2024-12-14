use category;
SELECT * FROM categories;

CREATE TABLE categories (
	category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    parent_id INT DEFAULT NULL,
    FOREIGN KEY (parent_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

INSERT INTO categories (category_name, parent_id)
VALUES ('Obat-Obatan', NULL);

INSERT INTO categories (category_name, parent_id)
VALUES ('Antibiotik', 10), ('Analgesik', 10), ('Antihistamin', 10);

SELECT * FROM categories;
SELECT category_id, category_name
FROM categories
WHERE parent_id = 1;