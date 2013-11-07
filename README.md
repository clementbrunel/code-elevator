Elevator-Brain
=====================================
To run you application, run the root application code-elevator. (Jetty)

Then run with you play application with Play!  (play run)


Mail
====================================
Mailing error is possible with overridding values in application.conf

- `smtp.host='
- `smtp.port='
- `smtp.ssl='
- `smtp.tls='
- `smtp.user='
- `smtp.password='
`


Deploy
====================================
This file will be packaged with your application, when using `play dist`.


##Cloudbees
Then deploy on cloudbees with
`bees app:deploy -t play2 target/universal/elevator-brain-1.0-SNAPSHOT.zip codestory/codestoryhasto -Rjava_version=1.7`


##Heroku
Just push on git for deploy
See logs with
'heroku logs'


#Serveur
See https://github.com/xebia-france/code-elevator for serveur