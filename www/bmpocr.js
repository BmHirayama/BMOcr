function OCRPlugin() {}

// Reader
OCRPlugin.prototype.startOCR = function (callback) {
	cordova.exec(function(result){ callback(result); }, function(err){}, "Plugin", "startOCR", []);
}

OCRPlugin.install = function() {
  if (!window.bmpocr) {
    window.bmpocr = {};
  }
  window.bmpocr = new OCRPlugin();
  return window.bmpocr.bmpocr;
};
cordova.addConstructor(OCRPlugin.install);

