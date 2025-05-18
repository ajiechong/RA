// main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';  // 引入 Vuex store
import './assets/global.css';
import 'element-plus/dist/index.css'; // 引入样式文件
import ElementPlus from 'element-plus'; // 确保导入 ElementPlus

// 创建 Vue 应用并使用 router 和 store
createApp(App)
    .use(router)  // 使用 Vue Router
    .use(store)   // 使用 Vuex Store
    .use(ElementPlus) // 使用 Element Plus
    .mount('#app'); // 挂载应用