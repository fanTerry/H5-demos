//商城模块
const store = () => import('../views/store/index.vue');
const goodsList = () => import('../views/store/list/goodsList.vue');
const goodsDetail = () => import('../views/store/detail/goodsDetail.vue');
const orderDetails = () => import('../views/store/order/orderDetails.vue');
const addressManage = () => import('../views/store/address/manage.vue');
const addressEdit = () => import('../views/store/address/edit.vue');
const myExchange = () => import('../views/store/my_exchange/my_exchange.vue');
const orderList = () => import('../views/store/my_exchange/orderList.vue');

export default [{
        path: '/store',
        name: 'store',
        component: store,
        meta: {
            keepAlive: true
        }
    },
    {
        path: '/goodsList',
        name: 'goodsList',
        component: goodsList
    }, {
        path: '/goodsDetail/:goodsId',
        name: 'goodsDetail',
        component: goodsDetail,
        meta: {
            requiresAuth: true, // 是否授权
            keepAlive: true, // 该字段表示该页面需要缓存
            isBack: false, // 用于判断上一个页面是哪个
            title: '商品详情'
        }
    }, {
        path: '/orderDetails/:orderId',
        name: 'orderDetails',
        component: orderDetails,
        meta: {
            requiresAuth: true
        }
    }, {
        path: '/addressManage',
        name: 'addressManage',
        component: addressManage
    }, {
        path: '/addressEdit',
        name: 'addressEdit',
        component: addressEdit,
        meta: {
            requiresAuth: true
        }
    }, {
        path: '/myExchange',
        name: 'myExchange',
        component: myExchange,
        meta: {
            requiresAuth: true
        }
    }, {
        path: '/orderList',
        name: 'orderList',
        component: orderList,
        meta: {
            requiresAuth: true
        }
    }
]
