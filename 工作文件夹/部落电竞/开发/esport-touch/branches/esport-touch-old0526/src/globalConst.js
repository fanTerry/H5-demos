//dota2英雄前缀
const heroDota2ImgUrlPrefix = 'https://rs.esportzoo.com/upload/league/hero/dota2/';
//LOL英雄前缀
const heroLoLImgUrlPrefix = 'https://rs.esportzoo.com/upload/league/champion/';
//直播赛事数据存放的json文本
const matchDataLiveJsonUrl = '/rsDomain/upload/interface/matchdata/match_live_';
//直播图文数据存放的json文本
const matchEventLiveJsonUrl = '/rsDomain/upload/interface/matchdata/event_live_';
//默认的赛事队伍logo
const matchTeamDefaultIcon = 'https://rs.esportzoo.com/svn/esport-res/mini/images/default/default_team.png';
// 默认的动动竞猜赛事队伍logo
const dDmatchTeamDefaultIcon = 'https://rs.esportzoo.com/svn/esport-res/mini/images/default/default_team.png';
//默认用户头像
const userDefaultIcon = 'https://rs.esportzoo.com/svn/esport-res/mini/images/icon/avatar.png';
//友宝充值地址
const uboxCharge = 'https://v.ubox.cn/weixinmp/recharge?source=jzdj';
//支付方式
const payIndexMap = new Map([
    ['REC_PAY', 0],
    ['WXXCX_PAY', 1],
    ['UBOX_PAY', 2],
    ['WXH5_PAY', 3],
    ['WXJSAPI_PAY', 4],
    ['WXSCAN_PAY', 5],
    ['APPLE_IAP_PAY', 6],
    ['Ali_H5_PAY', 7],
    ['YEEPAY_EBANK_PAY', 8],
    ['UMS_H5_PAY', 9]
]);
//话题默认icon
const topicDefaultIcon = 'https://rs.esportzoo.com/svn/esport-res/mini/images/follow/topic_default_icon.png';
//话题默认背景图
const topicDefaultImgUrl = 'https://rs.esportzoo.com/svn/esport-res/mini/images/follow/topic_default.png';

const entryUrl = location.href.split('#')[0];
export default {
    heroDota2ImgUrlPrefix,
    heroLoLImgUrlPrefix,
    matchDataLiveJsonUrl,
    matchEventLiveJsonUrl,
    matchTeamDefaultIcon,
    dDmatchTeamDefaultIcon,
    userDefaultIcon,
    uboxCharge,
    payIndexMap,
    topicDefaultIcon,
    topicDefaultImgUrl,
    entryUrl,
}