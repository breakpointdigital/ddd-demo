INSERT INTO public.users (id, username, email, password, created_date, updated_date, role)
VALUES ('01H1ANGPC16W1JVWV7YD2YRH5C', 'john', 'john@mail.com',
        'password', '2023-05-26 02:02:34.000000',
        '2023-05-26 02:02:41.000000', 'TEAM'),
       ('01H1APBFYS2VRNEND22YCJ94JC', 'anne', 'anne@mail.com',
        'password', '2023-05-26 02:12:12.000000',
        '2023-05-26 02:12:14.000000', 'CUSTOMER') ON CONFLICT DO NOTHING;
