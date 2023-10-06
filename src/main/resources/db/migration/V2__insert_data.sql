insert into country (name)
values
    ('Bulgaria')
ON DUPLICATE KEY update
    name = VALUES(name);

insert into city (name, country_id)
values
    ('Sofia', 1),
    ('Plovdiv', 1),
    ('Varna', 1),
    ('Burgas', 1),
    ('Ruse', 1),
    ('Stara Zagora', 1),
    ('Pleven', 1),
    ('Sliven', 1),
    ('Dobrich', 1),
    ('Shumen', 1),
    ('Haskovo', 1),
    ('Pazardzhik', 1),
    ('Blagoevgrad', 1),
    ('Veliko Tarnovo', 1),
    ('Gabrovo', 1),
    ('Asenovgrad', 1),
    ('Vratsa', 1),
    ('Yambol', 1),
    ('Kardzhali', 1),
    ('Kyustendil', 1),
    ('Montana', 1),
    ('Lovech', 1),
    ('Silistra', 1),
    ('Targovishte', 1),
    ('Vidin', 1),
    ('Razgrad', 1),
    ('Peshtera', 1),
    ('Svishtov', 1),
    ('Smolyan', 1)
ON DUPLICATE KEY update
    name = VALUES(name),
    country_id = VALUES(country_id);

insert into `author`
(`first_name`, `last_name`, `country_id`, `city_id`, `birth_date`, `is_alive`, `death_date`)
VALUES
('John', 'Smith', 1, 1, '1980-05-10', 1, NULL),
('Mary', 'Johnson', 1, 2, '1975-03-20', 1, NULL),
('Robert', 'Brown', 1, 3, '1992-08-15', 1, NULL),
('Susan', 'Davis', 1, 4, '1988-11-30', 1, NULL),
('Michael', 'Wilson', 1, 5, '1972-04-25', 1, NULL),
('Emily', 'Taylor', 1, 6, '1995-06-12', 1, NULL),
('David', 'Lee', 1, 7, '1987-09-05', 1, NULL),
('Jennifer', 'White', 1, 8, '1983-02-18', 1, NULL),
('William', 'Hall', 1, 9, '1978-12-08', 1, NULL),
('Sarah', 'Adams', 1, 10, '1990-07-14', 1, NULL),
('Christopher', 'Martin', 1, 11, '1982-03-28', 1, NULL),
('Jessica', 'Harris', 1, 12, '1974-10-22', 1, NULL),
('Daniel', 'Turner', 1, 13, '1993-09-03', 1, NULL),
('Lisa', 'Green', 1, 14, '1985-01-09', 1, NULL),
('Matthew', 'Lewis', 1, 15, '1981-06-16', 1, NULL),
('Amanda', 'Baker', 1, 16, '1991-12-04', 1, NULL),
('Andrew', 'Scott', 1, 17, '1977-04-19', 1, NULL),
('Olivia', 'King', 1, 18, '1986-08-23', 1, NULL),
('Joshua', 'Turner', 1, 19, '1979-11-11', 1, NULL),
('Elizabeth', 'Adams', 1, 20, '1984-07-27', 1, NULL),
('Thomas', 'Johnson', 1, 21, '1960-11-02', 0, '2021-03-15'),
('Helen', 'Anderson', 1, 22, '1955-07-18', 0, '2020-05-10'),
('George', 'Wright', 1, 23, '1970-01-25', 0, '2019-08-22'),
('Carol', 'Harris', 1, 24, '1965-09-08', 0, '2022-01-30'),
('Edward', 'Davis', 1, 25, '1950-04-12', 0, '2021-11-05'),
('Sophia', 'Brown', 1, 1, '1958-02-28', 0, '2020-12-19'),
('Paul', 'Smith', 1, 2, '1968-06-03', 0, '2022-04-25'),
('Nancy', 'Moore', 1, 3, '1952-12-15', 0, '2021-06-08'),
('Richard', 'Clark', 1, 4, '1963-03-10', 0, '2020-09-14'),
('Linda', 'Turner', 1, 5, '1957-08-04', 0, '2021-08-27')
ON DUPLICATE KEY update
  `first_name` = VALUES(`first_name`),
  `last_name` = VALUES(`last_name`),
  `country_id` = VALUES(`country_id`),
  `city_id` = VALUES(`city_id`),
  `birth_date` = VALUES(`birth_date`),
  `is_alive` = VALUES(`is_alive`),
  `death_date` = VALUES(`death_date`);

insert into narrator
(biography, birth_date, death_date, first_name, is_alive, last_name, city_id, country_id)
values
('John Doe was a renowned narrator known for his captivating storytelling. He was born on March 15, 1990, and sadly passed away on September 1, 2021.', '1990-03-15', '2021-09-01', 'John', 1, 'Doe', 1, 1),
('Jane Smith, born on May 20, 1985, was a talented narrator who left a mark in the world of storytelling. She passed away on August 27, 2021.', '1985-05-20', '2021-08-27', 'Jane', 1, 'Smith', 2, 1),
('Michael Johnson was a narrator known for his distinctive style and voice. He was born on November 10, 1978, and passed away on August 10, 2021.', '1978-11-10', '2021-08-10', 'Michael', 0, 'Johnson', 3, 1),
('Emily Brown, born on September 18, 1992, was a talented narrator whose storytelling touched many hearts. She passed away on July 25, 2021.', '1992-09-18', '2021-07-25', 'Emily', 1, 'Brown', 4, 1),
('Daniel Taylor, born on July 12, 1980, was a gifted narrator known for bringing stories to life. He passed away on September 5, 2021.', '1980-07-12', '2021-09-05', 'Daniel', 1, 'Taylor', 5, 1),
('Sophia Clark, born on April 22, 1989, was a beloved narrator with a unique storytelling style. She passed away on September 2, 2021.', '1989-04-22', '2021-09-02', 'Sophia', 1, 'Clark', 6, 1),
('Andrew Harris was a narrator known for his compelling storytelling that kept audiences engaged. He was born on December 5, 1983, and passed away on August 15, 2021.', '1983-12-05', '2021-08-15', 'Andrew', 1, 'Harris', 7, 1),
('Olivia White, born on August 8, 1975, was a seasoned narrator whose storytelling left a lasting impact. She passed away on July 18, 2021.', '1975-08-08', '2021-07-18', 'Olivia', 1, 'White', 8, 1),
('Matthew Davis was a talented narrator known for his captivating storytelling. He was born on June 25, 1991, and passed away on August 3, 2021.', '1991-06-25', '2021-08-03', 'Matthew', 1, 'Davis', 9, 1),
('Sophie Lewis, born on October 9, 1987, was a beloved narrator who brought stories to life through her unique voice. She passed away on September 8, 2021.', '1987-10-09', '2021-09-08', 'Sophie', 1, 'Lewis', 10, 1),
('Aiden Miller was a distinguished narrator known for his exceptional storytelling skills. He was born on March 21, 1986, and passed away on July 30, 2021.', '1986-03-21', '2021-07-30', 'Aiden', 1, 'Miller', 11, 1),
('Ella Moore, born on January 17, 1994, was a talented narrator whose voice resonated with listeners. She passed away on August 20, 2021.', '1994-01-17', '2021-08-20', 'Ella', 1, 'Moore', 12, 1),
('Liam Turner was a narrator known for his unique storytelling style. He was born on September 8, 1988, and passed away on August 7, 2021.', '1988-09-08', '2021-08-07', 'Liam', 1, 'Turner', 13, 1),
('Aria Davis, born on December 14, 1993, was a beloved narrator whose storytelling left a lasting impression. She passed away on August 22, 2021.', '1993-12-14', '2021-08-22', 'Aria', 1, 'Davis', 14, 1),
('Logan Thompson was a renowned narrator with a captivating storytelling style. He was born on October 5, 1979, and passed away on August 11, 2021.', '1979-10-05', '2021-08-11', 'Logan', 1, 'Thompson', 15, 1),
('Grace Adams, born on February 27, 1990, was a talented narrator known for her engaging storytelling. She passed away on August 25, 2021.', '1990-02-27', '2021-08-25', 'Grace', 1, 'Adams', 16, 1),
('Lucas Baker was a narrator known for his captivating storytelling that captured the imagination of many. He was born on July 19, 1984, and passed away on August 28, 2021.', '1984-07-19', '2021-08-28', 'Lucas', 1, 'Baker', 17, 1),
('Lily Hill, born on March 30, 1987, was a beloved narrator whose voice brought stories to life. She passed away on September 4, 2021.', '1987-03-30', '2021-09-04', 'Lily', 1, 'Hill', 18, 1),
('Henry Clark was a narrator known for his exceptional storytelling skills. He was born on November 14, 1976, and passed away on August 16, 2021.', '1976-11-14', '2021-08-16', 'Henry', 1, 'Clark', 19, 1),
('Ava Lewis, born on August 10, 1981, was a talented narrator with a unique storytelling style. She passed away on August 29, 2021.', '1981-08-10', '2021-08-29', 'Ava', 1, 'Lewis', 20, 1)
ON DUPLICATE KEY update
    biography = VALUES(biography),
    birth_date = VALUES(birth_date),
    death_date = VALUES(death_date),
    first_name = VALUES(first_name),
    is_alive = VALUES(is_alive),
    last_name = VALUES(last_name),
    city_id = VALUES(city_id),
    country_id = VALUES(country_id);

insert into `category`
(`name`)
VALUES
('Fiction'),
('Mystery'),
('Romance'),
('Science Fiction'),
('Fantasy'),
('Thriller'),
('Biography'),
('Self-Help'),
('History'),
('Science'),
('Technology'),
('Cooking'),
('Travel'),
('Art'),
('Health'),
('Religion'),
('Philosophy'),
('Psychology'),
('Business'),
('Children')
ON DUPLICATE KEY update
`name` = VALUES(`name`);

insert into `product`
(`isbn`, `avg_rating`, `description`, `language`, `price`, `product_type`, `publication_year`, `title`)
VALUES
('8734567890123', 5.0, 'A thrilling mystery audiobook', 'English', 15.99, 'AUDIOBOOK', 2022, 'Thrilling Mystery'),
('8745678901234', 4.0, 'An inspiring self-help audiobook', 'English', 12.99, 'AUDIOBOOK', 2021, 'Inspiring Self-Help'),
('8756789012345', 4.0, 'An adventurous fantasy audiobook', 'English', 17.99, 'AUDIOBOOK', 2023, 'Adventurous Fantasy'),
('8767890123456', 5.0, 'A romantic love story audiobook', 'English', 14.99, 'AUDIOBOOK', 2020, 'Romantic Love Story'),
('8778901234567', 5.0, 'An educational science audiobook', 'English', 16.99, 'AUDIOBOOK', 2021, 'Educational Science'),
('8789012345678', 4.0, 'A suspenseful thriller audiobook', 'English', 15.49, 'AUDIOBOOK', 2022, 'Suspenseful Thriller'),
('8790123456789', 5.0, 'A captivating history audiobook', 'English', 13.99, 'AUDIOBOOK', 2019, 'Captivating History'),
('8701234567890', 4.0, 'A sci-fi adventure audiobook', 'English', 18.99, 'AUDIOBOOK', 2023, 'Sci-Fi Adventure'),
('8712345678901', 4.0, 'A motivational success audiobook', 'English', 12.49, 'AUDIOBOOK', 2022, 'Motivational Success'),
('8723456789012', 3.0, 'An informative biography audiobook', 'English', 14.79, 'AUDIOBOOK', 2020, 'Informative Biography'),
('8787654321098', 2.0, 'A relaxing meditation audiobook', 'English', 12.99, 'AUDIOBOOK', 2021, 'Relaxing Meditation'),
('8776543210987', 5.0, 'A poetic literature audiobook', 'English', 16.79, 'AUDIOBOOK', 2018, 'Poetic Literature'),
('8765432109876', 4.0, 'An intriguing mystery audiobook', 'English', 15.49, 'AUDIOBOOK', 2019, 'Intriguing Mystery'),
('8754321098765', 5.0, 'A thrilling action audiobook', 'English', 17.99, 'AUDIOBOOK', 2023, 'Thrilling Action'),
('8743210987654', 3.0, 'A romantic drama audiobook', 'English', 13.99, 'AUDIOBOOK', 2020, 'Romantic Drama'),
('8732109876543', 4.0, 'An informative educational audiobook', 'English', 16.49, 'AUDIOBOOK', 2021, 'Informative Educational'),
('8721098765432', 4.0, 'A thought-provoking philosophy audiobook', 'English', 15.49, 'AUDIOBOOK', 2019, 'Thought-Provoking Philosophy'),
('8710987654321', 0, 'A captivating fiction audiobook', 'English', 15.99, 'AUDIOBOOK', 2022, 'Captivating Fiction'),
('8709876543210', 0, 'A mysterious thriller audiobook', 'English', 16.79, 'AUDIOBOOK', 2020, 'Mysterious Thriller'),
('8798765432109', 5.0, 'A historical drama audiobook', 'English', 15.49, 'AUDIOBOOK', 2018, 'Historical Drama'),
('7734567890123', 5.0, 'A thrilling mystery book', 'English', 19.99, 'BOOK', 2022, 'Thrilling Mystery Book'),
('7745678901234', 2.0, 'An inspiring self-help book', 'English', 15.99, 'BOOK', 2021, 'Inspiring Self-Help Book'),
('7756789012345', 3.0, 'An adventurous fantasy book', 'English', 17.99, 'BOOK', 2023, 'Adventurous Fantasy Book'),
('7767890123456', 4.0, 'A romantic love story book', 'English', 14.99, 'BOOK', 2020, 'Romantic Love Story Book'),
('7778901234567', 4.0, 'An educational science book', 'English', 16.99, 'BOOK', 2021, 'Educational Science Book'),
('7789012345678', 5.0, 'A suspenseful thriller book', 'English', 15.49, 'BOOK', 2022, 'Suspenseful Thriller Book'),
('7790123456789', 0, 'A captivating history book', 'English', 13.99, 'BOOK', 2019, 'Captivating History Book'),
('7701234567890', 0, 'A sci-fi adventure book', 'English', 18.99, 'BOOK', 2023, 'Sci-Fi Adventure Book'),
('7712345678901', 2.0, 'A motivational success book', 'English', 12.49, 'BOOK', 2022, 'Motivational Success Book'),
('7723456789012', 1.0, 'An informative biography book', 'English', 14.79, 'BOOK', 2020, 'Informative Biography Book'),
('7787654321098', 3.0, 'A relaxing meditation book', 'English', 12.99, 'BOOK', 2021, 'Relaxing Meditation Book'),
('7776543210987', 4.0, 'A poetic literature book', 'English', 16.79, 'BOOK', 2018, 'Poetic Literature Book'),
('7765432109876', 4.0, 'An intriguing mystery book', 'English', 15.49, 'BOOK', 2019, 'Intriguing Mystery Book'),
('7754321098765', 0, 'A thrilling action book', 'English', 17.99, 'BOOK', 2023, 'Thrilling Action Book'),
('7743210987654', 5.0, 'A romantic drama book', 'English', 13.99, 'BOOK', 2020, 'Romantic Drama Book'),
('7732109876543', 3.0, 'An informative educational book', 'English', 16.49, 'BOOK', 2021, 'Informative Educational Book'),
('7721098765432', 2.0, 'A thought-provoking philosophy book', 'English', 15.49, 'BOOK', 2019, 'Thought-Provoking Philosophy Book'),
('7710987654321', 4.0, 'A captivating fiction book', 'English', 15.99, 'BOOK', 2022, 'Captivating Fiction Book'),
('7709876543210', 4.0, 'A mysterious thriller book', 'English', 16.79, 'BOOK', 2020, 'Mysterious Thriller Book'),
('7798765432109', 2.0, 'A historical drama book', 'English', 15.49, 'BOOK', 2018, 'Historical Drama Book'),
('9780123456781', 5.0, 'Explore the world of science and technology with this comprehensive eBook. Learn about the latest advancements and discoveries that are shaping our future.', 'English', 10.99, 'EBOOK', 2021, 'Innovations in Science and Technology'),
('9780123456782', 5.0, 'Embark on a magical journey through a world of fantasy and adventure. Join the epic quest and unravel the mysteries of a mystical realm.', 'English', 8.99, 'EBOOK', 2020, 'Realm of Enchantment'),
('9780123456783', 3.0, 'Dive into the captivating story of love, loss, and redemption. Follow the intertwining lives of characters as they navigate the complexities of human emotions.', 'English', 12.99, 'EBOOK', 2019, 'Whispers of the Heart'),
('9780123456784', 4.0, 'Immerse yourself in a thrilling tale of espionage and intrigue. Join the undercover agents as they race against time to save the world from a global threat.', 'English', 9.79, 'EBOOK', 2022, 'Code Red: Undercover Operations'),
('9780123456785', 4.0, 'Discover the secrets of a hidden society and the adventures that unfold within. Uncover the truth and confront the challenges that lie ahead.', 'English', 14.99, 'EBOOK', 2021, 'Society of Shadows'),
('9780123456786', 2.0, 'Witness the evolution of humanity and the impact of technology on our lives. Delve into thought-provoking discussions on the future of our species.', 'English', 11.49, 'EBOOK', 2020, 'Future Horizons: Humanity and Technology'),
('9780123456787', 0, 'Experience the magic of nature and the beauty of our planet. Explore breathtaking landscapes and encounter the wonders of the natural world.', 'English', 13.99, 'EBOOK', 2018, 'Natural Wonders: A Journey Through Earth'),
('9780123456788', 0, 'Follow the thrilling escapades of a daring detective as he solves mysterious crimes. Unravel the clues and solve the case alongside the brilliant investigator.', 'English', 10.29, 'EBOOK', 2022, 'The Detective Chronicles: Unveiling Truths'),
('9780123456789', 3.0, 'Embark on a culinary adventure and explore the world of flavors. Discover mouthwatering recipes from various cultures and create your own culinary masterpieces.', 'English', 12.49, 'EBOOK', 2019, 'Global Gastronomy: A Culinary Journey'),
('9780123456790', 2.0, 'Journey through history and witness the rise and fall of empires. Explore the significant events and influential figures that have shaped our world.', 'English', 9.99, 'EBOOK', 2017, 'Chronicles of History: A Timeless Odyssey'),
('9780123456791', 5.0, 'Escape to distant realms and meet fantastical creatures in this epic adventure. Join the quest for the ultimate treasure and experience a world like no other.',  'English', 11.99, 'EBOOK', 2020, 'The Enchanted Quest: Legends of Mythos'),
('9780123456792', 2.0, 'Embark on a transformative journey of self-discovery and personal growth. Learn valuable insights and techniques to unlock your true potential.', 'English', 8.79, 'EBOOK', 2021, 'Awakening the Soul: A Path to Inner Wisdom'),
('9780123456793', 5.0, 'Dive into the depths of the cosmos and unravel the mysteries of the universe. Explore celestial phenomena and expand your understanding of the cosmos.', 'English', 13.49, 'EBOOK', 2019, 'Cosmic Odyssey: Exploring the Universe'),
('9780123456794', 4.0, 'Experience the trials and triumphs of characters in a world of magic and intrigue. Follow their adventures as they grapple with destiny and forge their paths.', 'English', 10.99, 'EBOOK', 2018, 'Sorcery and Destiny: A Magical Tale'),
('9780123456795', 1.0, 'Join a group of unlikely heroes as they embark on an epic quest to save their realm. Discover the power of friendship and bravery in the face of adversity.', 'English', 14.29, 'EBOOK', 2021, 'Heroes of Valor: Rise of the Fellowship'),
('9780123456796', 5.0, 'Dive into a world of espionage, danger, and intrigue. Follow the secret agent on a mission to save the world from an imminent threat.', 'English', 11.49, 'EBOOK', 2020, 'Agent X: Operation Shadow'),
('9780123456797', 3.0, 'Explore the wonders of science and its impact on our everyday lives. Learn about breakthroughs and innovations that have changed the course of humanity.', 'English', 12.99, 'EBOOK', 2022, 'Science Unveiled: A Journey into Discovery'),
('9780123456798', 5.0, 'Embark on an odyssey through the ages and witness the evolution of civilizations. Explore ancient cultures and their contributions to humanity.', 'English', 9.79, 'EBOOK', 2019, 'Chronicles of Antiquity: Echoes of the Past'),
('9780123456799', 0, 'Uncover the secrets of the mind and explore the depths of human consciousness. Delve into the realms of psychology and the mysteries of our thoughts.', 'English', 10.49, 'EBOOK', 2021, 'Mindscapes: A Journey Through Psychology'),
('9780123456800', 0, 'Discover the power of resilience and the triumph of the human spirit. Follow the inspiring stories of individuals who overcame adversities to achieve greatness.', 'English', 13.99, 'EBOOK', 2020, 'Rise from the Ashes: Inspiring Tales of Triumph')
ON DUPLICATE KEY update
  `isbn` = VALUES(`isbn`),
  `avg_rating` = VALUES(`avg_rating`),
  `description` = VALUES(`description`),
  `language` = VALUES(`language`),
  `price` = VALUES(`price`),
  `product_type` = VALUES(`product_type`),
  `publication_year` = VALUES(`publication_year`),
  `title` = VALUES(`title`);

insert into `cart`
(`products_count`, `products_sum`)
VALUES
('0', '0.00'),
('0', '0.00'),
('0', '0.00'),
('0', '0.00'),
('0', '0.00'),
('0', '0.00'),
('0', '0.00'),
('0', '0.00'),
('0', '0.00'),
('0', '0.00')
ON DUPLICATE KEY update
  `products_count` = VALUES(`products_count`),
  `products_sum` = VALUES(`products_sum`);

insert into `audiobook`
(`duration`, `file_format`, `file_size`, `number_of_available_copies`, `product_id`, `narrator_id`)
VALUES
(10.5, 'MP3', '100 MB', 1, 1, 1),
(8.2, 'MP3', '90 MB', 1, 2, 2),
(12.0, 'MP3', '110 MB', 1, 3, 3),
(9.7, 'MP3', '95 MB', 1, 4, 4),
(11.2, 'MP3', '120 MB', 1, 5, 5),
(10.8, 'MP3', '105 MB', 1, 6, 6),
(9.5, 'MP3', '92 MB', 1, 7, 7),
(13.1, 'MP3', '115 MB', 1, 8, 8),
(8.9, 'MP3', '88 MB', 1, 9, 9),
(10.3, 'MP3', '98 MB', 1, 10, 10),
(9.0, 'MP3', '85 MB', 1, 11, 11),
(11.5, 'MP3', '105 MB', 1, 12, 12),
(10.7, 'MP3', '100 MB', 1, 13, 13),
(12.2, 'MP3', '110 MB', 1, 14, 14),
(9.8, 'MP3', '94 MB', 1, 15, 15),
(11.0, 'MP3', '105 MB', 1, 16, 16),
(10.5, 'MP3', '100 MB', 1, 17, 17),
(10.8, 'MP3', '102 MB', 1, 18, 18),
(11.1, 'MP3', '107 MB', 1, 19, 19),
(10.6, 'MP3', '100 MB', 1, 20, 20)
ON DUPLICATE KEY update
  `duration` = VALUES(`duration`),
  `file_format` = VALUES(`file_format`),
  `file_size` = VALUES(`file_size`),
  `number_of_available_copies` = VALUES(`number_of_available_copies`),
  `narrator_id` = VALUES(`narrator_id`);

insert into audiobook_author
(audiobook_id, author_id)
values
    (1, 25),
    (2, 2),
    (3, 21),
    (4, 22),
    (5, 23),
    (6, 17),
    (7, 12),
    (8, 13),
    (9, 1),
    (10, 3),
    (11, 4),
    (12, 5),
    (13, 7),
    (14, 8),
    (15, 6),
    (16, 10),
    (17, 11),
    (18, 9),
    (19, 14),
    (20, 16)
ON DUPLICATE KEY update
    audiobook_id = VALUES(audiobook_id),
    author_id = VALUES(author_id);

insert into audiobook_category
(audiobook_id, category_id)
values
    (1, 18),
    (2, 2),
    (3, 15),
    (4, 19),
    (5, 20),
    (6, 17),
    (7, 12),
    (8, 13),
    (9, 1),
    (10, 3),
    (11, 4),
    (12, 5),
    (13, 7),
    (14, 8),
    (15, 6),
    (16, 10),
    (17, 11),
    (18, 9),
    (19, 14),
    (20, 16)
ON DUPLICATE KEY update
    audiobook_id = VALUES(audiobook_id),
    category_id = VALUES(category_id);

insert into `book`
(`number_of_available_copies`, `number_of_pages`, `number_of_total_copies`, `product_id`) VALUES
(10, 350, 500, 21),
(15, 200, 300, 22),
(8, 400, 600, 23),
(20, 250, 400, 24),
(12, 300, 450, 25),
(18, 280, 380, 26),
(10, 400, 550, 27),
(14, 350, 500, 28),
(10, 280, 400, 29),
(16, 220, 320, 30),
(10, 190, 280, 31),
(13, 290, 390, 32),
(10, 320, 450, 33),
(22, 260, 420, 34),
(14, 240, 360, 35),
(12, 300, 420, 36),
(15, 310, 440, 37),
(16, 270, 380, 38),
(18, 300, 430, 39),
(10, 280, 380, 40)
ON DUPLICATE KEY update
  `number_of_available_copies` = VALUES(`number_of_available_copies`),
  `number_of_pages` = VALUES(`number_of_pages`),
  `number_of_total_copies` = VALUES(`number_of_total_copies`);

insert into book_author
(book_id, author_id)
values
    (21, 25),
    (22, 24),
    (23, 21),
    (24, 22),
    (25, 23),
    (26, 11),
    (27, 12),
    (28, 13),
    (29, 1),
    (30, 5),
    (31, 3),
    (32, 4),
    (33, 7),
    (34, 8),
    (35, 6),
    (36, 18),
    (37, 17),
    (38, 9),
    (39, 14),
    (40, 16)
ON DUPLICATE KEY update
    book_id = VALUES(book_id),
    author_id = VALUES(author_id);

insert into book_category
(book_id, category_id)
values
    (21, 18),
    (22, 2),
    (25, 15),
    (26, 19),
    (23, 20),
    (24, 17),
    (27, 12),
    (28, 13),
    (39, 1),
    (40, 3),
    (35, 4),
    (33, 5),
    (32, 7),
    (34, 8),
    (31, 6),
    (36, 10),
    (38, 11),
    (37, 9),
    (29, 14),
    (30, 16)
ON DUPLICATE KEY update
    book_id = VALUES(book_id),
    category_id = VALUES(category_id);

insert into ebook
(file_format, file_size, number_of_pages, number_of_available_copies, product_id)
values
    ('PDF', '10 MB', 300, 1, 41),
    ('EPUB', '8 MB', 250, 1, 42),
    ('PDF', '12 MB', 400, 1, 43),
    ('EPUB', '9 MB', 280, 1, 44),
    ('PDF', '14 MB', 320, 1, 45),
    ('EPUB', '11 MB', 350, 1, 46),
    ('PDF', '13 MB', 380, 1, 47),
    ('EPUB', '10 MB', 290, 1, 48),
    ('PDF', '12 MB', 360, 1, 49),
    ('EPUB', '9 MB', 320, 1, 50),
    ('PDF', '11 MB', 340, 1, 51),
    ('EPUB', '8 MB', 270, 1, 52),
    ('PDF', '13 MB', 380, 1, 53),
    ('EPUB', '10 MB', 310, 1, 54),
    ('PDF', '14 MB', 340, 1, 55),
    ('EPUB', '11 MB', 330, 1, 56),
    ('PDF', '12 MB', 360, 1, 57),
    ('EPUB', '9 MB', 300, 1, 58),
    ('PDF', '10 MB', 280, 1, 59),
    ('EPUB', '13 MB', 320, 1, 60)
ON DUPLICATE KEY update
    file_format = VALUES(file_format),
    file_size = VALUES(file_size),
    number_of_pages = VALUES(number_of_pages);

insert into ebook_author
(ebook_id, author_id)
values
    (41, 25),
    (42, 24),
    (43, 21),
    (44, 22),
    (45, 23),
    (46, 11),
    (47, 12),
    (48, 13),
    (49, 1),
    (50, 5),
    (51, 3),
    (52, 4),
    (53, 7),
    (54, 8),
    (55, 6),
    (56, 18),
    (57, 17),
    (58, 9),
    (59, 14),
    (60, 16)
ON DUPLICATE KEY update
    ebook_id = VALUES(ebook_id),
    author_id = VALUES(author_id);

insert into ebook_category
(ebook_id, category_id)
values
    (41, 18),
    (42, 2),
    (45, 15),
    (46, 19),
    (43, 20),
    (44, 17),
    (47, 12),
    (48, 13),
    (59, 1),
    (50, 3),
    (55, 4),
    (53, 5),
    (52, 7),
    (54, 8),
    (51, 6),
    (56, 10),
    (58, 11),
    (57, 9),
    (49, 14),
    (60, 16)
ON DUPLICATE KEY update
    ebook_id = VALUES(ebook_id),
    category_id = VALUES(category_id);

insert into role
(name)
values
('ROLE_ADMIN'),
('ROLE_LIBRARIAN'),
('ROLE_USER')
ON DUPLICATE KEY update
    name = VALUES(name);

insert into user_profile
(address, birth_date, city, country, email, first_name, last_name, phone_number, mfa_enabled, profile_picture_url)
values
('Admin Street', '1990-03-15', 'Rome', 'Italy', 'admin@gmail.com', 'admin', 'admin', '0887080808', 1, null),
('Marto Street', '1992-04-01', 'Sofia', 'Bulgaria', 'martin@gmail.com', 'Martin', 'Ivanov', '0887070707', 1, null),
('Vanka Street', '1980-05-05', 'Varna', 'Bulgaria', 'ivan@gmail.com', 'Ivan', 'Petrov', '0887060606', 0, null),
('Pesho Street', '1995-06-12', 'Plovdiv', 'Bulgaria', 'petar@gmail.com', 'Petar', 'Dimitrov', '0887050505', 0, null),
('James Street', '1996-07-25', 'Los Angeles', 'USA', 'james@gmail.com', 'James', 'Owen', '0887040404', 1, null),
('Susan Street', '2000-08-22', 'Munich', 'Germany', 'susan@gmail.com', 'Susan', 'Gilbert', '0887030303', 0, null),
('Nash Street', '2004-09-11', 'Sydney', 'Australia', 'nash@gmail.com', 'Nash', 'Underwood', '0887020202', 0, null),
('Sammy Street', '2002-11-03', 'Aberdeen', 'England', 'sammy@gmail.com', 'Sammy', 'Wolf', '0887010101', 0, null),
('Kara Street', '1978-12-13', 'Madrid', 'Spain', 'kara@gmail.com', 'Kara', 'FEMALE', '0887090909', 0, null),
('Devin Street', '1965-10-21', 'Brasília', 'Brazil', 'devin@gmail.com', 'Devin', 'Snyder', '0887011112', 0, null)
ON DUPLICATE KEY update
    address = VALUES(address),
    birth_date = VALUES(birth_date),
    city = VALUES(city),
    country = VALUES(country),
    email = VALUES(email),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    phone_number = VALUES(phone_number),
    mfa_enabled = VALUES(mfa_enabled),
    profile_picture_url = VALUES(profile_picture_url);

insert into user
(address, birth_date, city, country, email, first_name, gender, is_active, is_email_verified, last_name, password, phone_number, mfa_enabled, cart_id, user_profile_id)
values
('Admin Street', '1990-03-15', 'Rome', 'Italy', 'admin@gmail.com', 'admin', 'MALE', 1, 1, 'admin', '$2a$12$GSZ.Vv0cIP9GcUgaSJeuP.o0ma17fKyGF2Z9CCYWM8CDCQpYTnL7G', '0887080808', 1, 1, 1),
('Marto Street', '1992-04-01', 'Sofia', 'Bulgaria', 'martin@gmail.com', 'Martin', 'MALE', 1, 1, 'Ivanov', '$2a$12$OxRCPzU0L2eckMr1Pe2AM.9.hYW9bY8d6o8e2kA6IQYe4B5PlxC/y', '0887070707', 1, 2, 2),
('Vanka Street', '1980-05-05', 'Varna', 'Bulgaria', 'ivan@gmail.com', 'Ivan', 'MALE', 0, 0, 'Petrov', '$2a$12$Q6lvaawQzohfB.9R9SVJMeLsZJrnazVICIHBtVzPx3PqejaIQntFm', '0887060606', 0, 3, 3),
('Pesho Street', '1995-06-12', 'Plovdiv', 'Bulgaria', 'petar@gmail.com', 'Petar', 'NON_BINARY', 0, 0, 'Dimitrov', '$2a$12$TAioRD/5LC8svR60kIHfLOA4l/uMfJQpwFt8tbtt36nuNuGUOYyEW', '0887050505', 0, 4, 4),
('James Street', '1996-07-25', 'Los Angeles', 'USA', 'james@gmail.com', 'James', 'MALE', 1, 1, 'Owen', '$2a$12$dugR24f/aCOiHchL.avfNOHqYIc4YAlQIK410NlzKwb9IuMKTgUdO', '0887040404', 0, 5, 5),
('Susan Street', '2000-08-22', 'Munich', 'Germany', 'susan@gmail.com', 'Susan', 'FEMALE', 1, 1, 'Gilbert', '$2a$12$1JS4RwBFuKnm1.fQ4SFRPOjNYdfHgtq6iwlprpz7tppYLGJ2zpkAC', '0887030303', 0, 6, 6),
('Nash Street', '2004-09-11', 'Sydney', 'Australia', 'nash@gmail.com', 'Nash', 'NON_BINARY', 1, 1, 'Underwood', '$2a$12$6v7VDV6EH20k4suJHmzuEeCpol9mi37V26Sne5uD9M44dQEWqEJTa', '0887020202', 0, 7, 7),
('Sammy Street', '2002-11-03', 'Aberdeen', 'England', 'sammy@gmail.com', 'Sammy', 'PREFER_NOT_TO_SAY', 0, 0, 'Wolf', '$2a$12$upOq8lr3X/cXES34pJ9ZXuTARQWOwIJ/SX8fZeogYzpGcCFVJE8Xi', '0887010101', 0, 8, 8),
('Kara Street', '1978-12-13', 'Madrid', 'Spain', 'kara@gmail.com', 'Kara', 'FEMALE', 0, 0, 'Dixon', '$2a$12$B2Hfdi38ZBwN9.jLdaHo5OM1g5nPUoFqnHCIwp1y65fHXvdiZgLWm', '0887090909', 0, 9, 9),
('Devin Street', '1965-10-21', 'Brasília', 'Brazil', 'devin@gmail.com', 'Devin', 'PREFER_NOT_TO_SAY', 1, 1, 'Snyder', '$2a$12$dPxNQqWmuC6NlUoSWSazde4VXK3zbCY.7JZ4pWv19uPPdomSOK2BW', '0887011112', 0, 10, 10)
ON DUPLICATE KEY update
    address = VALUES(address),
    birth_date = VALUES(birth_date),
    city = VALUES(city),
    country = VALUES(country),
    email = VALUES(email),
    first_name = VALUES(first_name),
    gender = VALUES(gender),
    is_active = VALUES(is_active),
    is_email_verified = VALUES(is_email_verified),
    last_name = VALUES(last_name),
    password = VALUES(password),
    phone_number = VALUES(phone_number),
    mfa_enabled = VALUES(mfa_enabled),
    cart_id = VALUES(cart_id),
    user_profile_id = VALUES(user_profile_id);

insert into user_role
(user_id, role_id)
values
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 3),
(3, 3),
(4, 3),
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(9, 3),
(10, 3)
ON DUPLICATE KEY update
    user_id = VALUES(user_id),
    role_id = VALUES(role_id);

insert into token_type (name)
values
    ('Confirmation'),
    ('Verification'),
    ('Reset')
ON DUPLICATE KEY update
    name = VALUES(name);

insert into `comment_review`
(`comment`, `posted_at`, `rating`, `updated_at`, `product_id`, `user_id`)
VALUES
('Good book', '2023-09-21 10:15:00', 5, '2023-09-21 10:15:00', 1, 1),
('Interesting read', '2023-09-22 11:30:00', 4, '2023-09-22 11:30:00', 2, 2),
('Informative content', '2023-09-23 09:45:00', 4, '2023-09-23 09:45:00', 3, 3),
('Fascinating content', '2023-09-24 13:20:00', 5, '2023-09-24 13:20:00', 4, 4),
('Loved it!', '2023-09-25 14:10:00', 5, '2023-09-25 14:10:00', 5, 5),
('Great story', '2023-09-26 12:45:00', 4, '2023-09-26 12:45:00', 6, 6),
('Highly recommended', '2023-09-27 15:00:00', 5, '2023-09-27 15:00:00', 7, 7),
('Could not put it down', '2023-09-28 11:55:00', 4, '2023-09-28 11:55:00', 8, 8),
('Well written', '2023-09-29 09:30:00', 4, '2023-09-29 09:30:00', 9, 9),
('Average book', '2023-09-30 10:20:00', 3, '2023-09-30 10:20:00', 10, 10),
('Disappointing', '2023-10-01 12:00:00', 2, '2023-10-01 12:00:00', 11, 1),
('Must-read', '2023-10-02 14:15:00', 5, '2023-10-02 14:15:00', 12, 2),
('Informative', '2023-10-03 15:25:00', 4, '2023-10-03 15:25:00', 13, 3),
('Engaging', '2023-10-04 09:50:00', 5, '2023-10-04 09:50:00', 14, 4),
('A bit slow-paced', '2023-10-05 11:35:00', 3, '2023-10-05 11:35:00', 15, 5),
('Captivating plot', '2023-10-06 13:40:00', 4, '2023-10-06 13:40:00', 16, 6),
('Well-researched', '2023-10-07 14:55:00', 4, '2023-10-07 14:55:00', 17, 7),
('Immersive experience', '2023-10-10 12:25:00', 5, '2023-10-10 12:25:00', 20, 10),
('A classic', '2023-10-11 13:30:00', 5, '2023-10-11 13:30:00', 21, 1),
('Overrated', '2023-10-12 14:40:00', 2, '2023-10-12 14:40:00', 22, 2),
('Could not connect with the characters', '2023-10-13 09:55:00', 3, '2023-10-13 09:55:00', 23, 3),
('Enjoyed it', '2023-10-14 10:45:00', 4, '2023-10-14 10:45:00', 24, 4),
('Intriguing storyline', '2023-10-15 11:50:00', 4, '2023-10-15 11:50:00', 25, 5),
('Well-crafted characters', '2023-10-16 13:00:00', 5, '2023-10-16 13:00:00', 26, 6),
('Thoroughly enjoyed it', '2023-10-19 09:30:00', 2, '2023-10-19 09:30:00', 29, 9),
('Unique plot', '2023-10-20 10:40:00', 1, '2023-10-20 10:40:00', 30, 10),
('Could be better', '2023-10-21 11:50:00', 3, '2023-10-21 11:50:00', 31, 1),
('A good read', '2023-10-22 13:00:00', 4, '2023-10-22 13:00:00', 32, 2),
('Page-turner', '2023-10-23 14:10:00', 4, '2023-10-23 14:10:00', 33, 3),
('Riveting', '2023-10-25 09:30:00', 5, '2023-10-25 09:30:00', 35, 5),
('A bit predictable', '2023-10-26 10:40:00', 3, '2023-10-26 10:40:00', 36, 6),
('Couldn''t get into it', '2023-10-27 11:50:00', 2, '2023-10-27 11:50:00', 37, 7),
('Solid storyline', '2023-10-28 13:00:00', 4, '2023-10-28 13:00:00', 38, 8),
('Engrossing', '2023-10-29 14:10:00', 4, '2023-10-29 14:10:00', 39, 9),
('Not my cup of tea', '2023-10-30 15:20:00', 2, '2023-10-30 15:20:00', 40, 10),
('Thought-provoking', '2023-10-31 09:30:00', 5, '2023-10-31 09:30:00', 41, 1),
('A classic tale', '2023-11-01 10:40:00', 5, '2023-11-01 10:40:00', 42, 2),
('Plot lacked depth', '2023-11-02 11:50:00', 3, '2023-11-02 11:50:00', 43, 3),
('Worth the read', '2023-11-03 13:00:00', 4, '2023-11-03 13:00:00', 44, 4),
('Entertaining', '2023-11-04 14:10:00', 4, '2023-11-04 14:10:00', 45, 5),
('Disliked the ending', '2023-11-05 15:20:00', 2, '2023-11-05 15:20:00', 46, 6),
('A bit slow', '2023-11-08 11:50:00', 3, '2023-11-08 11:50:00', 49, 9),
('Couldn''t relate', '2023-11-09 13:00:00', 2, '2023-11-09 13:00:00', 50, 10),
('Must-have', '2023-11-10 14:10:00', 5, '2023-11-10 14:10:00', 51, 1),
('Lacked substance', '2023-11-11 15:20:00', 2, '2023-11-11 15:20:00', 52, 2),
('A literary gem', '2023-11-12 09:30:00', 5, '2023-11-12 09:30:00', 53, 3),
('Fast-paced', '2023-11-13 10:40:00', 4, '2023-11-13 10:40:00', 54, 4),
('Couldn''t finish', '2023-11-14 11:50:00', 1, '2023-11-14 11:50:00', 55, 5),
('Hooked from the start', '2023-11-15 13:00:00', 5, '2023-11-15 13:00:00', 56, 6),
('Average at best', '2023-11-16 14:10:00', 3, '2023-11-16 14:10:00', 57, 7),
('Great character development', '2023-11-17 15:20:00', 5, '2023-11-17 15:20:00', 58, 8)
ON DUPLICATE KEY update
  `comment` = VALUES(`comment`),
  `posted_at` = VALUES(`posted_at`),
  `rating` = VALUES(`rating`),
  `updated_at` = VALUES(`updated_at`);