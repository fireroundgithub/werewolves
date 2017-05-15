/**
 * Created by cheng.wen on 2017/5/5.
 * 整个页面的文档
 */
// 4,4,4的局，身份：身份标示图
var identifyArray = {
    "0": ["狼人"],
    "1": ["预言家"],
    "2": ["女巫"],
    "3": ["守卫"],
    "4": ["猎人"],
    "5": ["平民"],
};
var myIdentify = ""; // 当前用户的身份信息
var death = false; // 用户存活信息
// 点击开始按钮，即进入开始游戏准备
$("#js-start").on("click", function (e) {
    $("#js-start").hide(); // 隐藏按钮
    $("#js-prepareGame").show(); // 跳转到等待页面


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
        myIdentify = data.identify; // 每位玩家的身份
        console.log("您的身份是：" + myIdentify);
        if(state === "start"){
            $("#js-prepareGame").hide(); // 跳转到等待页面
            $("#js-containAll").show();// 显示container
            timeOut(new Date()); // 开始计时

            showMyIdentify(myIdentify);//显示当前用户信息
            showSeats(data.seatArr, data.mySeat); // 显示座位信息

            // 游戏倒计时，由myIdentify保存当前用户身份
            showMessage();
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
 *显示个人用户信息
 * @param identify 该用户的身份
 */
function showMyIdentify(identify) {
    var imgUrl = "../img/card/identify"+identify+".jpg"; // 显示身份信息的图片
    $("#js-userIdentify").attr("style", "background: url('"+imgUrl+"');background-size: cover;");
    $("#js-userIdentify").html(identifyArray[identify]);// 图片上显示身份
}
/**
 * 用户座位列表展示,在当前用户的头像加上红色边框
 */
function showSeats(seatArr, mySeat) {
    for(var i = 0; i<seatArr.length; i++){
        var centerIndex = seatArr.length/2;
        var left = $('#js-leftLogo');
        var right = $('#js-rightLogo');
        var img = '<img src="../img/seats/'+i+'.jpg" alt="用户' + i + '">';
        if(i === mySeat){
            img = '<img src="../img/seats/'+i+'.jpg" class="mySeatImg" alt="我">';
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

/**
 * 展示提示信息
 */
function showMessage() {
    var $dom = $("#js-message"); // 提示信息展示处
    var textArr = ["开始,天黑请闭眼", "预言家请睁眼，请选择你要验谁的身份，点击该用户头像"];
    $dom.show();
    // 每个操作相隔5s
    for(var i=0; i<textArr.length; i++){
        setTimeout((function (i) {
            return function () {
                $dom.html(textArr[i]);
            }
        })(i), 5000*i);
    }
}