var self = require('sdk/self'),
    contextMenu = require('sdk/context-menu'),
    {Request} = require('sdk/request'),
    clipboard = require('sdk/clipboard'),
    notifications = require('sdk/notifications'),
    prefs = require('sdk/simple-prefs').prefs,
    _ = require('underscore')._;

var menuItem = contextMenu.Item({
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
