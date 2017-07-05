<? if(operate! != 'view'){ ?>
  <div id="picker">${i18n("operate.upload")}</div>
<? } ?>
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
            $("#name").val(response.fileName);
            $("#url").val(response.url);
            $("#size").val(response.size);
    	});
	});
	
	function removeFile(){
		$("#file_").remove();
		//重置容器
		uploader.reset();
		//重置表单
		$("form")[0].reset();
	}
</script>