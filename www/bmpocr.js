function BMOcr() {};

// Reader
BMOcr.prototype.startOCR = function (callback) {
    cordova.exec(function(result){ callback(result); }, function(err){}, "Plugin", "startOCR", []);
};

BMOcr.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.bmpocr = new BMOcr();
  return window.plugins.bmpocr;
};
cordova.addConstructor(BMOcr.install);