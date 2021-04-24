    const state = {
        loginData: {
            loginState: false, // 判断用户是否登录的标志，true已登录/false未登录
            loginType: 1,  // 控制用户登录和未登录的信息显示，2已登录/1未登录
            loginShowType: false, // 控制登录弹窗，true显示/false不显示
            codeType: 0 // 0不显示/1显示微信登录红包/2显示二维码
        }

    };
    const getters = {
        getLoginData: state => state.loginData
    };
    const mutations = {
        setLoginData(state, object) {
            state.loginData = object;
        }
    };
    const actions = {
        setLoginData({
            commit
        }, object) {
            commit('setLoginData', object);
        }
    };

    export default {
        state,
        getters,
        actions,
        mutations
    };
