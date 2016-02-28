It's a really simple file upload and rehosting tool in the style of puush or transfer.sh, but you can just run it yourself without overcomplicating the whole situation.. There is also a handy Firefox addon, which lets you right click an image to rehost it on an alsuti instance (just install  alsuti/firefox/alsuti_firefox_addon-0.0.2-an+fx.xpi and set the endpoint/api key in the preferences).

To configure the application, you just need to set two environment variables (both the server and the client use these):

* ALSUTI_API_KEY - The API key necessary to post on the server.
* ALSUTI_ENDPOINT - The base endpoint for the server, not including the 'upload' path and not including a trailing slash.

If you use bash, you can simply set these in your .bashrc, if you use fish you can do similarly in the fish config file using the 'setenv' command.

To run the server, 

1. cd server
1. Run npm install
2. Run bin/www.

To set up the client:,

2. Add ```alias alsuti='~/whereverthisrepois/alsuti'``` to your .bashrc
3. Then you can run alsuti whateveryouwanttoupload.gif

Note: if you use fish, you can add an alias instead by doing:

```
function alsuti
  ~/whereverthisrepois/alsuti $argv[1]
end
funcsave alsuti
```
