var api = require('../../../api/index.js');

var TABS = [
  { label: '全部', value: null },
  { label: '待支付', value: 1 },
  { label: '已支付', value: 2 },
  { label: '配送中', value: 3 },
  { label: '已完成', value: 4 },
  { label: '已取消', value: 5 }
];

Page({
  data: {
    tabs: TABS,
    currentTab: null,
    orderList: [],
    loading: false,
    pageNum: 1,
    pageSize: 10,
    hasMore: false,
    userId: null
  },

  onLoad: function() {
    var app = getApp();
    this.setData({ userId: app.globalData.userId });
    this.loadOrders(true);
  },

  onPullDownRefresh: function() {
    this.loadOrders(true, function() {
      wx.stopPullDownRefresh();
    });
  },

  onTabChange: function(e) {
    var value = e.currentTarget.dataset.value;
    this.setData({ currentTab: value });
    this.loadOrders(true);
  },

  loadOrders: function(reset, callback) {
    if (this.data.loading) return;
    var pageNum = reset ? 1 : this.data.pageNum;
    this.setData({ loading: true });
    var self = this;

    api.getOwnerOrderList(this.data.userId, {
      pageNum: pageNum,
      pageSize: this.data.pageSize,
      status: this.data.currentTab
    }).then(function(res) {
      var page = res.data;
      var newList = page.records || [];
      self.setData({
        orderList: reset ? newList : self.data.orderList.concat(newList),
        pageNum: pageNum + 1,
        hasMore: newList.length === self.data.pageSize,
        loading: false
      });
      if (callback) callback();
    }).catch(function(err) {
      console.error('[owner-orders] failed', JSON.stringify(err));
      self.setData({ loading: false });
      if (callback) callback();
    });
  },

  loadMore: function() {
    this.loadOrders(false);
  },

  onShip: function(e) {
    var orderId = e.currentTarget.dataset.id;
    var self = this;
    wx.showModal({
      title: '确认发货',
      content: '确认将此订单标记为配送中吗？',
      success: function(res) {
        if (res.confirm) {
          api.updateOwnerOrderStatus(self.data.userId, orderId, 3).then(function() {
            wx.showToast({ title: '已发货', icon: 'success' });
            self.loadOrders(true);
          }).catch(function(err) {
            console.error('[owner-ship] failed', JSON.stringify(err));
          });
        }
      }
    });
  },

  onComplete: function(e) {
    var orderId = e.currentTarget.dataset.id;
    var self = this;
    wx.showModal({
      title: '确认完成',
      content: '确认将此订单标记为已完成吗？',
      success: function(res) {
        if (res.confirm) {
          api.updateOwnerOrderStatus(self.data.userId, orderId, 4).then(function() {
            wx.showToast({ title: '订单已完成', icon: 'success' });
            self.loadOrders(true);
          }).catch(function(err) {
            console.error('[owner-complete] failed', JSON.stringify(err));
          });
        }
      }
    });
  }
});
