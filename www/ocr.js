function BmOCRPlugin() {}

// Reader
BmOCRPlugin.prototype.startOCR = function (callback) {
	cordova.exec(function(result){ callback(result); }, function(err){}, "Plugin", "startOCR", []);
}

BmOCRPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.ocr = new BmOCRPlugin();
  return window.plugins.ocr;
};
cordova.addConstructor(BmOCRPlugin.install);

