const imageUtil = require('../../../utils/image.js');

Page({
  data: {
    categories: [],
    activeCategoryId: null,
    categoryId: null,
    keyword: '',
    products: [],
    pageNum: 1,
    pageSize: 20,
    hasMore: true,
    loading: false,
    userId: null
  },

  onLoad(options) {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });

    this.loadCategories().then(() => {
      if (options.categoryId) {
        this.setData({ 
          categoryId: parseInt(options.categoryId),
          activeCategoryId: parseInt(options.categoryId)
        });
      } else if (app.globalData.targetCategoryId) {
        this.setData({ 
          categoryId: app.globalData.targetCategoryId,
          activeCategoryId: app.globalData.targetCategoryId
        });
        app.globalData.targetCategoryId = null;
      }
      if (options.keyword) {
        this.setData({ keyword: options.keyword });
      }
      this.loadProducts();
    });
  },

  onShow() {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.setData({ pageNum: this.data.pageNum + 1 });
      this.loadProducts(true);
    }
  },

  onPullDownRefresh() {
    this.setData({ pageNum: 1, products: [], hasMore: true });
    this.loadProducts();
    wx.stopPullDownRefresh();
  },

  loadCategories() {
    const api = require('../../../api/index.js');
    return api.getCategoryList().then(res => {
      const categories = res.data || [];
      this.setData({ categories });
    }).catch(err => {
      console.error('加载分类失败', err);
      this.setData({ categories: [] });
    });
  },

  selectCategory(e) {
    const id = e.currentTarget.dataset.id;
    const newCategoryId = id === this.data.activeCategoryId ? null : id;
    this.setData({
      activeCategoryId: id,
      categoryId: newCategoryId,
      pageNum: 1,
      products: [],
      hasMore: true
    });
    this.loadProducts();
  },

  loadProducts(append = false) {
    if (this.data.loading) return;
    this.setData({ loading: true });

    const api = require('../../../api/index.js');
    api.getProductList({
      pageNum: this.data.pageNum,
      pageSize: this.data.pageSize,
      categoryId: this.data.categoryId,
      keyword: this.data.keyword
    }).then(res => {
      const rawProducts = (res.data.records || []);
      const products = rawProducts.map(item => ({
        ...item,
        image: imageUtil.getImageUrl(item.image)
      }));
      const finalProducts = append
        ? [...this.data.products, ...products]
        : products;
      this.setData({
        products: finalProducts,
        hasMore: rawProducts.length >= this.data.pageSize,
        loading: false
      });
    }).catch(err => {
      this.setData({ loading: false });
    });
  },

  onSearch(e) {
    this.setData({ 
      keyword: e.detail.value, 
      pageNum: 1, 
      products: [], 
      hasMore: true 
    });
    this.loadProducts();
  },

  onClearSearch() {
    this.setData({ 
      keyword: '', 
      pageNum: 1, 
      products: [], 
      hasMore: true 
    });
    this.loadProducts();
  },

  goToDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/product/detail/detail?id=${id}`
    });
  },

  addToCart(e) {
    const { id, index } = e.currentTarget.dataset;
    if (!this.data.userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    const api = require('../../../api/index.js');
    api.addCart(this.data.userId, { productId: id, quantity: 1 }).then(() => {
      wx.showToast({ title: '已加入购物车', icon: 'success' });
      const products = this.data.products;
      products[index].cartCount = (products[index].cartCount || 0) + 1;
      this.setData({ products });
    }).catch(err => {
      wx.showToast({ title: '添加失败', icon: 'none' });
    });
  }
});