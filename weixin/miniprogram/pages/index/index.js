const imageUtil = require('../../utils/image.js');

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
      // 处理分类图片
      const categories = (categoryRes.data || []).map(item => ({
        ...item,
        icon: imageUtil.getImageUrl(item.icon)
      }));
      // 处理商品图片
      const hotProducts = (productRes.data || []).map(item => ({
        ...item,
        image: imageUtil.getImageUrl(item.image)
      }));
      this.setData({
        categories: categories,
        hotProducts: hotProducts
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
