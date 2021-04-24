//用户中心模块
const userCenter = () => import('../views/usercenter/index.vue');
const wallet = () => import('../views/usercenter/wallet/myWallet.vue');
const recharge = () => import('../views/usercenter/recharge/myRecharge.vue');
const iosRecharge = () => import('../views/usercenter/recharge/iosRecharge.vue');
const login = () => import('../views/usercenter/login/myLogin.vue');
const agreement = () => import('../views/usercenter/login/agreement.vue');
const pravicy = () => import('../views/usercenter/login/safe.vue');
const register = () => import('../views/usercenter/register/myRegister.vue');
const registerUserinfo = () => import('../views/usercenter/register/userInfo/myRegisterUserinfo.vue');
const userInfo = () => import('../views/usercenter/userinfo/userInfo.vue');
const bindPhone = () => import('../views/usercenter/userinfo/bindPhone.vue');
const userAuth = () => import('../views/usercenter/userinfo/userAuth.vue');
const userFuiouAuth = () => import('../views/usercenter/userinfo/userFuiouAuth.vue');
const userFuiouPay = () => import('../views/usercenter/userinfo/userFuiouPay.vue');
const userBankCard = () => import('../views/usercenter/userinfo/userBankCard.vue');
const salesManage = () => import('../views/usercenter/salesManage/index.vue');
const bankCard = () => import('../views/usercenter/salesManage/bankcard/index.vue');
const addCard = () => import('../views/usercenter/salesManage/bankcard/addCard.vue');
const addCardConfirm = () => import('../views/usercenter/salesManage/bankcard/addCardConfirm.vue');
const preventAddiction = () => import('../views/usercenter/preventaddiction/index.vue');

const userArticle = () => import('../views/usercenter/article/myArticle.vue');
const myRelease = () => import('../views/usercenter/article/myRelease.vue');
const resetPasswordValidCode = () => import('../views/usercenter/resetPassword/validCode.vue');
const resetPasswordConfirm = () => import('../views/usercenter/resetPassword/confirm.vue');
const writeInfo = () => import('../views/usercenter/userinfo/writeInfo.vue');

const myMessage = () => import('../views/usercenter/article/myMessage.vue');
const myFollow = () => import('../views/usercenter/follow/myFollow.vue');
const myFans = () => import('../views/usercenter/follow/myFans.vue');
const userPublishArticle = () => import('../views/usercenter/article/myPublishArticle.vue');
const myLikeArticle = () => import('../views/usercenter/article/myUpCmsArtcle.vue');

// 帮助中心
const helpcenter = () => import('../views/helpcenter/help.vue');
const questionList = () => import('../views/helpcenter/questionList.vue');
const feedBackList = () => import('../views/helpcenter/feedBackList.vue');
const feedBackSubmit = () => import('../views/helpcenter/feedBackSubmit.vue');
const feedBackSuccess = () => import('../views/helpcenter/feedBackSuccess.vue');
const myFeedBack = () => import('../views/helpcenter/myFeedBack.vue');
const feedBackDetail = () => import('../views/helpcenter/feedBackDetail.vue');

export default [{
        path: '/userCenter',
        name: 'userCenter',
        component: userCenter,

    }, {
        path: '/wallet',
        name: 'wallet',
        component: wallet,
        meta: {
            requiresAuth: true,
            parent: '/userCenter'
        }
    }, {
        path: '/recharge',
        component: recharge,
        name: 'recharge',
        meta: {
            requiresAuth: true,
            parent: '/userCenter'
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
    }, {
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
        path: '/uc/bindPhone',
        name: 'bindPhone',
        component: bindPhone,
    }, {
        path: '/uc/userAuth',
        name: 'userAuth',
        component: userAuth
    }, {
        path: '/uc/userFuiouAuth',
        name: 'userFuiouAuth',
        component: userFuiouAuth
    }, {
        path: '/uc/userFuiouPay',
        name: 'userFuiouPay',
        component: userFuiouPay,
        meta: {
            title: '支付中'
        }
    }, {
        path: '/uc/userBankCard',
        name: 'userBankCard',
        component: userBankCard
    }, {
        path: '/userCenter/salesManage',
        name: 'salesManage',
        component: salesManage,
        meta: {
            parent: '/userCenter'
        }

    }, {
        path: '/userCenter/salesManage/bankCard',
        name: 'bankCard',
        component: bankCard,
    }, {
        path: '/userCenter/salesManage/addCard',
        name: 'addCard',
        component: addCard,
    }, {
        path: '/userCenter/salesManage/addCardConfirm',
        name: 'addCardConfirm',
        component: addCardConfirm,
    }, {
        path: '/userCenter/preventAddiction',
        name: 'preventAddiction',
        component: preventAddiction,
        meta: {
            parent: '/userCenter'
        }
    }, {
        path: '/userArticle',
        name: 'userArticle',
        component: userArticle,
        meta: {
            requiresAuth: true
        }
    }, {
        path: '/userCenter/myRelease', // 我的记录
        name: 'userCenter/myRelease',
        component: myRelease,
        meta: {
            requiresAuth: true,
            parent: '/userCenter'
        }
    }, {
        path: '/resetPassword',
        name: 'resetPassword',
        component: resetPasswordValidCode
    }, {
        path: '/resetPasswordConfirm',
        name: 'resetPasswordConfirm',
        component: resetPasswordConfirm
    }, {
        path: '/writeInfo',
        name: 'writeInfo',
        component: writeInfo
    }, {
        path: '/myFollow',
        name: 'myFollow',
        component: myFollow
        // meta: { requiresAuth: true }
    }, {
        path: '/userCenter/myFans', //我的关注
        name: 'userCenter/myFans',
        component: myFans,
        meta: {
            parent: '/userCenter'
        }
    }, {
        path: '/userCenter/myMessage', //我的消息
        name: 'userCenter/myMessage',
        component: myMessage,
        meta: {
            requiresAuth: true
        }
    }, {
        path: '/userCenter/userPublishArticle', // 我的发布
        name: 'userCenter/userPublishArticle',
        component: userPublishArticle,
        meta: {
            requiresAuth: true,
            keepAlive: true,
            isBack: true,
        }
    }, {
        path: '/userCenter/myLikeArticle', // 我的点赞
        name: 'userCenter/myLikeArticle',
        component: myLikeArticle,
        meta: {
            requiresAuth: true
        }
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
    },
    {
        path: '/helpcenter/feedBackDetail',
        name: 'feedBackDetail',
        component: feedBackDetail
    }
]
