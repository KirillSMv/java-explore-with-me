drop TABLE IF EXISTS users CASCADE;
drop TABLE IF EXISTS categories CASCADE;
drop TABLE IF EXISTS compilations CASCADE;
drop TABLE IF EXISTS events CASCADE;
drop TABLE IF EXISTS events_compilations CASCADE;
drop TABLE IF EXISTS event_requests CASCADE;
drop TABLE IF EXISTS places CASCADE;

create table if not exists users (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(254) NOT NULL unique,
    name varchar(250) NOT NULL
);

create table if not exists categories (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL unique
);

create table if not exists compilations (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned boolean,
    title varchar(50) NOT NULL
);

create table if not exists events (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
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
    CONSTRAINT fk_events_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id) ON delete CASCADE ON update CASCADE
);

create table if not exists events_compilations (
    event_id bigint not null,
    compilation_id bigint not null,
    constraint pk_event_id_compilation_id primary key (event_id, compilation_id),
    constraint fk_events_compilations_to_events foreign key(event_id) references events(id) on delete cascade on update cascade,
    constraint fk_events_compilations_to_compilations foreign key(compilation_id) references compilations(id) on delete cascade on update cascade
);

create table if not exists event_requests (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT,
    created timestamp NOT NULL,
    user_id BIGINT,
    status varchar(9) NOT NULL,
    CONSTRAINT fk_event_requests_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON delete CASCADE ON update CASCADE,
    CONSTRAINT fk_event_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id) ON delete CASCADE ON update CASCADE,
    CONSTRAINT unique_event_id_user_id UNIQUE(event_id, user_id)
);

create table if not exists places (
   id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   name varchar(120) NOT NULL unique,
   lat real NOT NULL,
   lon real NOT NULL,
   rad real NOT NULL
);

create or replace FUNCTION public.distance(
	lat1 real,
	lon1 real,
	lat2 real,
	lon2 real)
    RETURNS real
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS '
declare
    dist float = 0;
    rad_lat1 float;
    rad_lat2 float;
    theta float;
    rad_theta float;
BEGIN
    IF lat1 = lat2 AND lon1 = lon2
    THEN
        RETURN dist;
    ELSE
        rad_lat1 = pi() * lat1 / 180;
        rad_lat2 = pi() * lat2 / 180;
        theta = lon1 - lon2;
        rad_theta = pi() * theta / 180;
        dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

        IF dist > 1
            THEN dist = 1;
        END IF;

        dist = acos(dist);
        dist = dist * 180 / pi();
        dist = dist * 60 * 1.8524;

        RETURN dist;
    END IF;
END;
'









