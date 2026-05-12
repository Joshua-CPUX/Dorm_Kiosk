Page({
  data: {
    id: null,
    product: {},
    quantity: 1,
    userId: null
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ id: parseInt(options.id) });
      this.loadProductDetail();
    }
    const app = getApp();
    this.setData({ userId: app.globalData.userId || wx.getStorageSync('userId') });
  },

  loadProductDetail() {
    const api = require('../../../api/index.js');
    api.getProductDetail(this.data.id).then(res => {
      this.setData({ product: res.data });
    }).catch(err => {
      console.error('加载商品详情失败', err);
    });
  },

  onAddCart() {
    if (!this.data.userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    const api = require('../../../api/index.js');
    api.addCart(this.data.userId, {
      productId: this.data.id,
      quantity: this.data.quantity
    }).then(() => {
      wx.showToast({ title: '已加入购物车', icon: 'success' });
    }).catch(err => {
      console.error('加入购物车失败', err);
    });
  },

  onBuyNow() {
    if (!this.data.userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    wx.navigateTo({
      url: `/pages/order/confirm/confirm?productId=${this.data.id}&quantity=${this.data.quantity}`
    });
  },

  onQuantityChange(e) {
    const type = e.currentTarget.dataset.type;
    let qty = this.data.quantity;
    if (type === 'plus') {
      if (qty >= this.data.product.stock) return;
      qty++;
    } else {
      if (qty <= 1) return;
      qty--;
    }
    this.setData({ quantity: qty });
  },

  goToHome() {
    wx.switchTab({ url: '/pages/index/index' });
  },

  goToCart() {
    wx.switchTab({ url: '/pages/cart/cart' });
  }
});
