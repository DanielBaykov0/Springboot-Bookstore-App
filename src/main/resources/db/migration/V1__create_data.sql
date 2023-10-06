create TABLE IF NOT EXISTS `country` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_llidyp77h6xkeokpbmoy710d4` (`name`)
);

create TABLE IF NOT EXISTS `city` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `country_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrpd7j1p7yxr784adkx4pyepba` (`country_id`),
  CONSTRAINT `FKrpd7j1p7yxr784adkx4pyepba` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
);

create TABLE IF NOT EXISTS `author` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `birth_date` date DEFAULT NULL,
  `death_date` date DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `is_alive` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `city_id` bigint DEFAULT NULL,
  `country_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp4rqwnqf9u46cwdmcw74b35s` (`city_id`),
  KEY `FKq9nw1djh062h5omgdo26u0gjf` (`country_id`),
  CONSTRAINT `FKp4rqwnqf9u46cwdmcw74b35s` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `FKq9nw1djh062h5omgdo26u0gjf` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
);

create TABLE IF NOT EXISTS `narrator` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `biography` varchar(255) NOT NULL,
  `birth_date` date DEFAULT NULL,
  `death_date` date DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `is_alive` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `city_id` bigint DEFAULT NULL,
  `country_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5cqu2h42yyskc0vke549yov6u` (`city_id`),
  KEY `FKhhp1atf5b3xoh43w9dod7v5y9` (`country_id`),
  CONSTRAINT `FK5cqu2h42yyskc0vke549yov6u` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `FKhhp1atf5b3xoh43w9dod7v5y9` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
);

create TABLE IF NOT EXISTS `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_46ccwnsi9409t36lurvtyljak` (`name`)
);

create TABLE IF NOT EXISTS `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `isbn` varchar(255) NOT NULL,
  `avg_rating` double NOT NULL,
  `description` varchar(255) NOT NULL,
  `language` varchar(255) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `product_type` enum('AUDIOBOOK','BOOK','EBOOK') NOT NULL,
  `publication_year` int NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_eh5ac8cccnogeps5ockuiwau5` (`isbn`)
);

create TABLE IF NOT EXISTS `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_price` decimal(38,2) DEFAULT NULL,
  `products_count` bigint DEFAULT NULL,
  `products_sum` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

create TABLE IF NOT EXISTS `cart_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `added_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjcyd5wv4igqnw413rgxbfu4nv` (`product_id`),
  CONSTRAINT `FKjcyd5wv4igqnw413rgxbfu4nv` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
);

create TABLE IF NOT EXISTS `cart_product` (
  `cart_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `products_key` bigint NOT NULL,
  PRIMARY KEY (`cart_id`,`products_key`),
  KEY `FK3qx236g9a7ndw5b1atsx87e8r` (`product_id`),
  KEY `FKdrx41fmbd0ramdbxl7wdm30ex` (`products_key`),
  CONSTRAINT `FK3qx236g9a7ndw5b1atsx87e8r` FOREIGN KEY (`product_id`) REFERENCES `cart_item` (`id`),
  CONSTRAINT `FKdrx41fmbd0ramdbxl7wdm30ex` FOREIGN KEY (`products_key`) REFERENCES `product` (`id`),
  CONSTRAINT `FKlv5x4iresnv4xspvomrwd8ej9` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`)
);

create TABLE IF NOT EXISTS `audiobook` (
  `duration` decimal(38,2) NOT NULL,
  `file_format` varchar(255) NOT NULL,
  `file_size` varchar(255) NOT NULL,
  `number_of_available_copies` int NOT NULL DEFAULT 1,
  `product_id` bigint NOT NULL,
  `narrator_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FKclukf5pbb3pt54uvnrbgh2431` (`narrator_id`),
  CONSTRAINT `FKa3p6bsrwiejonc41xlkfvw8x5` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON delete CASCADE,
  CONSTRAINT `FKclukf5pbb3pt54uvnrbgh2431` FOREIGN KEY (`narrator_id`) REFERENCES `narrator` (`id`)
);

create TABLE IF NOT EXISTS `audiobook_author` (
  `audiobook_id` bigint NOT NULL,
  `author_id` bigint NOT NULL,
  KEY `FK8ixsw55wwc2hcqnevrkcu0f25` (`author_id`),
  KEY `FKi5f3st72tcl6hifb8jix9kwxm` (`audiobook_id`),
  CONSTRAINT `FK8ixsw55wwc2hcqnevrkcu0f25` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)  ON delete CASCADE,
  CONSTRAINT `FKi5f3st72tcl6hifb8jix9kwxm` FOREIGN KEY (`audiobook_id`) REFERENCES `audiobook` (`product_id`)  ON delete CASCADE
);

create TABLE IF NOT EXISTS `audiobook_category` (
  `audiobook_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  KEY `FKsqqnb3xqpn3t1gkott1j2uw1i` (`category_id`),
  KEY `FK4vdcd66krd79iuj4huyq2poxc` (`audiobook_id`),
  CONSTRAINT `FK4vdcd66krd79iuj4huyq2poxc` FOREIGN KEY (`audiobook_id`) REFERENCES `audiobook` (`product_id`) ON delete CASCADE,
  CONSTRAINT `FKsqqnb3xqpn3t1gkott1j2uw1i` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON delete CASCADE
);

create TABLE IF NOT EXISTS `book` (
  `number_of_available_copies` int NOT NULL,
  `number_of_pages` int NOT NULL,
  `number_of_total_copies` int NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`),
  CONSTRAINT `FK8cjf4cjanicu58p2l5t8d9xvu` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON delete CASCADE
);

create TABLE IF NOT EXISTS `book_author` (
  `book_id` bigint NOT NULL,
  `author_id` bigint NOT NULL,
  KEY `FKbjqhp85wjv8vpr0beygh6jsgo` (`author_id`),
  KEY `FKhwgu59n9o80xv75plf9ggj7xn` (`book_id`),
  CONSTRAINT `FKbjqhp85wjv8vpr0beygh6jsgo` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON delete CASCADE,
  CONSTRAINT `FKhwgu59n9o80xv75plf9ggj7xn` FOREIGN KEY (`book_id`) REFERENCES `book` (`product_id`) ON delete CASCADE
);

create TABLE IF NOT EXISTS `book_category` (
  `book_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  KEY `FKam8llderp40mvbbwceqpu6l2s` (`category_id`),
  KEY `FKnyegcbpvce2mnmg26h0i856fd` (`book_id`),
  CONSTRAINT `FKam8llderp40mvbbwceqpu6l2s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON delete CASCADE,
  CONSTRAINT `FKnyegcbpvce2mnmg26h0i856fd` FOREIGN KEY (`book_id`) REFERENCES `book` (`product_id`) ON delete CASCADE
);

create TABLE IF NOT EXISTS `ebook` (
  `file_format` varchar(255) NOT NULL,
  `file_size` varchar(255) NOT NULL,
  `number_of_pages` int NOT NULL,
  `number_of_available_copies` int NOT NULL DEFAULT 1,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`),
  CONSTRAINT `FKn1ud7ccwcin37now7pc5rc0v6` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON delete CASCADE
);

create TABLE IF NOT EXISTS `ebook_author` (
  `ebook_id` bigint NOT NULL,
  `author_id` bigint NOT NULL,
  KEY `FK9d5flvio63u28qduky611547j` (`author_id`),
  KEY `FK5xovtvxbxuc0jm43frge83stn` (`ebook_id`),
  CONSTRAINT `FK5xovtvxbxuc0jm43frge83stn` FOREIGN KEY (`ebook_id`) REFERENCES `ebook` (`product_id`) ON delete CASCADE,
  CONSTRAINT `FK9d5flvio63u28qduky611547j` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON delete CASCADE
);

create TABLE IF NOT EXISTS `ebook_category` (
  `ebook_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  KEY `FKaed7hvmorcl7f3q5wrifugqqn` (`category_id`),
  KEY `FK3nmy921uy59q15a1svklgsnrt` (`ebook_id`),
  CONSTRAINT `FK3nmy921uy59q15a1svklgsnrt` FOREIGN KEY (`ebook_id`) REFERENCES `ebook` (`product_id`) ON delete CASCADE,
  CONSTRAINT `FKaed7hvmorcl7f3q5wrifugqqn` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON delete CASCADE
);

create TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` enum('ROLE_ADMIN','ROLE_LIBRARIAN','ROLE_USER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
);

create TABLE IF NOT EXISTS `user_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `birth_date` date NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `mfa_enabled` bit(1) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `profile_picture_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_tcks72p02h4dp13cbhxne17ad` (`email`)
);

create TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `birth_date` date NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `gender` enum('FEMALE','MALE','NON_BINARY','PREFER_NOT_TO_SAY') DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `is_email_verified` bit(1) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `mfa_enabled` bit(1) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `cart_id` bigint DEFAULT NULL,
  `user_profile_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_47dq8urpj337d3o65l3fsjph3` (`cart_id`),
  UNIQUE KEY `UK_2ek1mbe9ojg3q7p83vusnrj15` (`user_profile_id`),
  CONSTRAINT `FKjjes1f6tjhqns02054ou51m00` FOREIGN KEY (`user_profile_id`) REFERENCES `user_profile` (`id`),
  CONSTRAINT `FKtqa69bib34k2c0jhe7afqsao6` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`)
);

create TABLE IF NOT EXISTS `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
);

create TABLE IF NOT EXISTS `user_product_association` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
   PRIMARY KEY (`id`,`product_id`,`user_id`),
   KEY `FK9tn0nr86oi282o7debx2e2x0k` (`user_id`),
   KEY `FKcwojir11bw80kg194lr41vlik` (`product_id`),
   CONSTRAINT `FK9tn0nr86oi282o7debx2e2x0k` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`id`),
   CONSTRAINT `FKcwojir11bw80kg194lr41vlik` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
);


create TABLE IF NOT EXISTS `token_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` enum('CONFIRMATION','VERIFICATION','RESET') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6nvf2gjbgdyker1wtbc6u9ed1` (`name`)
 );

create TABLE IF NOT EXISTS `token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `confirmed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `token_type_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8b0fx0cdgxhv1rxjpwe0iv77i` (`token_type_id`),
  KEY `FKe32ek7ixanakfqsdaokm4q9y2` (`user_id`),
  CONSTRAINT `FK8b0fx0cdgxhv1rxjpwe0iv77i` FOREIGN KEY (`token_type_id`) REFERENCES `token_type` (`id`),
  CONSTRAINT `FKe32ek7ixanakfqsdaokm4q9y2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

create TABLE IF NOT EXISTS `comment_review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `posted_at` datetime(6) NOT NULL,
  `rating` int DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6t9f5j8tm6lxdvikjor4hk2dk` (`product_id`),
  KEY `FKoal5s5wil571v8gax667qovnf` (`user_id`),
  CONSTRAINT `FK6t9f5j8tm6lxdvikjor4hk2dk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKoal5s5wil571v8gax667qovnf` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

create TABLE IF NOT EXISTS `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `payment_intent_id` varchar(255) NOT NULL,
  `contact_phone_number` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `delivered_on` datetime(6) DEFAULT NULL,
  `modified_on` datetime(6) DEFAULT NULL,
  `price` decimal(38,2) NOT NULL,
  `status` enum('CANCELED','CHECKOUT','COMPLETED','NEW','SHIPMENT','WAITING_FOR_SHIPMENT') NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcpl0mjoeqhxvgeeeq5piwpd3i` (`user_id`),
  CONSTRAINT `FKcpl0mjoeqhxvgeeeq5piwpd3i` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

create TABLE IF NOT EXISTS `order_product` (
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  KEY `FKhnfgqyjx3i80qoymrssls3kno` (`product_id`),
  KEY `FKm6igrp4lwucj1me05axmv885c` (`order_id`),
  CONSTRAINT `FKhnfgqyjx3i80qoymrssls3kno` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKm6igrp4lwucj1me05axmv885c` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
);