Page({
  data: {
    order: {},
    userId: null
  },

  onLoad(options) {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });
    if (options.id) {
      this.loadOrderDetail(options.id);
    }
  },

  loadOrderDetail(id) {
    const api = require('../../../api/index.js');
    api.getOrderDetail(this.data.userId, parseInt(id)).then(res => {
      this.setData({ order: res.data });
    }).catch(err => {
      console.error('加载订单详情失败', err);
    });
  },

  onCancel() {
    wx.showModal({
      title: '提示',
      content: '确定要取消该订单吗？',
      success: (res) => {
        if (res.confirm) {
          const api = require('../../../api/index.js');
          api.cancelOrder(this.data.userId, this.data.order.id).then(() => {
            wx.showToast({ title: '订单已取消', icon: 'success' });
            this.loadOrderDetail(this.data.order.id);
          }).catch(err => {
            console.error('取消订单失败', err);
          });
        }
      }
    });
  },

  onConfirm() {
    wx.showModal({
      title: '提示',
      content: '确定已收到货品吗？',
      success: (res) => {
        if (res.confirm) {
          const api = require('../../../api/index.js');
          api.confirmOrder(this.data.userId, this.data.order.id).then(() => {
            wx.showToast({ title: '已确认收货', icon: 'success' });
            this.loadOrderDetail(this.data.order.id);
          }).catch(err => {
            console.error('确认收货失败', err);
          });
        }
      }
    });
  },

  onPay() {
    wx.showModal({
      title: '模拟支付',
      content: '确定要支付 ¥' + this.data.order.payAmount + ' 吗？\n（测试环境：模拟微信支付）',
      success: (res) => {
        if (res.confirm) {
          const api = require('../../../api/index.js');
          api.mockPay(this.data.userId, this.data.order.id).then(() => {
            wx.showToast({ title: '支付成功', icon: 'success' });
            this.loadOrderDetail(this.data.order.id);
          }).catch(err => {
            console.error('支付失败', err);
            wx.showToast({ title: '支付失败', icon: 'none' });
          });
        }
      }
    });
  }
});
