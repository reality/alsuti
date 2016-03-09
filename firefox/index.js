var self = require('sdk/self'),
    contextMenu = require('sdk/context-menu'),
    {Request} = require('sdk/request'),
    clipboard = require('sdk/clipboard'),
    notifications = require('sdk/notifications'),
    TextEncoder = require('sdk/io/buffer').TextEncoder,
    data = require("sdk/self").data,
    Panel = require("sdk/panel").Panel,
    prefs = require('sdk/simple-prefs').prefs,
    _ = require('./lib/underscore'),
    cjs = require('./lib/node-cryptojs-aes').CryptoJS;

var {Cc, components , Cu, Ci} = require("chrome");

Cu.import("resource://gre/modules/Downloads.jsm");
Cu.import("resource://gre/modules/Task.jsm");
Cu.import("resource://gre/modules/osfile.jsm")

var ios = Cc["@mozilla.org/network/io-service;1"].
          getService(Ci.nsIIOService);

//    Cc = Components.classes 
//    Ci = Components.interfaces 
//    Cu = Components.utils 
//    CC = Components.Constructor 

// Post image functions

function rawPostViaUri(imgSrc) {
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

function rawPostText(data) {
  var params = {
    'api_key': prefs.api_key,
    'content': data,
    'extension': 'txt'
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

function encPostImage(imgSrc) {
  getPassword(function(password) {
    Task.spawn(function () {
      var fName = _.last(imgSrc.split('/'));
      var tmpPath = OS.Path.join(OS.Constants.Path.tmpDir, fName);

      yield Downloads.fetch(imgSrc, tmpPath);

      var url = ios.newURI('file://'+tmpPath, null, null);

      if (!url || !url.schemeIs("file")) throw "Expected a file URL.";

      var imageFile = url.QueryInterface(Ci.nsIFileURL).file;
      var istream = Cc["@mozilla.org/network/file-input-stream;1"].
                    createInstance(Ci.nsIFileInputStream);
      istream.init(imageFile, -1, -1, false);

      var bstream = Cc["@mozilla.org/binaryinputstream;1"].
                    createInstance(Ci.nsIBinaryInputStream);
      bstream.setInputStream(istream);

      var bytes = bstream.readBytes(bstream.available());

      var cipherText = cjs.AES.encrypt(bytes, password);

      // It's difficult to upload a file in Firefox so we'll have to send the actual content
      var params = {
        'api_key': prefs.api_key,
        'content': cipherText.toString(),
        'extension': _.last(fName.split('.')),
        'encrypted': true
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
    }).then(null, function(err){ console.log(err); });
  });
}

function encPostText(text) {
  getPassword(function(password) {
    var cipherText = cjs.AES.encrypt(text, password);

    var params = {
      'api_key': prefs.api_key,
      'content': cipherText.toString(),
      'extension': 'txt',
      'encrypted': true
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
  });
}

// Util functions

function getPassword(callback) {
  var pInput = Panel({
    'contentURL': data.url('password-entry.html'),
    'contentScriptFile': data.url('get-password.js'),
    'width':300,
    'height':100
  });
  pInput.on('show', function() {
    pInput.port.emit('show'); 
  });
  pInput.port.on('text-entered', function(password) {
    pInput.hide();
    callback(password);
  });
  pInput.show()
}

// Add our menu items

contextMenu.Item({
  'label': 'Upload to Alsuti',
  'context': contextMenu.SelectionContext(),
  'contentScript': 'self.on("click", function(node, data) { self.postMessage(window.getSelection().toString()); });',
  'onMessage': rawPostText
});

contextMenu.Item({
  'label': 'Encrypt & Upload to Alsuti',
  'context': contextMenu.SelectionContext(),
  'contentScript': 'self.on("click", function(node, data) { self.postMessage(window.getSelection().toString()); });',
  'onMessage': encPostText
});

contextMenu.Item({
  'label': 'Upload to Alsuti',
  'context': contextMenu.SelectorContext('img'),
  'contentScript': 'self.on("click", function(node, data) { self.postMessage(node.src); });',
  'onMessage': rawPostViaUri
});

contextMenu.Item({
  'label': 'Encrypt & Upload to Alsuti',
  'context': contextMenu.SelectorContext('img'),
  'contentScript': 'self.on("click", function(node, data) { self.postMessage(node.src); });',
  'onMessage': encPostImage
});
