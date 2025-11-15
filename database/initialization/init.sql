CREATE TABLE users (
    userid SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

INSERT INTO users (username, password_hash)
VALUES ('cyTestUser', '$2a$10$onsIjFTbV/MSGrIfF9cgbe7Y2hOR5nmTxqAdcTnOOrS853HwZwruG');

CREATE TABLE ballpark (
    ballpark_id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    CONSTRAINT chk_lat CHECK (lat BETWEEN -90 AND 90),
    CONSTRAINT chk_lon CHECK (lon BETWEEN -180 AND 180)
);

CREATE TABLE game (
    game_id VARCHAR(255) PRIMARY KEY,
    home_team VARCHAR(255) NOT NULL,
    away_team VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    ballpark_id INTEGER NOT NULL,
    CONSTRAINT fk_ballpark 
        FOREIGN KEY (ballpark_id)
        REFERENCES ballpark (ballpark_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE trip (
    tripid SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    start_latitude DOUBLE PRECISION,
    start_longitude DOUBLE PRECISION,
    end_latitude DOUBLE PRECISION,
    end_longitude DOUBLE PRECISION,
    is_generated BOOLEAN NOT NULL,
    max_hours_per_day INTEGER,
    userid BIGINT NOT NULL,
    CONSTRAINT fk_users
        FOREIGN KEY (userid)
        REFERENCES users (userid)
        ON DELETE CASCADE
);

INSERT INTO trip (
  name, start_date, end_date,
  start_latitude, start_longitude,
  end_latitude, end_longitude,
  is_generated, max_hours_per_day, userid
)
VALUES (
  'Cypress Test Trip', '2026-03-25', '2026-03-29',
  NULL, NULL, NULL, NULL,
  FALSE, NULL, 1
);

CREATE TABLE trip_stop (
    tripstopid SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    location VARCHAR(255) NOT NULL,
    ballpark_id INTEGER,
    game_id VARCHAR(255),
    tripid BIGINT NOT NULL,
    CONSTRAINT fk_trip
        FOREIGN KEY (tripid)
        REFERENCES trip (tripid)
        ON DELETE CASCADE,
    CONSTRAINT fk_ballpark 
        FOREIGN KEY (ballpark_id)
        REFERENCES ballpark (ballpark_id),
    CONSTRAINT fk_game 
        FOREIGN KEY (game_id)
        REFERENCES game (game_id)
);