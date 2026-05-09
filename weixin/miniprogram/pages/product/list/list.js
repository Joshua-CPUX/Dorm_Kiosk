Page({
  data: {
    categoryId: null,
    keyword: '',
    products: [],
    pageNum: 1,
    pageSize: 10,
    hasMore: true,
    loading: false
  },

  onLoad(options) {
    if (options.categoryId) {
      this.setData({ categoryId: parseInt(options.categoryId) });
    }
    if (options.keyword) {
      this.setData({ keyword: options.keyword });
    }
    this.loadProducts();
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
      const products = append
        ? [...this.data.products, ...(res.data.records || [])]
        : (res.data.records || []);
      this.setData({
        products,
        hasMore: products.length < res.data.total,
        loading: false
      });
    }).catch(err => {
      this.setData({ loading: false });
    });
  },

  goToDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/product/detail/detail?id=${id}`
    });
  },

  onSearch(e) {
    this.setData({ keyword: e.detail.value, pageNum: 1, products: [], hasMore: true });
    this.loadProducts();
  }
});
