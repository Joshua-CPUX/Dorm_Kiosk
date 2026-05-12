const config = require('../config/index.js');

function getImageUrl(url) {
  if (!url) return '';
  // 如果已经是完整 URL，直接返回
  if (url.indexOf('http://') === 0 || url.indexOf('https://') === 0) {
    return url;
  }
  // 如果是本地上传路径，拼接完整地址
  if (url.indexOf('/uploads/') === 0) {
    // 获取基础地址，去掉 /api 后缀
    let baseUrl = config.baseUrl;
    if (baseUrl.indexOf('/api') === baseUrl.length - 4) {
      baseUrl = baseUrl.substring(0, baseUrl.length - 4);
    }
    return baseUrl + url;
  }
  // 其他情况（可能是 emoji 或旧数据），返回空字符串，不显示图片
  return '';
}

module.exports = {
  getImageUrl: getImageUrl
};
