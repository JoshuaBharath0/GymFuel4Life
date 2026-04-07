CREATE TABLE user_portal_state (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    gym_setup_complete BOOLEAN DEFAULT FALSE,
    diet_setup_complete BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES members(user_id) ON DELETE CASCADE
);

select * from user_portal_state;