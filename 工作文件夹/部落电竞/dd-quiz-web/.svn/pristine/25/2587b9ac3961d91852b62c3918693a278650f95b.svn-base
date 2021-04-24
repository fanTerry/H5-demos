// 预测
const guessHome = () => import('./views/guess/home/home.vue');
const guessRules = () => import('./views/guess/rules.vue');
const allGuess = () => import('./views/guess/allGuess.vue');
const guessRecord = () => import('./views/guess/record.vue');
const guessSign = () => import('./views/guess/sign.vue');
const guessRecharge = () => import('./views/guess/recharge/recharge.vue');
const ddBjnews = () => import('./views/guess/bjnews.vue');
const pullNews = () => import('./views/hd/hd104/pullnews/pullNews.vue');
const guessRank = () => import('./views/hd/hd105/rank/rank.vue');

//用户中心
const userCenter = () => import('./views/user_center/index.vue');
const wallet = () => import('./views/user_center/wallet/myWallet.vue');
const recharge = () => import('./views/user_center/recharge/myRecharge.vue');
const iosRecharge = () => import('./views/user_center/recharge/iosRecharge.vue');
const login = () => import('./views/user_center/login/myLogin.vue');
const agreement = () => import('./views/user_center/login/agreement.vue');
const pravicy = () => import('./views/user_center/login/safe.vue');
const register = () => import('./views/user_center/register/myRegister.vue');
const registerUserinfo = () => import('./views/user_center/register/userInfo/myRegisterUserinfo.vue');
const userInfo = () => import('./views/user_center/user_info/userInfo.vue');
const bindPhone = () => import('./views/user_center/user_info/bindPhone.vue');
const resetPasswordValidCode = () => import('./views/user_center/resetPassword/validCode.vue');
const resetPasswordConfirm = () => import('./views/user_center/resetPassword/confirm.vue');
// const userAuth = () => import('./views/user_center/user_info/userAuth.vue');

// 帮助中心
const helpcenter = () => import('./views/helpcenter/help.vue');
const questionList = () => import('./views/helpcenter/questionList.vue');
const feedBackList = () => import('./views/helpcenter/feedBackList.vue');
const feedBackSubmit = () => import('./views/helpcenter/feedBackSubmit.vue');
const feedBackSuccess = () => import('./views/helpcenter/feedBackSuccess.vue');
const myFeedBack = () => import('./views/helpcenter/myFeedBack.vue');
const feedBackDetail = () => import('./views/helpcenter/feedBackDetail.vue');

//游戏
const gameShake = () => import('./views/game/shake/shake.vue');
const gameLogin = () => import('./views/game/login.vue');


// 活动101
// const hd101Home = () => import('./views/hd/hd101/home.vue');
// const hd101Answering = () => import('./views/hd/hd101/answering.vue');

// const hd101Ruleslist = () => import('./views/hd/hd101/ruleslist.vue');
// const hd101Userwallet = () => import('./views/hd/hd101/userwallet.vue');
// const hd101Record = () => import('./views/hd/hd101/record.vue');

// const test = () => import('./views/home/video.vue');
// const testIosPay = () => import('./views/template/testIosPay.vue');

const routers = [{
    path: '/',
    component: guessHome,
    meta: {
        keepAlive: true
    }
}, {
    path: '/userCenter',
    name: 'userCenter',
    component: userCenter,
    // meta: { requiresAuth: true, keepAlive: true }

}, {
    path: '/wallet',
    name: 'wallet',
    component: wallet,
    meta: {
        requiresAuth: true
    }
}, {
    path: '/recharge',
    component: recharge,
    name: 'recharge',
    meta: {
        requiresAuth: true
    }
}, {
    path: '/iosRecharge',
    component: iosRecharge,
    name: 'iosRecharge',
    meta: {
        requiresAuth: true
    }
}, {
    path: '/login',
    name: 'login',
    component: login
},
{
    path: '/login/agreement',
    name: 'agreement',
    component: agreement
}, {
    path: '/login/pravicy',
    name: 'pravicy',
    component: pravicy
}, {
    path: '/register',
    name: 'register',
    component: register
}, {
    path: '/registerUserinfo',
    name: 'registerUserinfo',
    component: registerUserinfo
}, {
    path: '/userInfo',
    name: 'userInfo',
    component: userInfo
}, {
    path: '/resetPassword',
    name: 'resetPassword',
    component: resetPasswordValidCode
}, {
    path: '/resetPasswordConfirm',
    name: 'resetPasswordConfirm',
    component: resetPasswordConfirm
}, {
    path: '/guess/home',
    name: 'guessHome',
    component: guessHome,
    meta: {
        keepAlive: true,
        title: '动动电竞'
    }
}, {
    path: '/guess/rules',
    name: 'guessRules',
    component: guessRules
}, {
    path: '/guess/allGuess',
    name: 'allGuess',
    component: allGuess,
    meta: {
        keepAlive: true,
        isBack: false, // 用于判断上一个页面是哪个
    }

}, {
    path: '/guess/record',
    name: 'guessRecord',
    component: guessRecord
}, {
    path: '/guess/sign',
    name: 'guessSign',
    component: guessSign
}, {
    path: '/guess/recharge',
    name: 'guessRecharge',
    component: guessRecharge
}, {
    path: '/guess/ddBjnews',
    name: 'ddBjnews',
    component: ddBjnews
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
    path: '/hd/hd104/pullNews',
    name: 'pullNews',
    component: pullNews
}, {
    path: '/hd/hd105/rank',
    name: 'guessRank',
    component: guessRank
}, {
    path: '/helpcenter',
    name: 'helpcenter',
    component: helpcenter
}, {
    path: '/helpcenter/questionList',
    name: 'questionList',
    component: questionList
}, {
    path: '/helpcenter/feedBackList',
    name: 'feedBackList',
    component: feedBackList
}, {
    path: '/helpcenter/feedBackSubmit',
    name: 'feedBackSubmit',
    component: feedBackSubmit
}, {
    path: '/helpcenter/feedBackSuccess',
    name: 'feedBackSuccess',
    component: feedBackSuccess
}, {
    path: '/helpcenter/myFeedBack',
    name: 'myFeedBack',
    component: myFeedBack
}, {
    path: '/helpcenter/feedBackDetail',
    name: 'feedBackDetail',
    component: feedBackDetail
}, {
    path: '/uc/bindPhone',
    name: 'bindPhone',
    component: bindPhone
},{
    path: '*',
    component: guessHome
}
];

export default routers;
