<? if(type! == "image"){ ?>
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