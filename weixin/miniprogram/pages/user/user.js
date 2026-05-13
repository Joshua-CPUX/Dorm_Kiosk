Page({
  data: {
    userInfo: {},
    logoTapCount: 0,
    logoTapTimer: null
  },

  onShow() {
    var app = getApp();
    if (!app.globalData.token) {
      wx.navigateTo({ url: '/pages/login/login' });
      return;
    }
    var cached = app.globalData.userInfo;
    if (cached) {
      this.setData({ userInfo: cached });
    }
    this.loadUserInfo(app.globalData.userId);
  },

  loadUserInfo(userId) {
    if (!userId) return;
    var api = require('../../api/index.js');
    var self = this;
    api.getUserInfo(userId).then(function(res) {
      self.setData({ userInfo: res.data });
      var app = getApp();
      app.globalData.userInfo = res.data;
      wx.setStorageSync('userInfo', res.data);
    }).catch(function(err) {
      console.error('加载用户信息失败', err);
    });
  },

  goToOrders(e) {
    let url = '/pages/order/list/list';
    if (e && e.currentTarget && e.currentTarget.dataset && e.currentTarget.dataset.status !== undefined) {
      url += '?status=' + e.currentTarget.dataset.status;
    }
    wx.navigateTo({ url: url });
  },

  goToAddress() {
    wx.navigateTo({ url: '/pages/address/address' });
  },

  goToOwnerOrders() {
    wx.navigateTo({ url: '/pages/owner/orders/orders' });
  },

  goToOwnerProducts() {
    wx.navigateTo({ url: '/pages/owner/products/products' });
  },

  // 连续点击 Logo 5 次触发店主激活入口
  onLogoTap() {
    var self = this;
    var count = this.data.logoTapCount + 1;
    this.setData({ logoTapCount: count });

    if (this.data.logoTapTimer) clearTimeout(this.data.logoTapTimer);

    if (count >= 5) {
      this.setData({ logoTapCount: 0 });
      wx.navigateTo({ url: '/pages/owner/activate/activate' });
      return;
    }

    var timer = setTimeout(function() {
      self.setData({ logoTapCount: 0 });
    }, 2000);
    this.setData({ logoTapTimer: timer });
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
