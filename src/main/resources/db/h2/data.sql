INSERT INTO vets VALUES (default, 'James', 'Carter');
INSERT INTO vets VALUES (default, 'Helen', 'Leary');
INSERT INTO vets VALUES (default, 'Linda', 'Douglas');
INSERT INTO vets VALUES (default, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (default, 'Henry', 'Stevens');
INSERT INTO vets VALUES (default, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (default, 'radiology');
INSERT INTO specialties VALUES (default, 'surgery');
INSERT INTO specialties VALUES (default, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (default, 'cat');
INSERT INTO types VALUES (default, 'dog');
INSERT INTO types VALUES (default, 'lizard');
INSERT INTO types VALUES (default, 'snake');
INSERT INTO types VALUES (default, 'bird');
INSERT INTO types VALUES (default, 'hamster');

INSERT INTO owners VALUES (default, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO owners VALUES (default, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO owners VALUES (default, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO owners VALUES (default, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO owners VALUES (default, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO owners VALUES (default, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO owners VALUES (default, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO owners VALUES (default, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO owners VALUES (default, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO owners VALUES (default, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Leo', '2010-09-07', 1, 1, NULL, '985141001234567', 'Orange', 'Tabby', TRUE, 4.50, NULL, 'MALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Basil', '2012-08-06', 6, 2, NULL, '985141001234568', 'Brown', 'Golden Hamster', TRUE, 0.15, NULL, 'MALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Rosy', '2011-04-17', 2, 3, NULL, '985141001234569', NULL, NULL, TRUE, 8.20, NULL, 'FEMALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Jewel', '2010-03-07', 2, 3, NULL, '985141001234570', NULL, NULL, TRUE, 5.00, NULL, 'FEMALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Iggy', '2010-11-30', 3, 4, NULL, '985141001234571', NULL, NULL, TRUE, 1.20, NULL, 'UNKNOWN');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('George', '2010-01-20', 4, 5, NULL, NULL, NULL, NULL, TRUE, 2.50, NULL, 'MALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Samantha', '2012-09-04', 1, 6, NULL, NULL, NULL, NULL, TRUE, 3.80, NULL, 'FEMALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Max', '2012-09-04', 1, 6, NULL, NULL, NULL, NULL, TRUE, 4.00, NULL, 'MALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Lucky', '2011-08-06', 5, 7, NULL, NULL, NULL, NULL, TRUE, 0.50, NULL, 'MALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Mulligan', '2007-02-24', 2, 8, NULL, NULL, NULL, NULL, TRUE, 12.00, NULL, 'MALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Freddy', '2010-03-09', 5, 9, NULL, NULL, NULL, NULL, TRUE, 0.30, NULL, 'UNKNOWN');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Lucky', '2010-06-24', 2, 10, NULL, NULL, NULL, NULL, TRUE, 7.50, NULL, 'FEMALE');
INSERT INTO pets (name, birth_date, type_id, owner_id, photo_url, microchip_id, color, breed, active, weight, notes, gender) VALUES ('Sly', '2012-06-08', 1, 10, NULL, NULL, NULL, NULL, TRUE, 3.50, NULL, 'MALE');

INSERT INTO visits (pet_id, visit_date, description, vet_id) VALUES (7, '2013-01-01', 'rabies shot', 4);
INSERT INTO visits (pet_id, visit_date, description, vet_id) VALUES (8, '2013-01-02', 'rabies shot', 2);
INSERT INTO visits (pet_id, visit_date, description, vet_id) VALUES (8, '2013-01-03', 'neutered', 3);
INSERT INTO visits (pet_id, visit_date, description, vet_id) VALUES (7, '2013-01-04', 'spayed', NULL);

INSERT INTO weight_records (weight, measure_date, pet_id) VALUES (4.20, '2012-09-07', 1);
INSERT INTO weight_records (weight, measure_date, pet_id) VALUES (4.35, '2013-03-07', 1);
INSERT INTO weight_records (weight, measure_date, pet_id) VALUES (4.50, '2013-09-07', 1);
INSERT INTO weight_records (weight, measure_date, pet_id) VALUES (7.80, '2012-04-17', 3);
INSERT INTO weight_records (weight, measure_date, pet_id) VALUES (8.00, '2012-10-17', 3);
INSERT INTO weight_records (weight, measure_date, pet_id) VALUES (8.20, '2013-04-17', 3);
