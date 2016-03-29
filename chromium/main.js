var id = chrome.contextMenus.create({
  "title": "Upload to Alsuti",
  "contexts": [ "image", "selection" ],
  "onclick": onClick
});

function onClick(info, tab) {
  console.log(JSON.stringify(info));
  console.log(JSON.stringify(tab));
};
