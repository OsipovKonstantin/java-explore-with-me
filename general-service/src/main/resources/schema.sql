DROP TABLE IF EXISTS users, compilations, locations, categories, events,
    events_compilations, participation_requests;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations
(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    lat FLOAT NOT NULL CHECK (lat >= -90 AND lat <= 90),
    lon FLOAT NOT NULL CHECK (lon >= -180 AND lon <= 180)
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    location_id BIGINT NOT NULL REFERENCES locations(id),
    paid BOOLEAN NOT NULL,
    participant_limit INT NOT NULL CHECK(participant_limit >= 0),
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN NOT NULL,
    event_state VARCHAR(20) NOT NULL,
    title VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id BIGINT NOT NULL REFERENCES events(id),
    compilation_id BIGINT NOT NULL REFERENCES compilations(id),
    PRIMARY KEY(event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    request_status VARCHAR(20) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    UNIQUE (user_id, event_id)
);