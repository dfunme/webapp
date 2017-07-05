<? if(operate! != 'view'){ ?>
  <div id="picker">${i18n("operate.upload")}</div>
<? } ?>
  <div id="${id!}file">
  <? if(!isEmpty(value!)){ ?>
    <input type="hidden" id="${id!}" name="${name!}" value="${value!}">
    <a class="bluefont" href="${ctx!}/file/download?url=${value!}">${value!}</a>
    <? if(operate! != 'view'){ ?>
    <a class="btn btn-sm" href="javascript:remove('${id!}')">删除</a>
    <? } ?>
  <? } ?>
  </div>
<script type="text/javascript" >
	var uploader;
	$(document).ready(function() {
		uploader = WebUploader.create({
			auto: true,
			swf: '${ctx!}/static/plugins/webuploader/Uploader.swf',
			server: '${ctx!}/file/upload',
			pick: '#picker',
			fileNumLimit : 1,
			resize: false
		});
		uploader.on('uploadSuccess', function(file, response){
			var html = '<input type="hidden" id="${id!}" name="${name!}" value="'+response.url
				+'" /><a class="bluefont" href="${ctx!}/file/download?url='+response.url+'">'+response.url
				+'</a><a class="btn btn-sm" href="javascript:remove(\'${id!}\')">删除</a>';
			$("#${id!}file").append(html);
    	});
	});
	
    function remove(id){
    	$('#'+id).val('');
    	$('#'+id+'file').html('');
    }
</script>