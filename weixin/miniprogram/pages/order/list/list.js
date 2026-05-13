Page({
  data: {
    orders: [],
    status: null,
    statusList: [
      { value: null, label: '全部' },
      { value: 1, label: '待支付' },
      { value: 2, label: '已支付' },
      { value: 3, label: '配送中' },
      { value: 4, label: '已完成' },
      { value: 5, label: '已取消' }
    ],
    pageNum: 1,
    pageSize: 10,
    hasMore: true,
    userId: null
  },

  onLoad(options) {
    if (options.status !== undefined) {
      this.setData({ status: options.status === 'null' ? null : parseInt(options.status) });
    }
  },

  onShow() {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });
    if (userId) {
      this.setData({ orders: [], pageNum: 1, hasMore: true });
      this.loadOrders();
    }
  },

  onReachBottom() {
    if (this.data.hasMore) {
      this.setData({ pageNum: this.data.pageNum + 1 });
      this.loadOrders(true);
    }
  },

  onPullDownRefresh() {
    this.setData({ pageNum: 1, orders: [], hasMore: true });
    this.loadOrders();
    wx.stopPullDownRefresh();
  },

  loadOrders(append = false) {
    const api = require('../../../api/index.js');
    api.getOrderList(this.data.userId, {
      pageNum: this.data.pageNum,
      pageSize: this.data.pageSize,
      status: this.data.status
    }).then(res => {
      const orders = append
        ? [...this.data.orders, ...(res.data.records || [])]
        : (res.data.records || []);
      this.setData({
        orders,
        hasMore: orders.length < res.data.total
      });
    }).catch(err => {
      console.error('加载订单失败', err);
    });
  },

  onStatusChange(e) {
    this.setData({ status: e.currentTarget.dataset.value, pageNum: 1, orders: [], hasMore: true });
    this.loadOrders();
  },

  goToDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/order/detail/detail?id=${id}`
    });
  }
});
