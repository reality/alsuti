It's a really simple file upload and rehosting tool in the style of puush or transfer.sh, but you can just run it yourself without overcomplicating the whole situation.. There is also a handy Firefox addon, which lets you right click an image to rehost it on an alsuti instance (just install  alsuti/firefox/alsuti_firefox_addon-0.0.1-an+fx.xpi and set the endpoint/api key in the preferences).

If you want to run your own server:

1. Change the req.api_key line and the req.external_path variables in server/app.js to whatever you like.
2. Run npm install
3. Run bin/www.

To set up the client: 

1. Change the api_key and url to your own or someone else's instance details in the alsuti bash file.
2. Add alias alsuti='~/whereverthisrepois/alsuti' to your .bashrc (untested, because I don't use bash)
3. Then you can run alsuti whateveryouwanttoupload.gif

Note: if you use fish, you can add an alias instead by doing:

```
function alsuti
  ~/whereverthisrepois/alsuti $argv[1]
end
funcsave alsuti
```
