var request = require('../utils/request');

module.exports = {
  login: function(data) { return request.post('/user/login', data); },
  register: function(data) { return request.post('/user/register', data); },
  getUserInfo: function(userId) { return request.get('/user/info', null, { userId: userId }); },

  getCategoryList: function() { return request.get('/category/list'); },
  getProductList: function(params) { return request.get('/product/list', null, params); },
  getProductDetail: function(id) { return request.get('/product/' + id); },
  getHotProducts: function(limit) { return request.get('/product/hot', null, { limit: limit }); },

  getCartList: function(userId) { return request.get('/cart/list', null, { userId: userId }); },
  addCart: function(userId, data) { return request.post('/cart/add', data, { userId: userId }); },
  updateCart: function(userId, data) { return request.put('/cart/update', data, { userId: userId }); },
  deleteCart: function(userId, id) { return request.del('/cart/' + id, null, { userId: userId }); },

  getAddressList: function(userId) { return request.get('/address/list', null, { userId: userId }); },
  getDefaultAddress: function(userId) { return request.get('/address/default', null, { userId: userId }); },
  addAddress: function(userId, data) { return request.post('/address/add', data, { userId: userId }); },
  updateAddress: function(userId, data) { return request.put('/address/update', data, { userId: userId }); },
  deleteAddress: function(userId, id) { return request.del('/address/' + id, null, { userId: userId }); },
  setDefaultAddress: function(userId, id) { return request.put('/address/default/' + id, null, { userId: userId }); },

  createOrder: function(userId, data) { return request.post('/order/create', data, { userId: userId }); },
  mockPay: function(userId, orderId) { return request.post('/order/mock-pay', null, { userId: userId, orderId: orderId }); },
  getOrderList: function(userId, params) { return request.get('/order/list', null, { userId: userId, ...params }); },
  getOrderDetail: function(userId, id) { return request.get('/order/' + id, null, { userId: userId }); },
  cancelOrder: function(userId, id) { return request.put('/order/cancel/' + id, null, { userId: userId }); },
  confirmOrder: function(userId, id) { return request.put('/order/confirm/' + id, null, { userId: userId }); }
};