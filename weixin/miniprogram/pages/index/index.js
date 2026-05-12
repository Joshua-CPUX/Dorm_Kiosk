Page({
  data: {
    categories: [],
    hotProducts: [],
    banners: []
  },

  onLoad() {
    this.checkLogin();
  },

  onShow() {
    this.loadData();
  },

  checkLogin() {
    const app = getApp();
    if (!app.globalData.token) {
      wx.navigateTo({ url: '/pages/login/login' });
    }
  },

  loadData() {
    const api = require('../../api/index.js');
    Promise.all([
      api.getCategoryList(),
      api.getHotProducts(10)
    ]).then(([categoryRes, productRes]) => {
      this.setData({
        categories: categoryRes.data || [],
        hotProducts: productRes.data || []
      });
    }).catch(err => {
      console.error('加载数据失败', err);
    });
  },

  goToCategory(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({ url: `/pages/product/list/list?categoryId=${id}` });
  },

  goToProduct(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({ url: `/pages/product/detail/detail?id=${id}` });
  }
});
