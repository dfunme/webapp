<? if(type! == "textarea"){ ?>
  <textarea id="${id!}" name="${name!}" style="${style!}">${value!}</textarea>
<? } else if(type! == "image"){ ?>
<style>
  .imgbtn {
    display: block;
    border: 3px solid #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px rgba(0,0,0,.15);
    cursor: pointer;
    overflow: hidden;
    width: ${width!'150'}px;
    height: ${height!'150'}px;
    background:url("${ctx!}/static/plugins/cropper/img/bg.png" );
  }
</style>
<input type="hidden" id="${id!}" name="${name!}" value=""/>
<div id="${id!}btn" class="imgbtn" title="点此上传图片">
  <img id="${id!}preview" src="">
</div>
<script type="text/plain" id="${id!}hidden"></script>
<? } ?>
<? if(lib!true){ ?>
<script type="text/javascript">
	var ctx = '${ctx!}';
</script>
<link href="${ctx!}/static/plugins/ueditor/themes/default/css/ueditor.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx!}/static/plugins/ueditor/ueditor.config.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx!}/static/plugins/ueditor/ueditor.all.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx!}/static/plugins/ueditor/lang/zh-cn/zh-cn.js" charset="utf-8"></script>
<? } ?>
<script type="text/javascript" charset="utf-8">
	<? if(type! == "textarea"){ ?>
	var ${id!}ue = UE.getEditor('${id!}');
	<? }else if(type! == "image" || type! == "file"){ ?>
	var ${id!}ue = UE.getEditor('${id!}hidden');
	${id!}ue.ready(function() {
        //设置编辑器不可用
        ${id!}ue.setDisabled();
        //隐藏编辑器，因为不会用到这个编辑器实例，所以要隐藏
        ${id!}ue.hide();
        <? if(type! == "image"){ ?>
        //侦听图片上传
        ${id!}ue.addListener('beforeInsertImage', function (t, arg) {
            //将地址赋值给相应的input,只去第一张图片的路径
            $("#${id!}").attr("value", arg[0].src);
            //图片预览
            $("#${id!}preview").attr("src", arg[0].src);
        })
		<? } else if(type! == "file"){ ?>
        //侦听文件上传，取上传文件列表中第一个上传的文件的路径
        ${id!}ue.addListener('afterUpfile', function (t, arg) {
            $("#file").attr("value", _editor.options.filePath + arg[0].url);
        })
        <? } ?>
    });
    <? if(type! == "image"){ ?>
    $('#${id!}btn').click(function(){
		var ${id!}image = ${id!}ue.getDialog("insertimage");
		${id!}image.open();
    });
    <? } ?>
	<? } ?>
</script>