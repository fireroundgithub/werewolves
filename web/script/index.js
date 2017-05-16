/**
 * Created by cheng.wen on 2017/5/5.
 * 整个页面的文档
 */
// 4,4,4的局，身份：身份标示图
var identifyArray = {
    "0": ["狼人", wolfOperate],//wolfOperate
    "1": ["预言家", prophetOperate],//prophetOperate
    "2": ["女巫", witchOperate],// witchOperate
    "3": ["守卫"],
    "4": ["猎人"],
    "5": ["平民"],
};
var myIdentify = ""; // 当前用户的身份信息
var death = false; // 用户存活信息
// 点击开始按钮，即进入开始游戏准备
$("#js-start").on("click", function (e) {
    $("#js-start").hide(); // 隐藏按钮
    // $("#js-prepareGame").show(); // 跳转到等待页面


    myIdentify = 2; // 每位玩家的身份

    $("#js-prepareGame").hide(); // 跳转到等待页面
    $("#js-containAll").show();// 显示container
    timeOut(new Date()); // 开始计时

    showMyIdentify(myIdentify);//显示当前用户信息
    showSeats([1,6,0,12,11,7,4,3,2,5,10,8], 1); // 显示座位信息
    // 游戏倒计时，由myIdentify保存当前用户身份
    showMessage(myIdentify);


    // startGame("ws://localhost:8080/werewolves/ws-server/ready/0");
});

/**
 * 建立webSocket连接
 * 使用jquer的on事件委托，链式写法
 * 按钮点击的链接。点击按钮之后跳转到准备页面。当达到人数时，推送给客户端，进入游戏界面,显示当前用户身份
 * @param url ws链接ready
 */
function startGame(url) {
    var webSockets = new WebSocket(url);
    $(webSockets).on("message", function (msg) {
        var data = JSON.parse(msg.data);
        var state = data.state; // 游戏人数是否凑齐===start
        if(state === "start"){
            myIdentify = data.identify; // 每位玩家的身份

            $("#js-prepareGame").hide(); // 跳转到等待页面
            $("#js-containAll").show();// 显示container
            timeOut(new Date()); // 开始计时

            showMyIdentify(myIdentify);//显示当前用户信息
            showSeats(data.seatArr, data.mySeat); // 显示座位信息
            // 狼人，需要显示自己的狼队友
            if(data.wolf){ wolf(data.wolf); }
            // 游戏倒计时，由myIdentify保存当前用户身份
            showMessage(myIdentify);
        }
    })
}
/**
 *显示个人用户信息
 * @param identify 该用户的身份
 */
function showMyIdentify(identify) {
    var imgUrl = "../img/card/identify"+identify+".jpg"; // 显示身份信息的图片
    $("#js-userIdentify").attr("style", "background: url('"+imgUrl+"');background-size: cover;");
    $("#js-userIdentify").html(identifyArray[identify][0]);// 图片上显示身份
}
/**
 * 用户座位列表展示,在当前用户的头像加上红色边框
 */
function showSeats(seatArr, mySeat) {
    for(var i = 0; i<seatArr.length; i++){
        var centerIndex = seatArr.length/2;
        var left = $('#js-leftLogo');
        var right = $('#js-rightLogo');
        var img = '<div class="userLogo">'+(i+1)+'</div>';
        if(i === mySeat){
            img = '<div class="userLogo mySeatImg">'+(i+1)+'</div>';
        }
        if(i < centerIndex){
            left.append(img);
        } else {
            right.append(img);
        }
        $(".userLogo:last-child").attr("style", "background-image:url('../img/seats/"+i+".jpg');background-size: cover;");
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
function showMessage(identify) {
    var $dom = $("#js-message"); // 提示信息展示处
    var textArr = ["开始,天黑请闭眼", "狼人请睁眼", "预言家请睁眼", "女巫请睁眼", "守卫请睁眼", "天亮了，请睁眼"];
    var ide = identify+1;
    $dom.show();
    // 夜间操作
    for(var i=0; i<textArr.length; i++){
        setTimeout((function (i) {
            return function () {
                if(i === ide+1 && identify <= 2){
                    //天黑狼人，预言家，女巫操作
                    $dom.hide();
                    identifyArray[myIdentify][1]();
                } else {
                    $dom.html(textArr[i]);
                }
            }
        })(i), 5000*i);
    }
    //白天操作

}
/**
 * 显示狼队友
 */
function wolf(wolfIndex) {
    for(var j=0; j<wolfIndex.length; j++){
        $('.userLogo:eq('+j+')').attr("style", "background-image:url('../img/card/identify0.jpg');background-size: cover;");
    }
}
/**
 * 狼队友操作
 */
function wolfOperate(){
    $(".container:eq(0)").one("click", function (e) {
        var index = $(".userLogo").index($(e.target));
        if(e.target.className.search("death") === -1 && confirm("确定要杀"+(index+1)+"号吗？")){
            var socket = new WebSocket("ws://127.0.0.1:8080/werewolves/portal/kill/"+index);
            $(socket).on("message", function (msg) {
                alert("你们今晚杀的是"+msg.data+"号");
                $("#js-message").show();
            });
        }
    });
}
/**
 * 预言家操作
 */
function prophetOperate(){
    $(".container:eq(0)").one("click", function (e) {
        var index = $(".userLogo").index($(e.target));
        if(e.target.className.search("death") === -1 && confirm("确认要验证"+(index+1)+"号的身份？")){
            var socket = new WebSocket("ws://127.0.0.1:8080/werewolves/portal/prophet/"+index);
            $(socket).on("message", function (msg) {
                alert("你验的是"+index+"号，他的身份是"+msg.data.index);
                $("#js-message").show();
            });
        }
    });
}
/**
 *女巫操作
 */
function witchOperate(){
    var socket = new WebSocket("ws://127.0.0.1:8080/werewolves/portal/witch");// 请求杀人信息
    $(socket).on("message", function (msg) {
        var help = confirm("今天晚上死亡的是"+msg.data+"号，是否要使用解药？")?"1":"0";
        new WebSocket("ws://127.0.0.1:8080/werewolves/portal/help/"+help);
        var poison = confirm("请问是否使用毒药")?"1":"0";
        if(poison){
            $(".container:eq(0)").one("click", function (e) {
                var index = $(".userLogo").index($(e.target));
                if(e.target.className.search("death") === -1 && confirm("确认要毒"+(index+1)+"号吗？")){
                    var socket = new WebSocket("ws://127.0.0.1:8080/werewolves/portal/poison/"+index);
                    $(socket).on("message", function (msg) {
                        alert("您对"+index+"号，使用了毒药");
                        $("#js-message").show();
                    });
                }
            });
        } else {
            return;
        }
    });
}
/**
 * 投票
 */
