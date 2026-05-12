var request = require('../utils/request');

module.exports = {
  // 用户
  login: function(data) { return request.post('/user/login', data); },
  register: function(data) { return request.post('/user/register', data); },
  getUserInfo: function(userId) { return request.get('/user/info', null, { userId: userId }); },
  wxLogin: function(data) { return request.post('/user/wx-login', data); },
  activateOwner: function(userId, activationCode) {
    return request.post('/user/activate-owner', null, { userId: userId, activationCode: activationCode });
  },

  // 分类
  getCategoryList: function() { return request.get('/category/list'); },

  // 商品
  getProductList: function(params) { return request.get('/product/list', null, params); },
  getProductDetail: function(id) { return request.get('/product/' + id); },
  getHotProducts: function(limit) { return request.get('/product/hot', null, { limit: limit }); },

  // 购物车
  getCartList: function(userId) { return request.get('/cart/list', null, { userId: userId }); },
  addCart: function(userId, data) { return request.post('/cart/add', data, { userId: userId }); },
  updateCart: function(userId, data) { return request.put('/cart/update', data, { userId: userId }); },
  deleteCart: function(userId, id) { return request.del('/cart/' + id, null, { userId: userId }); },

  // 地址
  getAddressList: function(userId) { return request.get('/address/list', null, { userId: userId }); },
  getDefaultAddress: function(userId) { return request.get('/address/default', null, { userId: userId }); },
  addAddress: function(userId, data) { return request.post('/address/add', data, { userId: userId }); },
  updateAddress: function(userId, data) { return request.put('/address/update', data, { userId: userId }); },
  deleteAddress: function(userId, id) { return request.del('/address/' + id, null, { userId: userId }); },
  setDefaultAddress: function(userId, id) { return request.put('/address/default/' + id, null, { userId: userId }); },

  // 订单（用户端）
  createOrder: function(userId, data) { return request.post('/order/create', data, { userId: userId }); },
  mockPay: function(userId, orderId) { return request.post('/order/mock-pay', null, { userId: userId, orderId: orderId }); },
  getOrderList: function(userId, params) { return request.get('/order/list', null, { userId: userId, ...params }); },
  getOrderDetail: function(userId, id) { return request.get('/order/' + id, null, { userId: userId }); },
  cancelOrder: function(userId, id) { return request.put('/order/cancel/' + id, null, { userId: userId }); },
  confirmOrder: function(userId, id) { return request.put('/order/confirm/' + id, null, { userId: userId }); },

  // 店主端
  getOwnerOrderList: function(userId, params) {
    return request.get('/owner/order/list', null, { userId: userId, ...params });
  },
  updateOwnerOrderStatus: function(userId, orderId, status) {
    return request.put('/owner/order/status', null, { userId: userId, orderId: orderId, status: status });
  },
  getOwnerProductList: function(userId, params) {
    return request.get('/owner/product/list', null, { userId: userId, ...params });
  },
  updateOwnerProductStatus: function(userId, id, status) {
    return request.put('/owner/product/status', null, { userId: userId, id: id, status: status });
  }
};
