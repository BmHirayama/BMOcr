var OCRPluginExport = {};

// Public
OCRPluginExport.startOCR = function(callback) {
    cordova.exec(function(result){callback(result);}, function(err){}, "Plugin", "startOCR", []);
};

module.exports = OCRPluginExport;