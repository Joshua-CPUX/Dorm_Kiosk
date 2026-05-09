Page({
  data: {
    cartIds: [],
    orderType: 1,
    address: null,
    remark: '',
    totalAmount: 0,
    userId: null,
    productId: null,
    quantity: 1
  },

  onLoad(options) {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });

    if (options.cartIds) {
      this.setData({ cartIds: options.cartIds.split(',').map(id => parseInt(id)) });
    }
    if (options.productId) {
      this.setData({ productId: parseInt(options.productId), quantity: parseInt(options.quantity) || 1 });
    }
    this.loadAddress();
    this.calculateAmount();
  },

  onShow() {
    if (this.data.userId) {
      this.loadAddress();
    }
  },

  loadAddress() {
    const api = require('../../../api/index.js');
    api.getDefaultAddress(this.data.userId).then(res => {
      this.setData({ address: res.data });
    }).catch(err => {
      console.error('加载地址失败', err);
    });
  },

  calculateAmount() {
    const api = require('../../../api/index.js');
    if (this.data.cartIds && this.data.cartIds.length > 0) {
      api.getCartList(this.data.userId).then(res => {
        const cartList = res.data || [];
        const selectedItems = cartList.filter(item => this.data.cartIds.includes(item.cartId));
        let total = 0;
        selectedItems.forEach(item => {
          total += item.price * item.quantity;
        });
        this.setData({ totalAmount: total.toFixed(2) });
      }).catch(err => {
        console.error('计算金额失败', err);
      });
    } else if (this.data.productId) {
      api.getProductDetail(this.data.productId).then(res => {
        const product = res.data;
        const total = product.price * this.data.quantity;
        this.setData({ totalAmount: total.toFixed(2) });
      }).catch(err => {
        console.error('计算金额失败', err);
      });
    }
  },

  onOrderTypeChange(e) {
    this.setData({ orderType: parseInt(e.currentTarget.dataset.type) });
  },

  onRemarkInput(e) {
    this.setData({ remark: e.detail.value });
  },

  onSelectAddress() {
    wx.navigateTo({
      url: '/pages/address/address?from=confirm'
    });
  },

  onSubmit() {
    const api = require('../../../api/index.js');

    if (!this.data.address) {
      wx.showToast({ title: '请选择收货地址', icon: 'none' });
      return;
    }

    const doCreateOrder = (cartIds) => {
      const orderData = {
        cartIds: cartIds,
        orderType: this.data.orderType,
        addressId: this.data.address.id,
        remark: this.data.remark
      };

      api.createOrder(this.data.userId, orderData).then(res => {
        wx.showToast({ title: '下单成功', icon: 'success' });
        wx.redirectTo({
          url: `/pages/order/detail/detail?id=${res.data}`
        });
      }).catch(err => {
        console.error('下单失败', err);
        wx.showToast({ title: '下单失败', icon: 'none' });
      });
    };

    if (this.data.cartIds && this.data.cartIds.length > 0) {
      doCreateOrder(this.data.cartIds);
    } else if (this.data.productId) {
      api.addCart(this.data.userId, {
        productId: this.data.productId,
        quantity: this.data.quantity
      }).then(() => {
        return api.getCartList(this.data.userId);
      }).then(res => {
        const cartList = res.data || [];
        const addedItem = cartList.find(item => item.productId === this.data.productId);
        if (addedItem) {
          doCreateOrder([addedItem.cartId]);
        } else {
          wx.showToast({ title: '请先添加商品到购物车', icon: 'none' });
        }
      }).catch(err => {
        console.error('处理订单失败', err);
        wx.showToast({ title: '处理订单失败', icon: 'none' });
      });
    } else {
      wx.showToast({ title: '请选择商品', icon: 'none' });
    }
  }
});
