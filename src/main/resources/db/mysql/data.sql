INSERT IGNORE INTO vets VALUES (1, 'James', 'Carter');
INSERT IGNORE INTO vets VALUES (2, 'Helen', 'Leary');
INSERT IGNORE INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT IGNORE INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT IGNORE INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT IGNORE INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT IGNORE INTO specialties VALUES (1, 'radiology');
INSERT IGNORE INTO specialties VALUES (2, 'surgery');
INSERT IGNORE INTO specialties VALUES (3, 'dentistry');

INSERT IGNORE INTO vet_specialties VALUES (2, 1);
INSERT IGNORE INTO vet_specialties VALUES (3, 2);
INSERT IGNORE INTO vet_specialties VALUES (3, 3);
INSERT IGNORE INTO vet_specialties VALUES (4, 2);
INSERT IGNORE INTO vet_specialties VALUES (5, 1);

INSERT IGNORE INTO types VALUES (1, 'cat');
INSERT IGNORE INTO types VALUES (2, 'dog');
INSERT IGNORE INTO types VALUES (3, 'lizard');
INSERT IGNORE INTO types VALUES (4, 'snake');
INSERT IGNORE INTO types VALUES (5, 'bird');
INSERT IGNORE INTO types VALUES (6, 'hamster');

INSERT IGNORE INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT IGNORE INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT IGNORE INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT IGNORE INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT IGNORE INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT IGNORE INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT IGNORE INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT IGNORE INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT IGNORE INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT IGNORE INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (1, 'Leo', '2000-09-07', 1, 1, NULL, '985141001234567', 'Orange', 'Tabby', TRUE, 4.50, NULL, 'MALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (2, 'Basil', '2002-08-06', 6, 2, NULL, '985141001234568', 'Brown', 'Golden Hamster', TRUE, 0.15, NULL, 'MALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (3, 'Rosy', '2001-04-17', 2, 3, NULL, '985141001234569', NULL, NULL, TRUE, 8.20, NULL, 'FEMALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (4, 'Jewel', '2000-03-07', 2, 3, NULL, '985141001234570', NULL, NULL, TRUE, 5.00, NULL, 'FEMALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (5, 'Iggy', '2000-11-30', 3, 4, NULL, '985141001234571', NULL, NULL, TRUE, 1.20, NULL, 'UNKNOWN');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (6, 'George', '2000-01-20', 4, 5, NULL, NULL, NULL, NULL, TRUE, 2.50, NULL, 'MALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (7, 'Samantha', '1995-09-04', 1, 6, NULL, NULL, NULL, NULL, TRUE, 3.80, NULL, 'FEMALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (8, 'Max', '1995-09-04', 1, 6, NULL, NULL, NULL, NULL, TRUE, 4.00, NULL, 'MALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (9, 'Lucky', '1999-08-06', 5, 7, NULL, NULL, NULL, NULL, TRUE, 0.50, NULL, 'MALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (10, 'Mulligan', '1997-02-24', 2, 8, NULL, NULL, NULL, NULL, TRUE, 12.00, NULL, 'MALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (11, 'Freddy', '2000-03-09', 5, 9, NULL, NULL, NULL, NULL, TRUE, 0.30, NULL, 'UNKNOWN');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (12, 'Lucky', '2000-06-24', 2, 10, NULL, NULL, NULL, NULL, TRUE, 7.50, NULL, 'FEMALE');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES (13, 'Sly', '2002-06-08', 1, 10, NULL, NULL, NULL, NULL, TRUE, 3.50, NULL, 'MALE');

INSERT IGNORE INTO visits (id, pet_id, visit_date, description, vet_id) VALUES (1, 7, '2010-03-04', 'rabies shot', 4);
INSERT IGNORE INTO visits (id, pet_id, visit_date, description, vet_id) VALUES (2, 8, '2011-03-04', 'rabies shot', 2);
INSERT IGNORE INTO visits (id, pet_id, visit_date, description, vet_id) VALUES (3, 8, '2009-06-04', 'neutered', 3);
INSERT IGNORE INTO visits (id, pet_id, visit_date, description, vet_id) VALUES (4, 7, '2008-09-04', 'spayed', NULL);

INSERT IGNORE INTO weight_records (id, weight, measure_date, pet_id) VALUES (1, 4.20, '2012-09-07', 1);
INSERT IGNORE INTO weight_records (id, weight, measure_date, pet_id) VALUES (2, 4.35, '2013-03-07', 1);
INSERT IGNORE INTO weight_records (id, weight, measure_date, pet_id) VALUES (3, 4.50, '2013-09-07', 1);
INSERT IGNORE INTO weight_records (id, weight, measure_date, pet_id) VALUES (4, 7.80, '2012-04-17', 3);
INSERT IGNORE INTO weight_records (id, weight, measure_date, pet_id) VALUES (5, 8.00, '2012-10-17', 3);
INSERT IGNORE INTO weight_records (id, weight, measure_date, pet_id) VALUES (6, 8.20, '2013-04-17', 3);
