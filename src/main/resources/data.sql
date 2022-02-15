insert into role (id, role)
values (1, 'expert'),
       (2, 'admin');

insert into insurance (id, insurance_type)
values (3, 'ОСАГО'),
       (4, 'КАСКО'),
       (5, 'ИФЛ');

insert into service (id, service, price, active)
values (6, 'Осмотр ТС', 390, true),
       (7, 'Выезд в пределах', 270, true),
       (8, 'Холостой', 270, true),
       (9, 'Выезд за пределы', 490, true),
       (10, 'Имущество физических лиц', 1400, true),
       (11, 'Осмотр виновника', 390, true);

insert into user (id, full_name, email, phone_number, password, active)
values (1, 'Сергей Базаревич', 'bazar@mail.com', '9272369586', 'serzbazar', true),
       (2, 'Василий Карасев', 'karas-vas@yandex.ru', '9273455125', '1234', true),
       (3, 'Андрей Кириленко', 'kirya@ya.ru', '9379005623', 'qwerty', true),
       (4, 'Тимофей Мозгов', 'tima_soccer@gmail.com', '9655263698', 'asdf', true),
       (5, 'Алексей Швед', 'shved-sosed@vk.com', '9652368596', 'mypass', true);

insert into users_roles(user_id, role_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (5, 2);

insert into orders(id, num_order, num_incident, type_insurance_id, completion_date, address, property, desc_property,
                   owner, persist_time, last_update, user_id, description)
values (1, 156326, 18963256, 3, TIMESTAMP '2010-01-12 12:10:00', 'г. Москва, ул. Космонавтов, д. 34', 'BMW X3',
        'К956ХС777', 'Максимов Леонид', CURRENT_TIMESTAMP(), current_timestamp(), 1, ''),
       (2, 156327, 18985256, 4, TIMESTAMP '2010-01-12 13:10:00', 'г. Москва, ул. Комсомольская, д. 86', 'Lada Vesta',
        'С333РХ102', 'Некрасов Игорь', CURRENT_TIMESTAMP(), current_timestamp(), 1, ''),
       (3, 156328, 18963236, 3, TIMESTAMP '2010-01-13 12:00:00', 'г. Москва, ул. Некрасова, д. 125/2', 'Opel Insignia',
        'У456КХ702', 'Казанцева Ольга', CURRENT_TIMESTAMP(), current_timestamp(), 1, ''),
       (4, 156852, 18963369, 5, TIMESTAMP '2010-01-13 15:30:00', 'г. Железнодорожный, ул. Незалежная, д. 2а, кв. 156',
        'Квартира в многоквартирном доме',
        '', 'Шевцова Анна Олеговна', CURRENT_TIMESTAMP(), current_timestamp(), 1,
        'Клиент не открыл дверь. На звонок не ответил'),
       (5, 157256, 18985963, 3, TIMESTAMP '2010-01-12 09:00:00', 'г. Оренбург, ул. Максима Горького, д. 12',
        'Scania D2',
        'Р563РП142', 'РБ г. Оренбург и Оренбургская обл', CURRENT_TIMESTAMP(), current_timestamp(), 2, ''),
       (6, 158563, 18968856, 3, TIMESTAMP '2010-01-12 10:10:00', 'г. Оренбург, Оренбургский тракт, д. 156',
        'Chevrolet Niva',
        'Н963НП142', 'Ворфоломеев Павел Викторович', CURRENT_TIMESTAMP(), current_timestamp(), 2, ''),
       (7, 156853, 18956395, 4, TIMESTAMP '2010-02-14 11:50:00', 'Оренбургская обл, д. Матвеевка, ул. Школьная, д. 2',
        'Chevrolet Lacetti',
        'С741ХС142', 'Вавилов Виталий', CURRENT_TIMESTAMP(), current_timestamp(), 3, ''),
       (8, 156896, 18978563, 4, TIMESTAMP '2010-02-15 12:10:00', 'г. Мелеуз, ул. Смоленская, д. 186', 'Mercedes S200',
        'П852РБ102', 'Казначеева Наталья Викторовна', CURRENT_TIMESTAMP(), current_timestamp(), 4, ''),
       (9, 185263, 18945698, 3, TIMESTAMP '2010-03-20 12:10:00', 'г. Мелеуз, ул. Краснознаменная, д. 2', 'Lada Granta',
        'О856РО102', 'Управление ГИБДД по г. Мелеуз и Мелеузовскому району в РБ', CURRENT_TIMESTAMP(),
        current_timestamp(), 4, ''),
       (10, 156963, 18995632, 4, TIMESTAMP '2010-03-20 12:40:00', 'г. Мелеуз, ул. Первомайская, д. 1', 'Hyundai Tucson',
        'Х777ХМ702', 'Самойлова Анастасия', CURRENT_TIMESTAMP(), current_timestamp(), 4, ''),
       (11, 156742, 1896355632, 5, TIMESTAMP '2010-03-20 14:00:00', 'Мелеузовский р-н, д. Ташлыкуль, ул. Онги Юл, д. 4',
        'Частный дом',
        'возгорание в частном доме', 'Кравцов Иван Сергеевич', CURRENT_TIMESTAMP(), current_timestamp(), 4, '');

insert into orders_services (order_id, service_id)
values (1, 6),
       (1, 7),
       (2, 6),
       (2, 7),
       (3, 6),
       (3, 7),
       (4, 8),
       (4, 9),
       (5, 6),
       (5, 7),
       (6, 11),
       (6, 7),
       (7, 9),
       (8, 6),
       (8, 9),
       (9, 6),
       (9, 7),
       (10, 11),
       (10, 7),
       (11, 9),
       (11, 10);