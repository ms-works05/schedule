CREATE TABLE IF NOT EXISTS tasklist(
    id VARCHAR(8)   PRIMARY KEY,
    task VARCHAR(256),
    deadline VARCHAR(10),
    memo VARCHAR(255),
    done BOOLEAN
);