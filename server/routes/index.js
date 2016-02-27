var express = require('express'),
    fs = require('fs'),
    shortid = require('shortid'),
    _ = require('underscore')._,
    router = express.Router();

/* GET home page. */
router.post('/upload', function(req, res) {
  res.setHeader('Content-Type', 'application/text');

  if(req.body.api_key == req.api_key) {
    fs.readFile(req.files.fileupload.path, function(err, data) {
      var newName = shortid.generate() + '.' + _.last(req.files.fileupload.originalname.split('.'))
          newPath = __dirname + '/../public/';

      fs.writeFile(newPath + newName, data, function(err) {
        res.send(req.external_path + newName); 
      });
    });
  } else {
    res.send('Error: Incorrect API key');
  }
});

module.exports = router;
