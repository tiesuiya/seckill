/**
 * 详情页交互js逻辑，采用模块化方式定义
 */
var seckillDetail = {
    /**
     * 统一定义URL，方便维护
     */
    URL: {
        now: function () {
            return '/time/now';
        },
        exposer: function (seckillId) {
            return '/seckills/' + seckillId + '/exposer';
        },
        execution: function (seckillId) {
            return '/seckills/' + seckillId + '/execution';
        }
    },
    /**
     * 验证手机号函数
     * @param phone 待验证号码
     * @returns {boolean}
     */
    validatePhone: function (phone) {
        // isNaN如果是非数字为true
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    /**
     * 时间处理
     * @param seckillId 库存id
     * @param nowTime 当前时间
     * @param startTime 秒杀开始时间
     * @param endTime 秒杀结束时间
     */
    timeJudgment: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            // 秒杀已经结束
            seckillBox.html('秒杀已经结束！');
        } else if (nowTime < startTime) {
            // 秒杀未开始，进入倒计时
            var killTime = new Date(startTime);
            // 使用countDown插件
            seckillBox.countdown(killTime, function (event) {
                // 时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on("finish.countdown", function () {
                // 完成回调，获取秒杀地址，准备秒杀按钮
                seckillDetail.handleExposer(seckillId, seckillBox);
            });
        } else {
            // 秒杀已经开始，获取秒杀地址，准备秒杀按钮
            seckillDetail.handleExposer(seckillId, seckillBox);
        }
    },
    /**
     * 获取秒杀地址
     * @param seckillId 库存id
     * @param node 展示秒杀按钮的节点
     */
    handleExposer: function (seckillId, node) {
        node.hide();
        node.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.get(seckillDetail.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    var md5 = exposer['md5'];
                    // 执行秒杀
                    seckillDetail.doExecution(seckillId, md5, node);
                } else {
                    // 未开始秒杀，重新计时
                    var now = exposer['now'];
                    var startTime = exposer['startTime'];
                    var endTime = exposer['endTime'];
                    node.show().html('');
                    seckillDetail.timeJudgment(seckillId, now, startTime, endTime);
                }
            } else {
                // 接口调用方问题，给程序员看的
                console.log('result:' + result);
            }
        });
    },
    /**
     * 执行秒杀
     * @param seckillId 库存id
     * @param md5 安全码
     * @param node 展示秒杀按钮的节点
     */
    doExecution: function (seckillId, md5, node) {
        // 开始秒杀
        // 绑定一次点击事件，规避用户重复点击
        $("#killBtn").one('click', function () {
            // 执行秒杀请求
            // 禁用按钮
            $(this).addClass("disabled");

            console.log(1234);
            $.ajax({
                type: "post",
                url: seckillDetail.URL.execution(seckillId),
                dataType: "json",
                data: JSON.stringify({
                    "md5": md5,
                    "userPhone": $.cookie('USER_PHONE')
                }),
                headers: {'Content-Type': 'application/json'},
                success: function (result) {
                    if (result && result['success']) {
                        var killResult = result['data'];
                        console.log(killResult);
                        var state = killResult['state'];
                        var stateInfo = killResult['stateInfo'];
                        node.html('<span class="label label-success">' + stateInfo + '</span>');
                    } else {
                        // 接口调用方问题，给程序员看的
                        console.log('result:' + result);
                    }
                }
            });
        });
        node.show();
    },
    /**
     * 页面逻辑入口
     * @param seckillId 库存id
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    init: function (seckillId, startTime, endTime) {
        var userPhone = $.cookie('USER_PHONE');

        // 判断登录
        if (!seckillDetail.validatePhone(userPhone)) {
            // 如果没有登录，显示弹出层
            var killPhoneModal = $('#killPhoneModal');
            killPhoneModal.modal({
                show: true,// 显示弹出层
                backdrop: 'static', // 禁止位置关闭
                keyboard: false // 关闭键盘事件
            });
            $('#killPhoneBtn').click(function () {
                var inputPhone = $('#killPhoneKey').val();
                if (seckillDetail.validatePhone(inputPhone)) {
                    $.cookie('USER_PHONE', inputPhone, {expires: 7, path: '/seckills'})
                    window.location.reload();
                } else {
                    $('#killPhoneMessage').hide().html('<lable class="label label-danger">手机号错误！</lable>').show(320);
                }
            });
        } else {
            // 成功登录，继续执行流程
            $.get(seckillDetail.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    seckillDetail.timeJudgment(seckillId, result.data, startTime, endTime);
                } else {
                    // 接口调用方问题，给程序员看的
                    console.log('result:' + result);
                }
            });
        }

    }
};