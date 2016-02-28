var express = require('express'),
    fs = require('fs'),
    shortid = require('shortid'),
    _ = require('underscore')._,
    request = require('request'),
    router = express.Router();

/* GET home page. */
router.post('/upload', function(req, res) {
  res.setHeader('Content-Type', 'application/text');
  console.log(req.body);

  if(req.body.api_key == req.api_key) {
    if(_.has(req.files, 'fileupload')) {
      fs.readFile(req.files.fileupload.path, function(err, data) {
        var newName = shortid.generate() + '.' + _.last(req.files.fileupload.originalname.split('.'))
            newPath = __dirname + '/../public/';

        fs.writeFile(newPath + newName, data, function(err) {
          res.send(req.external_path + '/' + newName); 
        });
      });
    } else if(_.has(req.body, 'uri')) {
      var newName = shortid.generate() + '.' + _.last(req.body.uri.split('.')).replace(/\?.*$/,'').replace(/:.*$/,'')
          newPath = __dirname + '/../public/';

      request.get(req.body.uri).pipe(fs.createWriteStream(newPath + newName))
        .on('close', function() {
          res.send(req.external_path + '/' + newName); 
        });
    }
  } else {
    res.send('Error: Incorrect API key');
  }
});

module.exports = router;
