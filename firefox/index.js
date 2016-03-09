var self = require('sdk/self'),
    contextMenu = require('sdk/context-menu'),
    {Request} = require('sdk/request'),
    clipboard = require('sdk/clipboard'),
    notifications = require('sdk/notifications'),
    TextEncoder = require('sdk/io/buffer').TextEncoder,
    prefs = require('sdk/simple-prefs').prefs,
    _ = require('./underscore'),
    cjs = require('node-cryptojs-aes').CryptoJS;



var {Cc, components , Cu, Ci} = require("chrome");

Cu.import("resource://gre/modules/Downloads.jsm");
//Cu.import("resource://gre/modules/osfile.jsm");
Cu.import("resource://gre/modules/Task.jsm");
Cu.import("resource://gre/modules/osfile.jsm")

var ios = Cc["@mozilla.org/network/io-service;1"].
          getService(Ci.nsIIOService);

//    Cc = Components.classes 
//    Ci = Components.interfaces 
//    Cu = Components.utils 
//    CC = Components.Constructor 

var rawMenuItem = contextMenu.Item({
  'label': 'Upload to Alsuti',
  'context': contextMenu.SelectorContext('img'),
  'contentScript': 'self.on("click", function(node, data) { self.postMessage(node.src); });',
  'onMessage': function(imgSrc) {
    if(!_.has(prefs, 'api_key') || !_.has(prefs, 'endpoint')) {
      return notifications.notify({
        'title': 'Alsuti',
        'text': 'You must first set an alsuti endpoint and API key in the preferences.'
      });
    }

    var params = {
      'api_key': prefs.api_key,
      'uri': imgSrc
    };

    Request({
      'url': prefs.endpoint,
      'content': params,
      'onComplete': function(response) {
        clipboard.set(response.text);
        notifications.notify({
          'title': 'Alsuti',
          'text': response.text + ' (copied to clipboard)'
        });
      }
    }).post();
  }
});

var encryptdMenuItem = contextMenu.Item({
  'label': 'Encrypt & Upload to Alsuti',
  'context': contextMenu.SelectorContext('img'),
  'contentScript': 'self.on("click", function(node, data) { self.postMessage(node.src); });',
  'onMessage': function(imgSrc) {
    if(!_.has(prefs, 'api_key') || !_.has(prefs, 'endpoint')) {
      return notifications.notify({
        'title': 'Alsuti',
        'text': 'You must first set an alsuti endpoint and API key in the preferences.'
      });
    }

    // First we must download the file
	Task.spawn(function () {
      console.log('downlinading file ' + imgSrc + ' to ' + OS.Constants.Path.tmpDir);
      var tmpPath = OS.Path.join(OS.Constants.Path.tmpDir, _.last(imgSrc.split('/')));
	  yield Downloads.fetch(imgSrc, tmpPath);

	  // now i have the file, we have to load it
      console.log('got file ' + tmpPath);

	  var url = ios.newURI('file://'+tmpPath, null, null);

      console.log('created uri 1');
	  if (!url || !url.schemeIs("file")) throw "Expected a file URL.";

      console.log('created uri 2');

	  var imageFile = url.QueryInterface(Ci.nsIFileURL).file;
	  var istream = Cc["@mozilla.org/network/file-input-stream;1"].
					createInstance(Ci.nsIFileInputStream);
	  istream.init(imageFile, -1, -1, false);

      console.log('init stream');

	  var bstream = Cc["@mozilla.org/binaryinputstream;1"].
					createInstance(Ci.nsIBinaryInputStream);
	  bstream.setInputStream(istream);

      console.log('init bitstream');
      
      var bytes = bstream.readBytes(bstream.available());

	  var cipherText = cjs.AES.encrypt(bytes, 'password');

      // It's difficult to upload a file in Firefox so we'll have to send the actual content

      var encoder = new TextEncoder();                                   // This encoder can be reused for several writes
      var array = encoder.encode(cipherText);                   // Convert the text to an array
      var promise = OS.File.writeAtomic(OS.Path.join(OS.Constants.Path.tmpDir, _.last(imgSrc.split('/')) + '.enc'),
            array, {noOverwrite: true});   
        promise = promise.then(
          function onSuccess(a) {
            console.log('yay');
          },
          function onFailure(msg) {
            console.log(msg);
          }
        );
		
	}).then(null, Cu.reportError);
/*
    var params = {
      'api_key': prefs.api_key,
      'uri': imgSrc
    };

    Request({
      'url': prefs.endpoint,
      'content': params,
      'onComplete': function(response) {
        clipboard.set(response.text);
        notifications.notify({
          'title': 'Alsuti',
          'text': response.text + ' (copied to clipboard)'
        });
      }
    }).post();*/
  }
});
