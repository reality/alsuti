It's a really simple file upload and rehosting tool in the style of puush or transfer.sh, but you can just run it yourself without overcomplicating the whole situation (or you can use someone else's server if you like)... You can run it from the command line, use it with scrot, or there is also a handy Firefox addon, which lets you right click an image to rehost it on an alsuti instance. Currently there is no Windows or Chrome client, but I imagine it would probably be pretty easy to create these (can't Chrome run Firefox addons these days?).

## Configuration

To configure the application, you need to set two environment variables (both the server and the client use these):

* ALSUTI_API_KEY - The API key necessary to post on the server.
* ALSUTI_ENDPOINT - The base endpoint for the server, not including the 'upload' path and not including a trailing slash.

If you use bash, you can simply set these in your .bashrc, if you use fish you can do similarly in the fish config file using the 'setenv' command.

## Command line client

To set up the client:

1. Ensure you have scrot and xclip installed.
2. Add ```PATH="$PATH:$HOME/alsuti/bin"``` to your ~/.bashrc, modifying it to include the relative path to the bin subdirectory in the alsuti folder, which will depend on where you cloned this repository to.
3. Then you can run alsuti whateveryouwanttoupload.gif or scroti (also supports additional scrot flags e.g. scroti -d 5)

Note: if you use fish, you can add the bin folder to your path instead by doing: ```set -gx PATH ~/alsuti/bin $PATH```

To use the encryption you will need to install nodejs and then npm install node-cryptojs-aes underscore.

## Firefox

The firefox plugin allows you to easily rehost images on an alsuti instance and then share the link. This is useful for sharing images which are transient, or behind login-walls to your friends (or enemies). You can just go ahead and right click an image, and then click 'Upload to Alsuti,' and the link will be copied to your clipboard.

![Firefox Addon Screenshot](http://reality.rehab/al/VyFaTRiox.png)

To set this up, you simply need to install the addon in your firefox: firefox/alsuti_firefox_addon-0.0.2-an+fx.xpi and set the api key and endpoint path in the addon settings (note that unlike the ALSUTI_ENDPOINT environment variable, the endpoint for the firefox plugin must be the full upload path e.g. http://reality.rehab/al/upload).
