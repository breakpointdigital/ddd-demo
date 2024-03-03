INSERT INTO public.users (id, username, email, password, role)
VALUES ('01H1ANGPC16W1JVWV7YD2YRH5C', 'john', 'john@mail.com',
        'password', 'TEAM'),
       ('01H1APBFYS2VRNEND22YCJ94JC', 'anne', 'anne@mail.com',
        'password', 'CUSTOMER') ON CONFLICT DO NOTHING;
