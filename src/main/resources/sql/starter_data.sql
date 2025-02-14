-- Insert Users (General)
INSERT INTO users (id, username, password, email, phone) VALUES
                                                            (1, 'citizen', '$2a$10$rbOPqiEK4UndJgx.fenFP.WDMwIl4qI/L.oE87ceLT3MPNplp5ef2', 'citizen@example.com', '6943418200'),
                                                            (2, 'vet', '$2a$10$Nwp5mzjHuViK1f/UaifIIed1kuDeDmbaWwdcqpcrY3yg6cAzRDFL6', 'vet@example.com', '6975418200'),
                                                            (3, 'shelter1', '$2a$10$GpALHQdK510GEUhqhIEeH.pEfg5q2G.l1sk/lXbFgIbdqwpNDqjLG', 'shelter1@example.com', '6933818407'),
                                                            (4, 'ariadni', '$2a$10$wWHTt98INEwXiln64kVK3el8vYyKtfE2xcBsWRgE4njYha0mYA1na', 'akarakatsanidi@gmail.com', '6934689124'),
                                                            (5, 'shelter2', '$2a$10$Z.bvhLa3VE.qcCSl6/SYJudP99Fufz/e.znatoH8Q0WrIB4cmhsDG', 'shelter2@example.com', '6941074299'),
                                                            (6, 'D40augas', '$2a$10$3vmvwTjGkcOYe4Z1VbAZZ.8qKbnadYysNy.urWE0fLM45QF3kHm8G', 'dimitris40augas@gmail.com', '6976908151'),
                                                            (7, 'Byronlouki21', '$2a$10$1KOmsGyaqiVbiJmaFYwq1eiscK31u/qwyzLX8vHmaYTQokSOMDqjO', 'byronlouki21@gmail.com', '6945668184'),
                                                            (8, 'admin', '$2a$10$COmoVZkTRb.F9LNCFPAPpO6KEhi/PWq0.rueZ/zvERWFjhVqppVoe', 'admin@example.com', '6971106222');

-- User's password - > 1: citizen
--                     2: vet
--                     3: shelter1
--                     4: ariadni
--                     5: shelter2
--                     6: D40augas
--                     7: 12Byronlouki!
--                     8: admin

-- Insert Roles
--INSERT INTO roles (id, name) VALUES
                                 --(1, 'ROLE_CITIZEN'),
                                 --(2, 'ROLE_SHELTER'),
                                 --(3, 'ROLE_VETERINARIAN'),
                                 --(4, 'ROLE_ADMIN');

-- Assign Roles to Users
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),
                                              (2, 3),
                                              (3, 2),
                                              (4, 3),
                                              (5, 2),
                                              (6, 1),
                                              (7, 1),
                                              (8, 4);

-- Insert Citizens
INSERT INTO citizen (id, first_name, last_name, address) VALUES
    (1, 'Giorgos', 'Xelakis', 'Maiandrou 67'),
    (6, 'Dimitrios', 'Sarantaugas', 'Cappadocia 80'),
    (7, 'Vyronas', 'Loukidelis', 'Tyannon 18');

-- Insert Veterinarians
INSERT INTO veterinarian (id, first_name, last_name) VALUES
    (2, 'Alice', 'Smith'),
    (4, 'Ariadni', 'Karakatsanidi');

-- Insert Shelters
INSERT INTO shelter (id, name, location, address, description, approval_status) VALUES
    (3, 'Happy Tails Shelter', 'Downtown', '456 Elm St', 'A place for rescued pets', 1),
    (5, 'Pet family', 'Athens', 'Anakous 2', 'Welcome to our brand new adoption website', 1);

-- Insert Pets
INSERT INTO pet (id, name, age, species, sex, approval_status, shelter_id, citizen_id) VALUES
                                                                                           (1, 'Buddy', 3, 'Dog', 'Male', 1, 3, NULL),
                                                                                           (2, 'Mittens', 2, 'Cat', 'Female', 0, 3, NULL),
                                                                                           (3, 'Rocky', 1, 'Dog', 'Male', 1, 5, NULL),
                                                                                           (4, 'Lara', 8, 'Dog', 'Female', 3, 5, 7),
                                                                                           (5, 'Silver', 4, 'Dog', 'Male', 3, 5, 7),
                                                                                           (6, 'Bella', 3, 'Dog', 'Female', 1, 3, NULL),
                                                                                           (7, 'Max', 2, 'Cat', 'Male', 1, 3, NULL),
                                                                                           (8, 'Charlie', 4, 'Rabbit', 'Male', 0, 3, NULL),
                                                                                           (9, 'Lucy', 1, 'Parrot', 'Female', 1, 5, NULL),
                                                                                           (10, 'Rocky', 6, 'Dog', 'Male', 2, 5, NULL),
                                                                                           (11, 'Milo', 3, 'Hamster', 'Male', 1, 5, NULL),
                                                                                           (12, 'Daisy', 7, 'Turtle', 'Female', 3, 5, 6),
                                                                                           (13, 'Rex', 4, 'Dog', 'Male', 1, 3, NULL),
                                                                                           (14, 'Coco', 2, 'Parrot', 'Female', 3, 5, 6);

-- Insert Health Checks
INSERT INTO health_check (examination_id, details, status, pet_id, veterinarian_id) VALUES
                                                                            (1,  'Routine checkup - healthy', 1, 1, 2),
                                                                            (2,  'Vaccination - scheduled', 0, 2, 2),
                                                                            (3,  'Routine check-up completed', 1, 1, 4),
                                                                            (4,  'Vaccination completed',1, 2, 2),
                                                                            (5,  'Scheduled for heartworm test',0,  3, 4),
                                                                            (6,  'Dental cleaning completed',1,  4, 4),
                                                                            (7,  'Passed general health test',1,  5, 2),
                                                                            (8,  'General health check-up passed', 1, 6, 2),
                                                                            (9,  'Pending routine vaccination',0,  7, 2),
                                                                            (10, 'Ear infection treatment completed',1,  8, 4),
                                                                            (11, 'Pending annual exam',0,  9, 4),
                                                                            (12, 'Failed orthopedic assessment',2,  10, 4),
                                                                            (13, 'Successful grooming inspection',1,  11, 4),
                                                                            (15, 'Passed routine blood test',1,  12, 4),
                                                                            (16, 'Routine vaccination completed', 1, 13, 4),
                                                                            (17, 'Passed health assessment',1,  14, 2);

-- Insert Adoption Requests
INSERT INTO adoption (request_id, status, pet_id, citizen_id, shelter_id) VALUES
    (1, 3, 4,  7, 5),
    (2, 3, 5,  7, 5),
    (3, 3, 13, 6, 5),
    (4, 3, 15, 6, 5),
    (5, 2, 7, 1, 3),
    (6, 0, 11, 1, 5),
    (7, 0, 10, 1, 5),
    (8, 0, 6, 6, 3);




-- INSERT INTO contact (id, message, scheduled_visit, notified_citizen, notified_shelter) VALUES
--                                                                                            (1, 'Reminder: Visit scheduled for adoption center.', '2025-02-15 10:00:00', false, false),
--                                                                                            (2, 'Don\'t forget about your visit to see the pets!', '2025-02-15 11:30:00', false, false),
-- (3, 'You have a scheduled visit for your pet adoption!', '2025-02-15 12:00:00', false, false),
-- (4, 'Shelter visit confirmation for adoption.', '2025-02-15 14:00:00', true, false),
-- (5, 'Reminder: Visit to see your potential pet today.', '2025-02-15 15:00:00', false, true),
-- (6, 'Final reminder for your upcoming shelter visit.', '2025-02-15 16:30:00', false, false),
-- (7, 'Visit reminder: Shelter waiting for your arrival.', '2025-02-15 17:45:00', true, true);

-- Insert Contacts
INSERT INTO contact (id, message, scheduled_visit, status, citizen_id, shelter_id) VALUES (1, 'I would really like to visit your shelter sometime.', '2025-03-14 21:00:00.00', 1, 1, 3),
                                                                                          (2, 'Can i come in your adoption center?', '2025-02-15 14:00:00', 2, 1, 5),
                                                                                          (3, 'Visit to see your potential pet today.', '2025-02-28 15:00:00', 1, 7, 5),
                                                                                          (4, 'Can i come tomorrow?', '2025-02-20 20:00:00', 0, 7, 5),
                                                                                          (5, 'Are you open at the following date?', '2025-03-02 09:00:00', 0, 6, 5);

-- Syncs the id sequences
SELECT setval('adoption_request_id_seq', COALESCE((SELECT MAX(request_id) FROM adoption), 0) + 1, false);
SELECT setval('contact_id_seq', COALESCE((SELECT MAX(id) FROM contact), 0) + 1, false);
SELECT setval('health_check_examination_id_seq', COALESCE((SELECT MAX(examination_id) FROM health_check), 0) + 1, false);
SELECT setval('pet_id_seq', COALESCE((SELECT MAX(id) FROM pet), 0) + 1, false);
SELECT setval('roles_id_seq', COALESCE((SELECT MAX(id) FROM roles), 0) + 1, false);
SELECT setval('users_id_seq', COALESCE((SELECT MAX(id) FROM users), 0) + 1, false);