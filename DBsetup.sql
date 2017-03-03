CREATE TABLE actor
(
    actorname VARCHAR(500) NOT NULL,
    isman BOOLEAN NOT NULL,
    birthplace VARCHAR(200),
    CONSTRAINT actor_pkey PRIMARY KEY (actorname, isman),
    CONSTRAINT actor_birthplace_fkey FOREIGN KEY (birthplace) REFERENCES birthplaces (birthplace)
);
CREATE TABLE actorinmovie
(
    actorname VARCHAR(500),
    movietitle VARCHAR(500),
    moviereleasedate INTEGER,
    isserie BOOLEAN,
    actorplays TEXT,
    priority INTEGER,
    isman BOOLEAN,
    CONSTRAINT actorinmovie_actorname_fkey FOREIGN KEY (actorname, isman) REFERENCES actor (actorname, isman),
    CONSTRAINT actorinmovie_movietitle_fkey FOREIGN KEY (movietitle, moviereleasedate, isserie) REFERENCES movies (movietitle, releasedate, isserie)
);
CREATE TABLE birthplaces
(
    birthplace VARCHAR(200) PRIMARY KEY NOT NULL,
    coords VARCHAR(400)
);
CREATE TABLE country
(
    countryid VARCHAR(100) PRIMARY KEY NOT NULL,
    countryname VARCHAR(500)
);
CREATE TABLE director
(
    name VARCHAR(200) PRIMARY KEY NOT NULL
);
CREATE TABLE genre
(
    type VARCHAR(100) PRIMARY KEY NOT NULL
);
CREATE TABLE moviedirector
(
    directorname VARCHAR(200),
    movietitle VARCHAR(500),
    moviereleasedate INTEGER,
    isserie BOOLEAN,
    CONSTRAINT moviedirector_directorname_fkey FOREIGN KEY (directorname) REFERENCES director (name),
    CONSTRAINT moviedirector_movietitle_fkey FOREIGN KEY (movietitle, moviereleasedate, isserie) REFERENCES movies (movietitle, releasedate, isserie)
);
CREATE TABLE moviegenre
(
    genretype VARCHAR(100),
    movietitle VARCHAR(500),
    moviereleasedate INTEGER,
    isserie BOOLEAN,
    CONSTRAINT moviegenre_genretype_fkey FOREIGN KEY (genretype) REFERENCES genre (type),
    CONSTRAINT moviegenre_movietitle_fkey FOREIGN KEY (movietitle, moviereleasedate, isserie) REFERENCES movies (movietitle, releasedate, isserie)
);
CREATE TABLE moviegross
(
    movietitle VARCHAR(500),
    moviereleasedate INTEGER,
    isserie BOOLEAN,
    grosstype VARCHAR(20),
    grossvalue NUMERIC,
    grosslocation VARCHAR(100),
    CONSTRAINT moviegross_movietitle_fkey FOREIGN KEY (movietitle, moviereleasedate, isserie) REFERENCES movies (movietitle, releasedate, isserie)
);
CREATE TABLE movielocation
(
    locationname VARCHAR(100),
    movietitle VARCHAR(500),
    moviereleasedate INTEGER,
    isserie BOOLEAN,
    CONSTRAINT movielocation_locationname_fkey FOREIGN KEY (locationname) REFERENCES country (countryid),
    CONSTRAINT movielocation_movietitle_fkey FOREIGN KEY (movietitle, moviereleasedate, isserie) REFERENCES movies (movietitle, releasedate, isserie)
);
CREATE TABLE movies
(
    movietitle VARCHAR(500) NOT NULL,
    releasedate INTEGER NOT NULL,
    duration INTEGER,
    rating NUMERIC,
    isserie BOOLEAN NOT NULL,
    CONSTRAINT movies_pkey PRIMARY KEY (movietitle, releasedate, isserie)
);


--Add data to genre table
INSERT INTO genre (
  SELECT DISTINCT genre FROM genresstaging
);

--Add data to land table
INSERT INTO country (
  SELECT DISTINCT country FROM productioncompaniesstaging
);

--Add data to director table
INSERT INTO director (
  SELECT DISTINCT name FROM directorsstaging
);
--Set releaseyears to NOTNULL because postges doesnt support combined pkey with a null value..
UPDATE  moviesstaging
  SET releaseyear = -1
  WHERE releaseyear ISNULL;

UPDATE  ratingsstaging
  SET releaseyear = -1
  WHERE releaseyear ISNULL;

UPDATE  directorsstaging
  SET releaseyear = -1
  WHERE releaseyear ISNULL;

UPDATE  actorsstaging
  SET releaseyear = -1
  WHERE releaseyear ISNULL;

UPDATE  businessstaging
  SET movieyear = -1
  WHERE movieyear ISNULL;

--Add movies to videomateriaal table
INSERT INTO movies(movietitle, releasedate, isserie) (
    SELECT DISTINCT name, releaseyear, isserie FROM moviesstaging
);

--Add actors to acteur table
INSERT INTO actor (actorname, isman) (
  SELECT DISTINCT name, ismale FROM actorsstaging
);

--Add movie runtime to corresponding movie
UPDATE movies AS new
  SET duration = stage.runtime
  FROM runningtimesstaging AS stage
  WHERE (new.movietitle = stage.moviename AND new.releasedate = stage.releaseyear AND new.isserie = stage.isserie)
        AND (new.isserie = FALSE OR stage.isserie = FALSE);

--Add average runtime to series
UPDATE movies AS new
  SET duration = stage.runtime
  FROM (
      SELECT moviename, releaseyear, AVG(runtime) AS runtime, runningtimesstaging.isserie FROM runningtimesstaging
      WHERE runningtimesstaging.isserie
      GROUP BY moviename, releaseyear, runningtimesstaging.isserie
       )AS stage
  WHERE (new.movietitle = stage.moviename AND new.releasedate = stage.releaseyear AND new.isserie = stage.isserie);

--Add birthplaces to actors
UPDATE bigmovie.acteur AS new
  SET geboorteplaats = stage.birthplace
  FROM (SELECT * FROM "bigmovieStaging".biographies) AS stage
  WHERE stage.name = new.naam;

--add movie rating to videomateriaal
UPDATE movies AS new
  SET rating = stage.rating
  FROM ratingsstaging as stage
  WHERE new.movietitle = stage.moviename AND new.releasedate = stage.releaseyear AND new.isserie = stage.isserie AND stage.isserie = FALSE;

--Add average rating to series in videomateriaal
UPDATE movies AS new
  SET rating = stage.rating
  FROM (
      SELECT moviename, releaseyear, AVG(rating) AS rating, ratingsstaging.isserie FROM ratingsstaging
      WHERE ratingsstaging.isserie = FALSE
      GROUP BY moviename, releaseyear, ratingsstaging.isserie) AS stage
  WHERE new.movietitle = stage.moviename AND new.releasedate = stage.releaseyear AND new.isserie = stage.isserie;


--Add references between movies/series and genres
INSERT INTO moviegenre (genretype, movietitle, moviereleasedate, isserie)
    SELECT DISTINCT stage.genre, stage.moviename, stage.releaseyear, stage.isserie FROM genresstaging AS stage
      INNER JOIN movies AS ref
      ON stage.moviename = ref.movietitle AND stage.releaseyear = ref.releasedate AND stage.isserie = ref.isserie;

--Add references between movies/series and countries
INSERT INTO movielocation (locationname, movietitle, moviereleasedate, isserie)
    SELECT DISTINCT stage.country, stage.moviename, stage.releaseyear, stage.isserie FROM productioncompaniesstaging AS stage
      INNER JOIN movies AS ref
      ON stage.moviename = ref.movietitle AND stage.releaseyear = ref.releasedate AND stage.isserie = ref.isserie;

--Add references between movies/series and directors
INSERT INTO moviedirector (directorname, movietitle, moviereleasedate, isserie)
    SELECT DISTINCT stage.name, stage.moviename, stage.releaseyear, stage.isserie FROM directorsstaging AS stage
      INNER JOIN movies AS ref
      ON stage.moviename = ref.movietitle AND stage.releaseyear = ref.releasedate AND stage.isserie = ref.isserie
      WHERE stage.name IN (SELECT director.name FROM director);


--Add references between movies/series and actors
INSERT INTO actorinmovie (actorname, movietitle, moviereleasedate, isserie, actorplays, priority, isman)
    SELECT DISTINCT stage.name, stage.moviename, stage.releaseyear, stage.isserie, stage.role, stage.payroll, stage.ismale FROM actorsstaging AS stage
      INNER JOIN movies AS ref
      ON stage.moviename = ref.movietitle AND stage.releaseyear = ref.releasedate AND stage.isserie = ref.isserie
      WHERE (stage.name) IN (SELECT actor.actorname FROM actor);

--Add references between movies/series and their income
INSERT INTO moviegross(movietitle, moviereleasedate, isserie, grosstype, grossvalue, grosslocation)
    SELECT DISTINCT stage.moviename, stage.movieyear, stage.isserie, stage.currency, stage.amount, stage.type FROM businessstaging AS stage
      INNER JOIN movies AS ref
      ON stage.moviename = ref.movietitle AND stage.movieyear = ref.releasedate AND stage.isserie = ref.isserie;

--Add birthplaces to actors
UPDATE actor AS new
  SET birthplace = stage.birthplace
  FROM (SELECT * FROM actorbackup) AS stage
  WHERE stage.actorname = new.actorname;