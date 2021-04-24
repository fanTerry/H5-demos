/*
 * @Author: your name
 * @Date: 2020-06-12 09:32:11
 * @LastEditTime: 2020-06-12 09:35:09
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch-followplan/src/libs/storages/scheduleLocalStorage.js
 * 
 * js定时缓存
 *  
 */ 

let scheduleStorage = {
    set: function (variable, value, ttl_ms) {
        var data = {
            value: value,
            expires_at: new Date(ttl_ms).getTime()
        };
        localStorage.setItem(variable.toString(), JSON.stringify(data));
    },
    get: function (variable) {
        var data = JSON.parse(localStorage.getItem(variable.toString()));
        if (data !== null) {
            debugger
            if (data.expires_at !== null && data.expires_at < new Date().getTime()) {
                localStorage.removeItem(variable.toString());
            } else {
                return data.value;
            }
        }
        return null;
    },
    remove(key) {
        localStorage.removeItem(key);
    }
}
export default scheduleStorage;