heroku pg:psql --app ahl-javaweb-development < SQL/psql-Drop-Schema.sql 
heroku pg:psql --app ahl-javaweb-development < SQL/psql-Create-Schema.sql 
heroku pg:psql --app ahl-javaweb-development < SQL/psql-Insert-Test-Data.sql 
