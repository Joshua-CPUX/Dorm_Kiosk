import axios from 'axios';
import { ElMessage } from 'element-plus';

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
});

request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('admin_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败');
      return Promise.reject(new Error(res.message || '请求失败'));
    }
    return res;
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('admin_token');
      localStorage.removeItem('admin_id');
      window.location.href = '/login';
      return Promise.reject(error);
    }
    ElMessage.error(error.response?.data?.message || error.message || '网络错误');
    return Promise.reject(error);
  }
);

export default request;
