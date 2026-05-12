// 环境切换：将 ENV 改为 'prod' 切换到生产环境
const ENV = 'dev';

const config = {
  dev: {
    baseUrl: 'http://localhost:8080/api'
  },
  prod: {
    baseUrl: 'http://localhost:8080/api'  // 生产部署时替换为服务器 IP 或域名
  }
};

module.exports = config[ENV];
