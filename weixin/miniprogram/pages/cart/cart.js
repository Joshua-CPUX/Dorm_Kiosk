const imageUtil = require('../../utils/image.js');

Page({
  data: {
    cartList: [],
    totalAmount: 0,
    selectedIds: [],
    allSelected: false,
    userId: null
  },

  onShow() {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });
    if (userId) {
      this.loadCart(userId);
    }
  },

  loadCart(userId) {
    const api = require('../../api/index.js');
    const effectiveUserId = userId || this.data.userId;
    api.getCartList(effectiveUserId).then(res => {
      // 处理商品图片
      const cartList = (res.data || []).map(item => ({
        ...item,
        image: imageUtil.getImageUrl(item.image),
        selected: this.data.selectedIds.includes(item.cartId)
      }));
      this.setData({ cartList });
      this.calculateTotal();
    }).catch(err => {
      console.error('加载购物车失败', err);
    });
  },

  calculateTotal() {
    let total = 0;
    this.data.cartList.forEach(item => {
      if (item.selected) {
        total += item.price * item.quantity;
      }
    });
    this.setData({ totalAmount: total.toFixed(2) });
  },

  onSelectItem(e) {
    const { id } = e.currentTarget.dataset;
    const cartList = this.data.cartList.map(item => {
      if (item.cartId === id) {
        item.selected = !item.selected;
      }
      return item;
    });
    const selectedIds = cartList.filter(item => item.selected).map(item => item.cartId);
    this.setData({ cartList, selectedIds: selectedIds });
    this.calculateTotal();
  },

  onSelectAll() {
    const allSelected = !this.data.allSelected;
    const cartList = this.data.cartList.map(item => ({ ...item, selected: allSelected }));
    const selectedIds = allSelected ? cartList.map(item => item.cartId) : [];
    this.setData({ cartList, allSelected, selectedIds });
    this.calculateTotal();
  },

  onQuantityChange(e) {
    const { id, value } = e.currentTarget.dataset;
    const api = require('../../api/index.js');
    const userId = this.data.userId;
    api.updateCart(userId, { id, quantity: parseInt(value) }).then(() => {
      this.loadCart(userId);
    }).catch(err => {
      console.error('更新数量失败', err);
    });
  },

  onDeleteItem(e) {
    const { id } = e.currentTarget.dataset;
    const userId = this.data.userId;
    wx.showModal({
      title: '提示',
      content: '确定要删除该商品吗？',
      success: (res) => {
        if (res.confirm) {
          const api = require('../../api/index.js');
          api.deleteCart(userId, id).then(() => {
            wx.showToast({ title: '删除成功', icon: 'success' });
            this.loadCart(userId);
          }).catch(err => {
            console.error('删除失败', err);
          });
        }
      }
    });
  },

  onCheckout() {
    if (this.data.selectedIds.length === 0) {
      wx.showToast({ title: '请选择商品', icon: 'none' });
      return;
    }

    const userId = this.data.userId;
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    const selectedCartIds = this.data.selectedIds.join(',');
    wx.navigateTo({
      url: `/pages/order/confirm/confirm?cartIds=${selectedCartIds}`
    });
  }
});
