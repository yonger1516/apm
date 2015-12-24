


/****
 * ##################################
 * ##   自定义
 * ##############################
 */

/**
 * notifications 通知
 * @param text  内容
 * @param type  类型：  success | error | alert 。。。
 * @param layout  弹出位置：bottomRight |  top | topLeft  | topRight | bottomLeft 。。。
 */
function myNotifications (text , type , layout) {
    if(!layout){
        layout = 'bottomRight';  // 默认从右下角 弹出
    }
    noty({
        "text": text,
        "layout": layout,
        "type": type,  // error ---  alert
        "animateOpen": {"opacity": "show"}
    });
}

/**
 * 模态框弹出提示信息
 */
function myModel(content, title, duration) {
    var idx = new Date().getTime() + '';
    if (!title) {
        title = '操作提示';
    }

    $('<div id="myModal' + idx + '" class="modal hide fade" data-backdrop="false">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
        '<h3 id="myModalLabel">' + title + '</h3>' +
        '</div>' +
        '<div class="modal-body">' + content + '</div>' +
        '<div class="modal-footer">' +
        '<button class="btn btn-primary myconfirm">确认</button>' +
        '</div>' +
        '</div>').appendTo($('body', document));

    $('#myModal' + idx).on('show', function () {
        $('body', document).addClass('modal-open');
        $('<div class="modal-backdrop fade in"></div>').appendTo($('body', document));
    });
    $('#myModal' + idx).on('hide', function (e) {
        $('body', document).removeClass('modal-open');
        $('div.modal-backdrop').remove();
        $('#myModal' + idx).remove();
    });

    $('#myModal' + idx).modal('show');

    if (duration != null) {
        $('#myModal' + idx).fadeIn(500).delay(duration).fadeOut(500, function () {
            $('#myModal' + idx).modal('hide');
            $('#myModal' + idx).remove();
        });
    }

    $('.myconfirm').on('click', function () {
        /** if button is locked, return.or not to add lock flag */
        var thisBtn = $(this);
        if (thisBtn.hasClass('disabled')) {
            return;
        } else {
            thisBtn.addClass('disabled');
        }


        /** remove lock flag */
        thisBtn.removeClass('disabled');

        /** close layout */
        $('#myModal' + idx).modal('hide');
        $('#myModal' + idx).remove();
    });
}

/**
 * 确认提示层
 *
 * @param {string} message 提示消息
 * @param {function} fn　点确认按钮执行的函数
 * @param {json} option　fn接收的参数对象
 */
function myConfirm(message, fn, option) {
    var idx = new Date().getTime() + '';
    console.log('in myconfirm');
    $('<div id="myModal' + idx + '" class="modal hide /*fade*/" data-backdrop="false">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
        '<h3 id="myModalLabel">操作提示</h3>' +
        '</div>' +
        '<div class="modal-body">' + message + '</div>' +
        '<div class="modal-footer">' +
        '<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>' +
        '<button class="btn btn-primary myconfirm">确认</button>' +
        '</div>' +
        '</div>').appendTo($('body', document));

    $('#myModal' + idx).on('show.bs.modal', function () {
        $('body', document).addClass('modal-open');
        $('<div class="modal-backdrop fade in"></div>').appendTo($('body', document));
    });
    $('#myModal' + idx).on('hide', function (e) {
        $('body', document).removeClass('modal-open');
        $('div.modal-backdrop').remove();
        $('#myModal' + idx).remove();
    });

    $('#myModal' + idx).modal('show');

    $('.myconfirm').on('click', function () {
        /** if button is locked, return.or not to add lock flag */
        var thisBtn = $(this);
        if (thisBtn.hasClass('disabled')) {
            return;
        } else {
            thisBtn.addClass('disabled');
        }

        /** do task */
        if (fn && $.isFunction(fn)) {
            try {
                fn(option);
            } catch (e) {
            }
            ;
        }

        /** remove lock flag */
        thisBtn.removeClass('disabled');

        /** close layout */
        $('#myModal' + idx).modal('hide');
        $('#myModal' + idx).remove();
    });
}
