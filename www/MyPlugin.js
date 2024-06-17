var exec = require('cordova/exec');

var MyPlugin = {
    coolMethod: function(success) {
        exec(success, function(err){}, "MyPlugin", "coolMethod", "message");
    }
};

module.exports = MyPlugin;
