It's a really simple file upload and rehosting tool in the style of puush or transfer.sh, but you can just run it yourself without overcomplicating the whole situation.. There is also a handy Firefox addon, which lets you right click an image to rehost it on an alsuti instance.

## Configuration

To configure the application, you just need to set two environment variables (both the server and the client use these):

* ALSUTI_API_KEY - The API key necessary to post on the server.
* ALSUTI_ENDPOINT - The base endpoint for the server, not including the 'upload' path and not including a trailing slash.

If you use bash, you can simply set these in your .bashrc, if you use fish you can do similarly in the fish config file using the 'setenv' command.

## Server

To run the server, 

1. cd server
1. Run npm install
2. Run bin/www.

## Command line client

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

### Screenshots

If you want to automatically upload screenshots, you can install xclip and scrot, then add the following alias in bash (though it is more convenient if you put it in a bash file in your PATH somewhere, since then you can run it from dmenu): 

```alias scroti="scrot $@ -e '~/alsuti/alsuti $f | tail -n 1 | xclip -selection clipboard'"```



## Firefox

The firefox plugin allows you to easily rehost images on an alsuti instance and then share the link. This is useful for sharing images which are transient, or behind login-walls to your friends. You can simply right click an image, and then click 'Upload to Alsuti,' and the link will be copied to your clipboard.

![Firefox Addon Screenshot](http://reality.rehab/al/VyFaTRiox.png)

To set this up, you simply need to install the addon in your firefox: firefox/alsuti_firefox_addon-0.0.2-an+fx.xpi and set the api key and endpoint path in the addon settings (note that unlike the ALSUTI_ENDPOINT environment variable, the endpoint for the firefox plugin must be the full upload path e.g. http://reality.rehab/al/upload).
