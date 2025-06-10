import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/views/HomePage.vue'; // 确保路径和组件名正确
import SearchView from '../views/SearchView.vue'
import RecommendView from '../views/RecommendView.vue'
import ChatView from '../views/ChatView.vue'

import IHome from '@/views/IHome.vue'; // 新增 IHome 页面
import IDetail from '@/views/IDetail.vue';
import DoctorPage from '@/views/DoctorPage.vue';
import Doctor_Detail from '@/views/Doctor_Detail.vue';
import VideoResources from '@/views/VideoResources.vue';
import ArticleResources from '@/views/ArticleResources.vue';
import AIChat from '../views/AIChat.vue'
import Profile_Page from '@/views/Profile_Page.vue';
import TestPage from "@/views/test.vue";
import MyCollection from '@/views/MyCollection.vue';
import axios from "axios";
import store from "@/store";
import SearchPage from '@/views/SearchPage.vue';


const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage
  },
  {
    path: '/search',
    name: 'search',
    component: SearchView
  },
  {
    path: '/recommend',
    name: 'recommend',
    component: RecommendView
  },
  {
    path: '/chat',
    name: 'chat',
    component: ChatView
  },
  {
    path: '/symptoms',
    name: 'IHome',
    component: IHome
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: IDetail,
    props: true
  },
  {
    path: '/doctors',
    name: 'DoctorPage',
    component: DoctorPage
  },
  {
    path: '/doctor_detail',
    name: 'Doctor_Detail',
    component: Doctor_Detail
  },
  {
    path: '/resources/video',
    name: 'VideoResourcePage',
    component: VideoResources
  },
  {
    path: '/resources/article',
    name: 'ArticleResourcePage',
    component: ArticleResources
  },
  {
    path: '/sandbox',
    name: 'Sandbox',
    component: AIChat
  },
  {
    path: '/profile',
    name: 'ProfilePage',
    component: Profile_Page,
    //meta:{hideNavbar:true}
  },
  {
    path: '/test',
    name:'TestPage',
    component:TestPage
  },
  {
    path: '/collection',
    name: 'MyCollection',
    component: MyCollection,
    meta:{hideNavbar:true}
  },
  {
    path: '/search',
    name: 'SearchPage',
    component: SearchPage,
    props: (route) => ({ query: route.query.q }), // 将路由参数作为 props 传递
  },
  {
    path: '/paper/:id',
    name: 'PaperDetail',
    component: () => import('@/views/PaperDetail.vue')  // 根据你的项目路径调整
  },
  // Catch-all route for any other paths that are not APIs
  {
    path: '/:catchAll(.*)',
    redirect: '/404' // Redirect to a 404 page or handle it as needed
  },

];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Add a beforeEach guard to prevent Vue Router from handling /api requests
router.beforeEach((to, from, next) => {
  // 判断用户是否已经登录
  const isLoggedIn = store.state.isLoggedIn;

  // 需要登录的页面（例如 /profile 和 /collection）
  const requiresAuth = ['/profile', '/collection'].includes(to.path);

  // 如果用户未登录并且访问需要登录的页面
  if (requiresAuth && !isLoggedIn) {
    // 重定向到主页
    next('/');
  } else {
    // 允许访问
    next();
  }
  if (to.path.startsWith('/api')) {
    // 处理跨域请求，这里假设后端允许所有来源的跨域请求（实际情况中需要根据后端配置来设置正确的跨域策略）
    axios.defaults.withCredentials = true;
    axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
    axios.defaults.headers.common['Access-Control-Allow-Methods'] = 'GET, POST, PUT, DELETE';
    axios.defaults.headers.common['Access-Control-Allow-Headers'] = 'Content-Type, Authorization';

    // 允许API请求通过，但不进行路由导航（因为这是API请求，不是页面导航请求）
    next(false);
  } else {
    next();
  }
});

export default router;
