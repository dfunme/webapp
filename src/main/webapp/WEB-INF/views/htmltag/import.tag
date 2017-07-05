<? if(operate! != 'view'){ ?>
  <div id="picker">${i18n("operate.upload")}</div>
<? } ?>
<script type="text/javascript" >
	var uploader;
	$(document).ready(function() {
		uploader = WebUploader.create({
    		auto: true,
    	    swf: '${ctx!}/static/plugins/webuploader/Uploader.swf',
    	    server: '${url!}',
    	    pick: '#picker',
    	    fileNumLimit : 1,
    	    resize: false
    	});
    	uploader.on('uploadSuccess', function(file, response){
    		console.log(response);
    		layer.alert(response.msg);
    	});
	});
</script>