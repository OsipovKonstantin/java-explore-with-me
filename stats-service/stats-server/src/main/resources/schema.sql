DROP TABLE IF EXISTS hits;

CREATE TABLE IF NOT EXISTS hits
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL
    PRIMARY
    KEY,
    app
    VARCHAR
(
    50
),
    uri VARCHAR
(
    2048
),
    ip VARCHAR
(
    45
),
    timestamp TIMESTAMP
    );
