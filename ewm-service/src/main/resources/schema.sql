drop table
drop table
drop table
drop table
drop table
drop table



CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(254) NOT NULL unique,
    name varchar(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation varchar(2000) NOT NULL,
    title varchar(120) NOT NULL,
    description varchar(7000) NOT NULL,
    categoty_id BIGINT,
    event_date timestamp NOT NULL,
    created_on timestamp NOT NULL,
    published_on timestamp NOT NULL,
    user_id BIGINT,
    paid boolean,
    requestModeration boolean,
    state varchar(9) NOT NULL,
    participant_limit integer NOT NULL,
    location_id BIGINT,
    CONSTRAINT fk_events_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY(categoty_id) REFERENCES categories(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY(categoty_id) REFERENCES categories(id) ON DELETE CASCADE ON UPDATE CASCADE, //todo location
);

