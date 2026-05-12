const { baseUrl: BASE_URL } = require('../config/index.js');

function cleanParams(data) {
  if (!data || typeof data !== 'object') return data;
  var result = {};
  Object.keys(data).forEach(function(key) {
    if (data[key] !== null && data[key] !== undefined) {
      result[key] = data[key];
    }
  });
  return result;
}

function buildUrl(url, params) {
  if (!params || typeof params !== 'object') return url;
  var queryParts = [];
  Object.keys(params).forEach(function(key) {
    if (params[key] !== null && params[key] !== undefined) {
      queryParts.push(key + '=' + encodeURIComponent(params[key]));
    }
  });
  if (queryParts.length > 0) {
    return url + '?' + queryParts.join('&');
  }
  return url;
}

function request(options) {
  return new Promise(function(resolve, reject) {
    var token = wx.getStorageSync('token');
    var header = { 'Content-Type': 'application/json' };
    if (token) {
      header['Authorization'] = 'Bearer ' + token;
    }

    var fullUrl = buildUrl(BASE_URL + options.url, options.query);

    wx.request({
      url: fullUrl,
      method: options.method || 'GET',
      data: cleanParams(options.data),
      header: header,
      success: function(res) {
        if (res.data && res.data.code === 200) {
          resolve(res.data);
        } else {
          var msg = (res.data && res.data.message) || '请求失败';
          wx.showToast({ title: msg, icon: 'none', duration: 2500 });
          reject(res.data);
        }
      },
      fail: function(err) {
        wx.showToast({ title: '网络错误，请检查网络连接', icon: 'none', duration: 2500 });
        reject(err);
      }
    });
  });
}

module.exports = {
  get: function(url, params, query) { return request({ url: url, method: 'GET', data: params, query: query }); },
  post: function(url, data, query) { return request({ url: url, method: 'POST', data: data, query: query }); },
  put: function(url, data, query) { return request({ url: url, method: 'PUT', data: data, query: query }); },
  del: function(url, params, query) { return request({ url: url, method: 'DELETE', data: params, query: query }); }
};