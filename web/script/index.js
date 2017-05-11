/**
 * Created by cheng.wen on 2017/5/5.
 * 整个页面的文档
 */
// 4,4,4的局，身份：身份标示图
var identifyArray = {
    "0": "预言家",
    "1": "女巫",
    "2": "猎人",
    "3": "守卫",
    "4": "平民",
    "5": "狼人",
};
var myIdentify = ""; // 当前用户的身份信息
// 点击开始按钮，即进入开始游戏准备
$("#js-start").on("click", function (e) {
    $("#js-start").hide(); // 隐藏按钮
    $("#js-prepareGame").show(); // 跳转到等待页面

    $("#js-prepareGame").hide(); // 跳转到等待页面
    $("#js-containAll").show();// 显示container
    timeOut(new Date()); // 开始计时

    startGame("ws://localhost:8080/werewolves/ws-server/ready/0");
});

/**
 * 建立webSocket连接
 * 使用jquer的on事件委托，链式写法
 * 按钮点击的链接。点击按钮之后跳转到准备页面。当达到人数时，推送给客户端，进入游戏界面,显示当前用户身份
 * @param url ws链接ready
 */
function startGame(url) {
    var webSockets = new WebSocket(url);
    $(webSockets).on("open", function () {
        console.log("发送数据中");
    })
    .on("message", function (msg) {
        var data = JSON.parse(msg.data);
        var state = data.state; // 游戏人数是否凑齐===start
        var identify = data.identify; // 每位玩家的身份
        console.log("您的身份是：" + identify);
        if(state === "start"){
            var imgUrl = "../img/card/identify"+identify+".jpg"; // 显示身份信息的图片
            $("#js-userIdentify").attr("style", "background: url('"+imgUrl+"');background-size: cover;");
            $("#js-userIdentify").html(identifyArray[identify]);// 图片上显示身份
            myIdentify = identify; // 用户身份

            showSeats(data.seatArr, data.mySeat); // 显示座位信息

            $("#js-prepareGame").hide(); // 跳转到等待页面
            $("#js-containAll").show();// 显示container
            timeOut(new Date()); // 开始计时

            // 游戏倒计时，由myIdentify保存当前用户身份.预言家验人
            $("#js-message").show();
            $("#js-message>div").html("开始,天黑请闭眼");
            $("#js-message>div").html("预言家请睁眼，请选择你要验谁的身份，点击该用户头像");
        }
    })
    .on("close", function (e) {
        console.log("关闭连接" + e);
    })
    .on("error", function (e) {
        console.log("发送错误" + e);
    });
}

/**
 * 用户座位列表展示,在当前用户的头像加上红色边框
 */
function showSeats(seatArr, mySeat) {
    for(var i = 0; i<seatArr.length; i++){
        var centerIndex = seatArr.length/2;
        var left = $("js-leftLogo");
        var right = $("js-rightLogo");
        var img = "<img src='../img/seats/"+i+".png' alt='用户'" + i + ">";
        if(i === mySeat){
            img = "<img src='../img/seats/"+i+".png' class='mySeatImg' alt='我'>";
        }
        if(i < centerIndex){
            left.append(img);
        } else {
            right.append(img);
        }
    }
}
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