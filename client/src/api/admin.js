import request from '../utils/request';

export function adminLogin(data) {
  return request.post('/admin/login', data);
}

export function getAdminInfo(adminId) {
  return request.get('/admin/info', { params: { adminId } });
}

export function getProductList(params) {
  return request.get('/admin/product/list', { params });
}

export function addProduct(data) {
  return request.post('/admin/product/add', data);
}

export function updateProduct(data) {
  return request.put('/admin/product/update', data);
}

export function deleteProduct(id) {
  return request.delete(`/admin/product/${id}`);
}

export function updateProductStatus(id, status) {
  return request.put('/admin/product/status', null, { params: { id, status } });
}

export function getCategoryList() {
  return request.get('/admin/category/list');
}

export function addCategory(params) {
  return request.post('/admin/category/add', null, { params });
}

export function updateCategory(params) {
  return request.put('/admin/category/update', null, { params });
}

export function deleteCategory(id) {
  return request.delete(`/admin/category/${id}`);
}

export function getOrderList(params) {
  return request.get('/admin/order/list', { params });
}

export function getOrderDetail(id) {
  return request.get(`/admin/order/${id}`);
}

export function updateOrderStatus(orderId, status) {
  return request.put('/admin/order/status', null, { params: { orderId, status } });
}

export function getDashboardData() {
  return request.get('/admin/statistics/dashboard');
}

export function getSalesTrend(days) {
  return request.get('/admin/statistics/sales', { params: { days } });
}

export function getProductRanking(limit) {
  return request.get('/admin/statistics/ranking', { params: { limit } });
}

export function getOrderStatusStats() {
  return request.get('/admin/statistics/order-status');
}
