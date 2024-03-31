DROP TABLE IF EXISTS stats;

CREATE TABLE IF NOT EXISTS stats (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app varchar(255) NOT NULL,
    uri varchar(255) NOT NULL,
    ip varchar(255) NOT NULL,
    timestamp timestamp NOT NULL,
    hits BIGINT NOT NULL
);