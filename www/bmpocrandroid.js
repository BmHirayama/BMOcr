function BMOcrAndroidPlugin() {}

// Reader
BMOcrAndroidPlugin.prototype.startOCR = function (callback) {
	cordova.exec(function(result){ callback(result); }, function(err){}, "Plugin", "startOCR", []);
}

BMOcrAndroidPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.bmpocrandroid = new BMOcrAndroidPlugin();
  return window.plugins.bmpocrandroid;
};
cordova.addConstructor(BMOcrAndroidPlugin.install);