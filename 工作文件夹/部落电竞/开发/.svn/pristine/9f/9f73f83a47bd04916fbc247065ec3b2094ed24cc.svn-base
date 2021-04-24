//活动模块
const pullNews = () => import('../views/hd/hd104/pullnews/pullNews.vue');
const guessRank = () => import('../views/hd/hd105/rank/rank.vue');

// 活动101
const hd101Home = () => import('../views/hd/hd101/home.vue');
const hd101Answering = () => import('../views/hd/hd101/answering.vue');
const hd101Ruleslist = () => import('../views/hd/hd101/ruleslist.vue');
const hd101Userwallet = () => import('../views/hd/hd101/userwallet.vue');
const hd101Record = () => import('../views/hd/hd101/record.vue');

export default [{
    path: '/hd/hd104/pullNews',
    name: 'pullNews',
    component: pullNews
}, {
    path: '/hd/hd105/rank',
    name: 'guessRank',
    component: guessRank
}, {
    path: '/hd/hd101/home',
    name: 'hd101Home',
    component: hd101Home,
    meta: {
        requiresAuth: true,
        title: '一站到底答题赢大奖'
    }
}, {
    path: '/hd/hd101/answering',
    name: 'hd101Answering',
    component: hd101Answering,
    meta: {
        requiresAuth: true,
        pageShare: true
    }
}, {
    path: '/hd/hd101/ruleslist',
    name: 'hd101Ruleslist',
    component: hd101Ruleslist,
    meta: {
        requiresAuth: true
    }
}, {
    path: '/hd/hd101/userwallet',
    name: 'hd101Userwallet',
    component: hd101Userwallet,
    meta: {
        requiresAuth: true
    }
}, {
    path: '/hd/hd101/record',
    name: 'hd101Record',
    component: hd101Record,
    meta: {
        requiresAuth: true
    }
}]
