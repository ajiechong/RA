// front_end/src/store/index.js
import { createStore } from 'vuex';
import createPersistedState from 'vuex-persistedstate';  // 引入插件

export default createStore({
    state: {
        // 用户登录状态
        isLoggedIn: false,
        // 用户名
        username: '',
        // 用户头像 URL
        avatar_url: '',
        // 用户 UID
        uid: null,
    },
    mutations: {
        // 设置用户信息
        SET_USER(state, user) {
            state.isLoggedIn = true;
            state.username = user.username;
            state.avatar_url = user.avatar_url;
            state.uid = user.uid;
        },
        // 清除用户信息
        LOGOUT(state) {
            state.isLoggedIn = false;
            state.username = '';
            state.avatar_url = '';
            state.uid = null;
        },
        // 更新用户名和头像
        // UPDATE_USER_INFO(state, { username, avatar_url }) {
        //     state.username = username;
        //     state.avatar_url = avatar_url;
        // },


        UPDATE_USER_INFO(state, payload) {
            state.username = payload.username;
            state.avatar_url = payload.avatar_url;
            state.uid = payload.uid;
        },
    },
    actions: {
        // 登录操作
        async login({ commit }, user) {
            try {
                commit('SET_USER', user);  // 在验证成功后设置用户信息
                console.log("登陆成功");
            } catch (error) {
                console.error('登录失败:', error);
            }
        },
        // 退出登录操作
        logout({ commit }) {
            // 清除用户信息
            commit('LOGOUT');
            // 清除 localStorage 中的用户信息
            localStorage.removeItem('user');
        },
    },
    getters: {
        // 获取用户登录状态
        isLoggedIn: state => state.isLoggedIn,
        // 获取用户名
        username: state => state.username,
        // 获取用户头像
        avatar_url: state => state.avatar_url,
        // 获取用户 UID
        uid: state => state.uid,
    },
    plugins: [
        createPersistedState({
            storage: window.localStorage, // 使用 localStorage 来保存状态
            reducer(val) {
                // 只持久化部分状态
                return {
                    isLoggedIn: val.isLoggedIn,
                    username: val.username,
                    avatar_url: val.avatar_url,
                    uid: val.uid,
                };
            },
        }),
    ],
});
