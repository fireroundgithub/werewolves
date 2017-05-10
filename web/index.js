/**
 * Created by cheng.wen on 2017/5/5.
 * 整个页面的文档
 */
// 点击开始按钮，即进入开始游戏
$("#js-start").on("click", function (e) {
    $("#js-start").hide(); // 隐藏按钮
    $("#js-prepareGame").show(); // 跳转到等待页面
    startGame();
});
/**
 * 右上角的倒计时函数
 * @param nowTime 当前时间
 * 每隔一秒钟刷新一次倒计时显示器*/
function timeOut(nowTime) {
    setInterval(function () {
        var nowTime1 = new Date();
        var time = (nowTime1.getMinutes()*60+nowTime1.getSeconds()) - (nowTime.getMinutes()*60+nowTime.getSeconds());
        $("#js-minutes").html(parseInt(time/60)); // 倒计时显示分钟
        $("#js-seconds").html(time%60); // 倒计时显示秒
    }, 1000);
}

/**
 * 建立webSocket连接*/
function startGame() {
    var webSockets = ""; // ws对象
    if('WebSocket' in window) {
        try {
            webSockets = new WebSocket("ws://localhost:8080/werewolves/ws-server/ready/0");
        } catch (e) {
            alert("ws启动出错，请重试" + e);
        }
    }
    webSockets.onopen = function () {
        // open的时候发送数据
        console.log("发送数据中"+JSON.stringify({'action':'ready'}));
        // webSockets.send({'action':'ready'});// 发送字符串数据
    };
    webSockets.onerror = function () {console.log("发送错误");};
    webSockets.onmessage = function (msg) {
        var state = JSON.parse(msg.data).state; // 游戏准备状态
        if(state === "start"){
            // 游戏准备开始
            $("#js-prepareGame").hide(); // 跳转到等待页面
            $("#js-containAll").show();// 显示container
            timeOut(new Date()); // 开始计时
        }
        console.log("收到数据:"+state);
    };
    webSockets.onclose = function () {console.log("关闭连接");};
}