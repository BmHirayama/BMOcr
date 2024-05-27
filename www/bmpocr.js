var exec = require('cordova/exec');
var OCRPluginExport = {};

// Public
OCRPluginExport.startOCR = function(callback) {
    exec(function(result){callback(result);}, function(err){}, "Plugin", "startOCR", []);
};

module.exports = OCRPluginExport;