DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS events_compilations CASCADE;
DROP TABLE IF EXISTS event_requests CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(254) NOT NULL unique,
    name varchar(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL unique
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned boolean,
    title varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation varchar(2000) NOT NULL,
    title varchar(120) NOT NULL,
    description varchar(7000) NOT NULL,
    category_id BIGINT,
    event_date timestamp NOT NULL,
    created_on timestamp NOT NULL,
    published_on timestamp,
    user_id BIGINT,
    confirmed_requests integer,
    paid boolean,
    request_moderation boolean,
    state varchar(9) NOT NULL,
    participant_limit integer NOT NULL,
    lat real NOT NULL,
    lon real NOT NULL,
    CONSTRAINT fk_events_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS events_compilations (
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT pk_event_id_compilation_id PRIMARY KEY (event_id, compilation_id),
    CONSTRAINT fk_events_compilations_to_events FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_events_compilations_to_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS event_requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT,
    created timestamp NOT NULL,
    user_id BIGINT,
    status varchar(9) NOT NULL,
    CONSTRAINT fk_event_requests_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_event_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_event_id_user_id UNIQUE(event_id, user_id)
);











