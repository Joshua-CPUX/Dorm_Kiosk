const imageUtil = require('../../utils/image.js');

Page({
  data: {
    banners: [],
    announcement: '',
    featuredProducts: []
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
      api.getConfigs(),
      api.getHotProducts(4)
    ]).then(([configRes, productRes]) => {
      const configs = configRes.data || {};
      const featuredProducts = (productRes.data || []).map(item => ({
        ...item,
        image: imageUtil.getImageUrl(item.image)
      }));
      this.setData({
        announcement: configs.announcement || '欢迎光临校园小卖部！',
        featuredProducts: featuredProducts
      });
    }).catch(err => {
      console.error('加载数据失败', err);
      api.getHotProducts(4).then(res => {
        const featuredProducts = (res.data || []).map(item => ({
          ...item,
          image: imageUtil.getImageUrl(item.image)
        }));
        this.setData({
          featuredProducts: featuredProducts
        });
      });
    });
  },

  goToProduct(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({ url: `/pages/product/detail/detail?id=${id}` });
  },

  addToCart(e) {
    const { id } = e.currentTarget.dataset;
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    
    if (!userId) {
      wx.navigateTo({ url: '/pages/login/login' });
      return;
    }

    const api = require('../../api/index.js');
    api.addCart(userId, { productId: id, quantity: 1 }).then(() => {
      wx.showToast({ title: '已加入购物车', icon: 'success' });
    }).catch(err => {
      console.error('加入购物车失败', err);
      wx.showToast({ title: '加入失败', icon: 'none' });
    });
  }
});