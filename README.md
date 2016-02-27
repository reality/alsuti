Just change the req.api_key line and the req.external_path variables in server/app.js to whatever you like, then run bin/www.

Then you can upload things to the server with e.g. curl --form "fileupload=@test.txt" --form api_key=fdjiaofjai849309809ahionanlnkjzbahhfhwa http://iliketurtles.com/upload
