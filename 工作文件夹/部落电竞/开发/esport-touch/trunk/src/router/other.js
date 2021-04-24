//放置其他路由:暂时未用上/已废弃/测试路由
const match = () => import('../views/match/index.vue');
const matchDetail = () => import('../views/match/detail/matchDetail.vue');


// 赛事工具
const matchTool = () => import('../views/matchtool/home.vue');
const createMatch = () => import('../views/matchtool/creatematch.vue');
const matchInfo = () => import('../views/matchtool/matchinfo.vue');
const matchRoom = () => import('../views/matchtool/matchroom.vue');
const moreTeam = () => import('../views/matchtool/moreteam.vue');
const moreSchedule = () => import('../views/matchtool/moreschedule.vue');

//游戏
// const gameShake = () => import('../views/game/shake/shake.vue');
// 动森风格
const gameShake = () => import('../views/gamenew/shake/shake.vue');
const gameLogin = () => import('../views/game/login.vue');
//约架
const pkHome = () => import('../views/pk/home/home.vue');

const homeTest = () => import('../views/index.vue');
const test = () => import('../views/home/video.vue');
const testIosPay = () => import('../views/template/testIosPay.vue');

export default [{
    path: '/match',
    name: 'match',
    component: match,
    meta: {
        keepAlive: true
    }
}, {
    path: '/matchDetail/:matchId',
    name: 'matchDetail',
    meta: {
        requiresAuth: true
    },
    component: matchDetail
}, {
    path: '/matchtool',
    name: 'matchTool',
    component: matchTool
}, {
    path: '/matchtool/createMatch',
    name: 'createMatch',
    component: createMatch
}, {
    path: '/matchtool/info',
    name: 'matchInfo',
    component: matchInfo
}, {
    path: '/matchtool/room',
    name: 'matchRoom',
    component: matchRoom
}, {
    path: '/matchtool/moreTeam',
    name: 'moreTeam',
    component: moreTeam
}, {
    path: '/matchtool/moreSchedule',
    name: 'moreSchedule',
    component: moreSchedule
}, {
    path: '/game/shake',
    name: 'gameShake',
    component: gameShake,
    meta: {
        title: '欢乐摇一摇'
    }
}, {
    path: '/game/login',
    name: 'gameLogin',
    component: gameLogin
}, {
    path: '/test',
    name: 'test',
    component: test,
    meta: {
        keepAlive: true
    }
}, {
    path: '/testIosPay',
    name: 'testIosPay',
    component: testIosPay,

}, {
    path: '/pk/home',
    name: 'pkHome',
    component: pkHome,
}, {
    path: '/homeTest',
    name: 'homeTest',
    component: homeTest,
}]
