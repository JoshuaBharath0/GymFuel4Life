CREATE TABLE members (
    user_id BIGSERIAL PRIMARY KEY,
    password VARCHAR(255)  NULL,
    first_name VARCHAR(50)  NULL,
    last_name VARCHAR(50)  NULL,
    gender VARCHAR(10)  NULL,
    email_address VARCHAR(50) NOT null UNIQUE,
    date_of_birth DATE  NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE members
ADD COLUMN height INTEGER;

drop table  members;

select * from members;