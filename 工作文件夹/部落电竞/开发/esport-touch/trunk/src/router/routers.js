//主路由入口
import home from './home';
import community from './community';
import usercenter from './usercenter';
import guess from './guess';
import hd from './hd';
import store from './store';
import expert from './expert';
import other from './other';

const routers = [
    ...home, //资讯首页
    ...community, //发现模块
    ...usercenter, //用户中心
    ...guess, //预测
    ...hd, //活动
    ...store, //商城
    ...expert, //专家卖料
    ...other //其他
];

export default routers;
