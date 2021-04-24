exports.tofix = () => {
    let u = navigator.userAgent;
    let isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

    //如果是ios，执行下面的代码
    if (isiOS) {
        console.log(isiOS, "isiOS");
        //因为要禁止整个页面的滑动，所以定义一个box，里面装有chatBody和foot两个子元素
        const box = document.getElementById("mainId");
        const chatBody = document.getElementById("scrollId");
        if (!box) {
            return;
        }
        if (!chatBody) {
            return;
        }
        var startY, endY; //定义滑动的起点和终点
        //监听touchstart事件，记录滑动起点的位置
        box.addEventListener("touchstart", function (e) {
            startY = e.touches[0].pageY;
        });
        //开始滑动，此处使用box元素的事件监听，来禁止整个页面的滑动
        box.addEventListener("touchmove", function (e) {
            endY = e.touches[0].pageY; //记录此时的滑动y轴坐标

            //页面向上滑动
            //页面滚动上去的长度scrollTop
            if (endY > startY && chatBody.scrollTop <= 0) {
                console.log("页面向上滑动");
                e.preventDefault();
            }

            //页面向下滑动
            //页面的总长度（包括滚动上去的部分）scrollHeight
            if (
                endY < startY &&
                chatBody.scrollTop + chatBody.clientHeight >= chatBody.scrollHeight
            ) {
                console.log("页面向下滑动");
                e.preventDefault();
            }
        });
    }
}
