var api = require('../../../api/index.js');

Page({
  data: {
    activationCode: '',
    loading: false
  },

  onCodeInput: function(e) {
    this.setData({ activationCode: e.detail.value });
  },

  onActivate: function() {
    if (this.data.loading) return;
    var code = this.data.activationCode.trim();
    if (!code) {
      wx.showToast({ title: '请输入激活码', icon: 'none' });
      return;
    }

    var app = getApp();
    var userId = app.globalData.userId;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    this.setData({ loading: true });
    var self = this;

    api.activateOwner(userId, code).then(function() {
      // 更新本地用户信息的 role
      var userInfo = app.globalData.userInfo || {};
      userInfo.role = 1;
      app.globalData.userInfo = userInfo;
      wx.setStorageSync('userInfo', userInfo);

      self.setData({ loading: false });
      wx.showToast({ title: '激活成功！', icon: 'success' });
      setTimeout(function() {
        wx.navigateBack();
      }, 1000);
    }).catch(function(err) {
      console.error('[activate] failed', JSON.stringify(err));
      self.setData({ loading: false });
    });
  }
});
