/*
 * @Author: your name
 * @Date: 2020-06-17 18:22:53
 * @LastEditTime: 2020-06-17 18:30:18
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch-followplan/src/libs/mixin/math.js
 */


export const  math = {
    data() {
        return {
            isShow:'sdsfdfdfdf'
        };
    },
    created () {
      console.log("999999999-混入");  
    },
    methods: {
        accAdd(arg1,arg2) {
            var r1, r2, m, c;
            try {
                r1 = arg1.toString().split(".")[1].length;
            } catch (e) {
                r1 = 0;
            }
            try {
                r2 = arg2.toString().split(".")[1].length;
            } catch (e) {
                r2 = 0;
            }
            c = Math.abs(r1 - r2);
            m = Math.pow(10, Math.max(r1, r2));
            if (c > 0) {
                var cm = Math.pow(10, c);
                if (r1 > r2) {
                    arg1 = Number(arg1.toString().replace(".", ""));
                    arg2 = Number(arg2.toString().replace(".", "")) * cm;
                } else {
                    arg1 = Number(arg1.toString().replace(".", "")) * cm;
                    arg2 = Number(arg2.toString().replace(".", ""));
                }
            } else {
                arg1 = Number(arg1.toString().replace(".", ""));
                arg2 = Number(arg2.toString().replace(".", ""));
            }
            return (arg1 + arg2) / m;
        }
    }
}
