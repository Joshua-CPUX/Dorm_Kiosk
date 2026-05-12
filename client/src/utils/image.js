// 图片 URL 处理工具
export function getImageUrl(url) {
  if (!url) return '';
  // 如果已经是完整 URL，直接返回
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  // 如果是本地上传路径，拼接完整地址
  if (url.startsWith('/uploads/')) {
    let baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
    // 移除末尾的 /api 后缀
    if (baseUrl.endsWith('/api')) {
      baseUrl = baseUrl.substring(0, baseUrl.length - 4);
    }
    return baseUrl + url;
  }
  return url;
}
