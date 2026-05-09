App({
  globalData: {
    userId: null,
    token: null,
    userInfo: null
  },

  onLaunch() {
    const token = wx.getStorageSync('token');
    const userId = wx.getStorageSync('userId');
    const userInfo = wx.getStorageSync('userInfo');
    if (token && userId) {
      this.globalData.token = token;
      this.globalData.userId = userId;
      this.globalData.userInfo = userInfo || null;
    }
  },

  setUserData(token, userId, userInfo) {
    this.globalData.token = token;
    this.globalData.userId = userId;
    this.globalData.userInfo = userInfo || null;
    wx.setStorageSync('token', token);
    wx.setStorageSync('userId', userId);
    wx.setStorageSync('userInfo', userInfo || {});
  },

  clearUserData() {
    this.globalData.token = null;
    this.globalData.userId = null;
    this.globalData.userInfo = null;
    wx.removeStorageSync('token');
    wx.removeStorageSync('userId');
    wx.removeStorageSync('userInfo');
  }
})
