const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务的地址
        changeOrigin: true, // 确保跨域时 Host 头被改成目标地址
        pathRewrite: { '^/api': '/api' } // 保留 /api 前缀
      }
    }
  }
});
//修改路由配置，使得前端的/api请求能发送到后端