//发现模块
const community = () => import('../views/community/index.vue');
const publishEssays = () => import('../views/community/publishArticle/publishEssays.vue');
const detailTopic = () => import('../views/community/topic/gameTopic.vue');
const topic = () => import('../views/community/topicList.vue');
const recommend = () => import('../views/community/recommend.vue');
const searchTopic = () => import('../views/community/searchTopic.vue');

export default [{
    path: '/topic',
    name: 'topic',
    component: topic,
    meta: {
        requiresAuth: true,
        parent: '/community'
    }
}, {
    path: '/recommend',
    name: 'recommend',
    component: recommend,
    meta: {
        keepAlive: true
    }
}, {
    path: '/community',
    name: 'community',
    component: community,
    meta: {
        keepAlive: true
    }
}, {
    path: '/article/publishEssays',
    name: 'publishEssays',
    component: publishEssays,
    meta: {
        requiresAuth: true
    }
}, {
    path: '/detailTopic/:id',
    name: 'detailTopic',
    component: detailTopic,
    meta: {
        requiresAuth: true,
        keepAlive: true,
        isBack: true,
        meta: {
            parent: '/community'
        }
    }
}, {
    path: '/community/searchTopic',
    name: 'searchTopic',
    component: searchTopic,
    meta: {
        requiresAuth: true
    }
}]
