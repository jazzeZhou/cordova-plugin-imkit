function ImKit() {
};

ImKit.prototype.Init = function (str, success, error) {
  cordova.exec(success, error, 'ImKit', 'Init', [str]);
}
ImKit.prototype.Connect = function (str, str2, success, error) {
  cordova.exec(success, error, 'ImKit', 'Connect', [str,str2]);
}
ImKit.prototype.Exit = function (success, error) {
  cordova.exec(success, error, 'ImKit', 'Exit', []);
}
ImKit.prototype.LaunchChats = function (success, error) {
  cordova.exec(success, error, 'ImKit', 'LaunchChats', []);
}
ImKit.prototype.LaunchChat = function (str1, str2, success, error) {
  cordova.exec(success, error, 'ImKit', 'LaunchChat', [str1,str2]);
}

ImKit.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.ImKit = new ImKit();
  return window.plugins.ImKit;
};

cordova.addConstructor(ImKit.install);
