CREATE TABLE users (
    userid SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

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
    game_id SERIAL PRIMARY KEY,
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