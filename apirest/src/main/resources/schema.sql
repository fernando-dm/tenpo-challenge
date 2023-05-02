-- set schema PUBLIC;

DROP TABLE IF EXISTS history CASCADE ;

CREATE SEQUENCE IF NOT EXISTS history_id_sequence
    START WITH 50
    INCREMENT BY 50;

CREATE TABLE history (
                               id INT NOT NULL AUTO_INCREMENT ,
--                                id INT AUTO_INCREMENT  PRIMARY KEY,
                               endpoint_url VARCHAR(100) NOT NULL,
                               response VARCHAR(250) NOT NULL,
                               status VARCHAR(10) DEFAULT NULL
);

