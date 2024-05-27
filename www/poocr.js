function OCRPlugin() {}

// Reader
OCRPlugin.prototype.startOCR = function (callback) {
	cordova.exec(function(result){ callback(result); }, function(err){}, "Plugin", "startOCR", []);
}

OCRPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.poocr = new OCRPlugin();
  return window.plugins.poocr;
};
cordova.addConstructor(OCRPlugin.install);

