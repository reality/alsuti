
1. Change the req.api_key line and the req.external_path variables in server/app.js to whatever you like, then run bin/www.
2. Change the api_key and url to the previously configured options in the alsuti file
3. Add alias alsuti='~/whereverthisrepois/alsuti' to your .bashrc
4. Then you can run alsuti whateveryouwanttoupload.gif

Note: if you use fish, you can add an alias instead by doing:

```
function alsuti
  ~/whereverthisrepois/alsuti $argv[1]
end
funcsave alsuti
```
