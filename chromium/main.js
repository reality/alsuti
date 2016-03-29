function onClick(info, tab) {
  console.log(JSON.stringify(info));
  console.log(JSON.stringify(tab));

  var localName = _.last(info.srcUrl.split('/'));

  chrome.downloads.download({
    'url': info.srcUrl,
    'filename': "/tmp/" + localName
  });

  var reader = new FileReader();

  reader.onerror = errorhandler;
  reader.onloadend = function(e) {
    console.log(e.target.result);
  });
  reader.readAsDataURL()
};

var id = chrome.contextMenus.create({
  "title": "Upload to Alsuti",
  "contexts": [ "image", "selection" ],
  "onclick": onClick
});
