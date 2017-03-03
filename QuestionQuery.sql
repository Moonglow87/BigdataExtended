-- Geef het aantal films dat in Nederland gemaakt is weer in de tijd.
                SELECT count(*) as counted, releasedate FROM movies
                        INNER JOIN movielocation ON movies.movietitle = movielocation.movietitle
                        AND movies.releasedate = movielocation.moviereleasedate
                        AND movies.isserie = movielocation.isserie
                        WHERE locationname = 'nl' AND movies.isserie = FALSE GROUP BY releasedate
                        ORDER BY counted DESC;
-- Wat is het meest voorkomende film genre
                SELECT counted, genretype FROM (
                        SELECT count(*) AS counted, genretype FROM moviegenre
                        WHERE isserie = FALSE
                        GROUP BY genretype
                        ) AS count;
-- Geef visueel aan waar films worden gemaakt.
                SELECT count(*) as counted, country.countryname FROM movielocation
                        INNER JOIN country ON country.countryid = movielocation.locationname
                        WHERE movielocation.isserie = FALSE GROUP BY countryname
                        ORDER BY counted DESC;
-- Wat is het meest voorkomende genre met het woord 'beer' in de titel.
                SELECT counted, genretype from (
                        SELECT count(*) AS counted, genretype FROM moviegenre
                        WHERE isserie = FALSE
                             AND movietitle LIKE '%beer%'
                        GROUP BY genretype ORDER BY counted DESC
                        ) AS count LIMIT 10;
    

-- Welke acteur heeft de meeste dubbelrollen
                SELECT count(*) as dubbelrollen, actorname FROM actorinmovie
                        WHERE actorplays LIKE '%/%' GROUP BY actorname ORDER BY dubbelrollen DESC
                        LIMIT 10;
--In welke films speelde Joop Braakhekke,
                SELECT * FROM actorinmovie 
                        WHERE actorname = 'Braakhekke, Joop'  
                        AND isserie = FALSE;
--Welke acteur (m/v) speelt het meest in de slechtst gewaardeerde films? !!Duurt Lang!!,
                SELECT one.actorname, sum(case when two.rating < 3.5 and two.isserie = FALSE then 1 else 0  end) as BadMovies 
                        FROM actorinmovie AS one 
                        INNER JOIN movies AS two 
                           ON one.movietitle = two.movietitle 
                           AND one.moviereleasedate = two.releasedate 
                        GROUP BY one.actorname 
                        ORDER BY BadMovies DESC 
                        LIMIT 100;
--Welke regisseur heeft de meeste films met Jim Carrey in de hoofdrol geregisseerd,
                SELECT count(*) AS counted, directorname FROM moviedirector 
                        INNER JOIN actorinmovie 
                        ON moviedirector.movietitle = actorinmovie.movietitle 
                         AND moviedirector.moviereleasedate = actorinmovie.moviereleasedate 
                         AND moviedirector.isserie = actorinmovie.isserie 
                        WHERE actorinmovie.actorname = 'Carrey, Jim' 
                            AND actorinmovie.priority = 1 
                        GROUP BY directorname 
                        ORDER BY counted DESC 
                        LIMIT 10;
--In welk jaar tussen 1990 en nu zijn de meeste films met het woord ‘beer’ in de titel geproduceerd,
                SELECT counted, releasedate from ( 
                         SELECT count(*) AS counted, releasedate FROM movies 
                        WHERE isserie = FALSE 
                             AND movietitle LIKE '%beer%' 
                        GROUP BY releasedate ORDER BY counted DESC 
                        ) AS count LIMIT 10
--Wat is het meest voorkomende genre met het woord 'beer' in de titel,
                SELECT counted, genretype from ( 
                         SELECT count(*) AS counted, genretype FROM moviegenre 
                        WHERE isserie = FALSE 
                             AND movietitle LIKE '%beer%' 
                        GROUP BY genretype ORDER BY counted DESC 
                        ) AS count LIMIT 10
--Wat is de kortste film met een waardering van 8.5 of hoger,
                SELECT * 
                        FROM movies 
                        WHERE rating > 8.5 AND duration > 1 
                              AND rating NOTNULL 
                              AND movies.isserie = FALSE 
                        ORDER BY duration ASC;
--Hoeveel films heeft Woody Allen gemaakt,
                SELECT count(*) 
                        FROM moviedirector 
                        WHERE directorname = 'Allen, Woody';
--In hoeveel daarvan speelde Woody Allen zelf mee,
                SELECT count(*) FROM actorinmovie 
                        INNER JOIN moviedirector 
                           ON moviedirector.movietitle = actorinmovie.movietitle 
                         AND moviedirector.moviereleasedate = actorinmovie.moviereleasedate 
                         AND moviedirector.isserie = actorinmovie.isserie 
                        WHERE directorname = 'Allen, Woody' 
                             AND actorname = 'Allen, Woody' 
                             AND actorinmovie.isserie = FALSE;
--Zijn er ook films waarin Woody Allen wel speelde maar niet regisseerde,
                SELECT DISTINCT actorinmovie.* FROM actorinmovie 
                        INNER JOIN moviedirector 
                           ON moviedirector.movietitle = actorinmovie.movietitle 
                         AND moviedirector.moviereleasedate = actorinmovie.moviereleasedate 
                         AND moviedirector.isserie = actorinmovie.isserie 
                        WHERE directorname != 'Allen, Woody' 
                             AND actorname = 'Allen, Woody' 
                             AND actorinmovie.isserie = FALSE;