//路由懒加载
const home = () => import('./views/home/home.vue');
const iframe = () => import('./views/iframe/iframe.vue');


const routers = [{
    path: '/',
    component: home
}, {
    path: '/home',
    name: 'home',
    component: home,
    meta: { keepAlive: true }
},
{
    path: '/iframe',
    name: 'iframe',
    component: iframe
}, {
    path: '*',
    component: home
}
];

export default routers;
