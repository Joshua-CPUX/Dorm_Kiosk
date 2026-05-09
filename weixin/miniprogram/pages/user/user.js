Page({
  data: {
    userInfo: {}
  },

  onShow() {
    const app = getApp();
    if (!app.globalData.token) {
      wx.navigateTo({ url: '/pages/login/login' });
      return;
    }
    const cached = app.globalData.userInfo;
    if (cached) {
      this.setData({ userInfo: cached });
    }
    this.loadUserInfo(app.globalData.userId);
  },

  loadUserInfo(userId) {
    if (!userId) {
      console.error('[user] userId is null, cannot load user info');
      return;
    }
    const api = require('../../api/index.js');
    api.getUserInfo(userId).then(res => {
      this.setData({ userInfo: res.data });
      const app = getApp();
      app.globalData.userInfo = res.data;
      wx.setStorageSync('userInfo', res.data);
    }).catch(err => {
      console.error('加载用户信息失败', err);
    });
  },

  goToOrders() {
    wx.navigateTo({ url: '/pages/order/list/list' });
  },

  goToAddress() {
    wx.navigateTo({ url: '/pages/address/address' });
  },

  onLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          getApp().clearUserData();
          wx.reLaunch({ url: '/pages/login/login' });
        }
      }
    });
  }
});
