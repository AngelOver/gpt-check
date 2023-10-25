// ==UserScript==
// @name         Poe 添加删除所有记录按钮127-203
// @grant    GM_xmlhttpRequest
// @connect  124.221.62.203
// @namespace    https://github.com/xxnuo/
// @version      0.1
// @license      MIT
// @description  给 124.221.62.203 Poe 网页版添加一个选中所有历史记录的功能，需要先选择 Delete 一句话，然后点击油猴图标里全选历史记录按钮。
// @author       xxnuo
// @match        https://poe.com/*
// @icon         https://www.google.com/s2/favicons?sz=64&domain=poe.com
// @grant        GM_registerMenuCommand
// ==/UserScript==
var clientId ="Client_"+ Math.floor(Math.random() * 10000)+"_"+document.location.pathname;
var  msgId = "";

var  lastSize = 0;
var  nowSize = 0;
var  nextSize = 0;


window.addEventListener('load', function() {


    //var host = "http://124.221.62.203:9081";
    var host = "http://124.221.62.203:9081";

    // 每200毫秒汇报
    setInterval(function() {
        //汇报客户端是否运行
        //汇报生成的文本
        if(msgId!=""){
            console.log(msgId);

            var allTextP = document.querySelector('div[class^="InfiniteScroll_container"]');
            var allText = "";
            var allTextFlag = false;
            var allTextFlagC = false;
            if(allTextP.lastChild.childElementCount>1){
                //allText = allTextP.lastChild.childNodes[1].innerText;

                //allTextFlag = allTextP.lastChild.childNodes[1].getAttribute("data-complete");
                // if(!allTextP.lastChild.childNodes[1].hasAttribute("data-complete")){
                //     allTextFlag = false;
                // }

                nowSize =  document.querySelector('div[class^="InfiniteScroll_container"]').childNodes.length;

                if(allTextP.lastChild.childNodes[1].hasAttribute("data-complete")){
                    allTextFlagC = allTextP.lastChild.childNodes[1].getAttribute("data-complete") == 'true';
                }
                var allTextPt =   allTextP.lastChild.innerText;

                if(allTextPt.includes("Share")&allTextFlagC){
                    console.log(""+nowSize+","+nextSize);
                    if(nowSize>=nextSize){
                        allTextFlag = true;
                    }

                }
                if(allTextP.lastChild.childNodes[1].childElementCount==2){
                    allText = allTextP.lastChild.childNodes[1].childNodes[1].innerText;

                }

            }



            var url = host+'/v1/chat/online2';
            //   var allTextL =  document.querySelectorAll('div[class^="ChatMessage_botMessageHeader"]');
            //   var allText = allTextL[allTextL.length-1].parentElement.lastChild.innerText;
            var freeFlag = !document.querySelector('button[aria-label="stop message"]');
            GM_xmlhttpRequest({
                method: 'POST',
                url: url,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: JSON.stringify({
                    freeFlag: freeFlag,
                    clientId: clientId,
                    text: allText,
                    msgId:msgId,
                    allTextFlag:allTextFlag
                }),
                onload: function(response) {
                    // console.log(response);
                    if(allTextFlag&&(msgId!='')){
                        console.log("ok");
                        //上报完成，清理msgId
                        msgId = '';
                    }

                },
                onerror: function(error) {
                    console.error('Error:', error);
                },
            });

        }
    }, 300);
    // 获取任务
    setInterval(function() {
        var url = host+'/v1/chat/send';
        var freeFlag = !document.querySelector('button[aria-label="stop message"]');
        GM_xmlhttpRequest({
            method: 'POST',
            url: url,
            headers: {
                'Content-Type': 'application/json',
            },
            data: JSON.stringify({
                freeFlag: freeFlag,
                clientId: clientId
            }),
            onload: function(response) {
                //console.log(response);
                var res =  JSON.parse(response.responseText);
                var textarea =   document.querySelector('textarea');
                var btn = document.querySelector('footer button').nextElementSibling.children[3];
                var clientMsg = "";
                if(res.code == 200){
                    textarea.parentNode.parentNode.parentNode.firstChild.click();
                    lastSize =  document.querySelector('div[class^="InfiniteScroll_container"]').childNodes.length;
                    nextSize = lastSize+2;
                    setTimeout(function() {

                        clientMsg = res.data.clientMsg;
                        textarea.parentElement.setAttribute("data-replicated-value",clientMsg);
                        textarea.value=clientMsg;
                        btn.removeAttribute('disabled');

                        btn.click();
                        msgId=res.data.key;
                        //清理本次消息
                        // var allTextL =  document.querySelectorAll('div[class^="ChatMessage_botMessageHeader"]');
                        // allTextL[allTextL.length-1].parentElement.lastChild.setHTML('');

                    }, 800);


                }
            },
            onerror: function(error) {
                console.error('Error:', error);
            },
        });
    }, 1000);  // 指定时间间隔为 1000 毫秒，即 1 秒

}, false);


