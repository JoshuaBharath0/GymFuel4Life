CREATE TABLE user_portal_state (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    gym_setup_complete BOOLEAN DEFAULT FALSE,
    diet_setup_complete BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES members(user_id) ON DELETE CASCADE
);


CREATE TABLE members (
    user_id BIGSERIAL PRIMARY KEY,
    password VARCHAR(255)  NULL,
    first_name VARCHAR(50)  NULL,
    last_name VARCHAR(50)  NULL,
    gender VARCHAR(10)  NULL,
    email_address VARCHAR(50) NOT null UNIQUE,
    date_of_birth DATE  NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);drop table members