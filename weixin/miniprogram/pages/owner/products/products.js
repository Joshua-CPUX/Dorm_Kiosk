var api = require('../../../api/index.js');

Page({
  data: {
    productList: [],
    keyword: '',
    loading: false,
    pageNum: 1,
    pageSize: 20,
    hasMore: false,
    userId: null
  },

  onLoad: function() {
    var app = getApp();
    this.setData({ userId: app.globalData.userId });
    this.loadProducts(true);
  },

  onPullDownRefresh: function() {
    this.loadProducts(true, function() {
      wx.stopPullDownRefresh();
    });
  },

  onKeywordInput: function(e) {
    this.setData({ keyword: e.detail.value });
  },

  onSearch: function() {
    this.loadProducts(true);
  },

  loadProducts: function(reset, callback) {
    if (this.data.loading) return;
    var pageNum = reset ? 1 : this.data.pageNum;
    this.setData({ loading: true });
    var self = this;

    api.getOwnerProductList(this.data.userId, {
      pageNum: pageNum,
      pageSize: this.data.pageSize,
      keyword: this.data.keyword || undefined
    }).then(function(res) {
      var page = res.data;
      var newList = page.records || [];
      self.setData({
        productList: reset ? newList : self.data.productList.concat(newList),
        pageNum: pageNum + 1,
        hasMore: newList.length === self.data.pageSize,
        loading: false
      });
      if (callback) callback();
    }).catch(function(err) {
      console.error('[owner-products] failed', JSON.stringify(err));
      self.setData({ loading: false });
      if (callback) callback();
    });
  },

  loadMore: function() {
    this.loadProducts(false);
  },

  onToggleStatus: function(e) {
    var id = e.currentTarget.dataset.id;
    var currentStatus = e.currentTarget.dataset.status;
    var newStatus = currentStatus === 1 ? 0 : 1;
    var actionText = newStatus === 1 ? '上架' : '下架';
    var self = this;

    wx.showModal({
      title: '确认操作',
      content: '确认' + actionText + '该商品吗？',
      success: function(res) {
        if (res.confirm) {
          api.updateOwnerProductStatus(self.data.userId, id, newStatus).then(function() {
            wx.showToast({ title: actionText + '成功', icon: 'success' });
            self.loadProducts(true);
          }).catch(function(err) {
            console.error('[owner-product-status] failed', JSON.stringify(err));
          });
        }
      }
    });
  }
});
