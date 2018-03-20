<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/pages/common/tag.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>详情页</title>
    <%@include file="/WEB-INF/pages/common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading text-center">
            <h2>${seckill.productName}</h2>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <span class="glyphicon glyphicon-time"></span>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>

<!-- 登录弹出层模态框（Modal） -->
<div class="modal fade" id="killPhoneModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                </h3>
            </div>
            <div class="modal-body text-center">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" id="killPhoneKey" placeholder="填写手机号^o^" class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <span id="killPhoneMessage" class=""></span>
                <button type="button" id="killPhoneBtn" class="btn btn-primary">
                    <span class="glyphicon glyphicon-phone"></span>提交
                </button>
            </div>
        </div>
    </div>
</div>
</body>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<%-- jquery cookie插件 --%>
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>

<%-- jquery countDown倒计时插件 --%>
<script src="https://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>

<%-- 当前页脚本 --%>
<script src="/resources/script/seckillDetail.js" type="text/javascript"></script>

<%-- 执行当前页脚本 --%>
<script type="text/javascript">
    $(function () {
        seckillDetail.init(${seckill.id}, ${seckill.startTime.time}, ${seckill.endTime.time});
    });
</script>
</html>
