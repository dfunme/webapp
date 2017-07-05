<link rel="stylesheet" href="${ctx!}/static/plugins/cropper/css/cropper.min.css">
<link rel="stylesheet" href="${ctx!}/static/plugins/cropper/css/main.css">
<style>
a{ cursor: pointer;}
.avatar-view {
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
#fileContent p{
  text-align:center;
  max-width:220px;
}
#fileContent img{
  margin:10px;
}
#fileContent .picSpace{
    border: 1px solid #fff;
}
#fileContent .picSpace img{
  width:220px;
  max-width:220px;
  height:140px;
  max-heigth:140px;
}
#fileContent #Content2 .picSpace.active,#fileContent #Content2 .picSpace:hover{
    border: 1px solid #99d1ff;
    background-color:#cce8ff;
}
</style>
<!-- Content -->
<div id="crop-avatar">
  <!-- Current avatar -->
  <div class="avatar-view" title="点此上传图片">
    <img  data-ajax="no" onerror="this.style.display='none'" >
    <input type="hidden" id="${id!}" name="${name!}" value=""/>
  </div>
  <!-- Cropping modal -->
  <div class="modal fade" id="avatar-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="avatar-form" >
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title" id="avatar-modal-label">图片裁剪</h4>
          </div>
          <div class="modal-body">
            <div class="avatar-body">
              <!-- Upload image and data -->
              <div class="avatar-upload">
                <input type="hidden" class="avatar-src" name="avatar_src">
                <input type="hidden" class="avatar-data" name="avatar_data">
                <label for="avatarInput">上传本地图片</label>                
                <input type="file" class="avatar-input" id="avatarInput" name="avatar_file" style="width:200px;">
                <label class="remind" >请选择宽${width!'150'}px 高${height!'150'}px的图片</label>
                <a id="remind2" href="javascript:;">图片库</a>
              
              </div>
              <!-- Crop and preview -->
              <div class="row-fluid">
                <div class="span9">
                  <div class="avatar-wrapper"></div>
                </div>
                <div class="span3 avatar-preview-container">
                  <!-- span3 -->
                  <!-- 这里选择固定宽度 -->
                  <div class="avatar-preview preview-lg"></div>
             <!-- <div class="avatar-preview preview-md"></div> -->
             <!-- <div class="avatar-preview preview-sm"></div> -->
                </div>
              </div>
              <div class="row-fluid avatar-btns">
                <div class="span9">
                  <div class="btn-group">
                     <button type="button" style="display:none" class="avatar-load-serverImg"></button>
                    <button type="button" class="btn btn-primary" data-method="setDragMode" data-option="move" title="Move">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;setDragMode&quot;, &quot;move&quot;)">
                        <span class="fa fa-arrows"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="setDragMode" data-option="crop" title="Crop">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;setDragMode&quot;, &quot;crop&quot;)">
                        <span class="fa fa-crop"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="zoom" data-option="0.1" title="Zoom In">
                      <span class="docs-tooltip" data-toggle="tooltip" title="放大0.1倍">
                        <span class="fa fa-search-plus"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="zoom" data-option="-0.1" title="Zoom Out">
                      <span class="docs-tooltip" data-toggle="tooltip" title="缩小0.1倍">
                        <span class="fa fa-search-minus"></span>
                      </span>
                    </button>
                  </div>
                  <div class="btn-group">
                    <button type="button" class="btn btn-primary" data-method="move" data-option="-10" data-second-option="0" title="Move Left">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;move&quot;, -10, 0)">
                        <span class="fa fa-arrow-left"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="move" data-option="10" data-second-option="0" title="Move Right">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;move&quot;, 10, 0)">
                        <span class="fa fa-arrow-right"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="move" data-option="0" data-second-option="-10" title="Move Up">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;move&quot;, 0, -10)">
                        <span class="fa fa-arrow-up"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="move" data-option="0" data-second-option="10" title="Move Down">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;move&quot;, 0, 10)">
                        <span class="fa fa-arrow-down"></span>
                      </span>
                    </button>
                  </div>
                  <div class="btn-group">
                    <button type="button" class="btn btn-primary" data-method="rotate" data-option="-15" title="向左旋转">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;rotate&quot;, -45)">
                        <span class="fa fa-rotate-left"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="rotate" data-option="15" title="向右旋转">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;rotate&quot;, 45)">
                        <span class="fa fa-rotate-right"></span>
                      </span>
                    </button>
				    <button type="button" class="btn btn-primary" data-method="scaleX" data-option="-1" title="Flip Horizontal">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;scaleX&quot;, -1)">
                        <span class="fa fa-arrows-h"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="scaleY" data-option="-1" title="Flip Vertical">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;scaleY&quot;-1)">
                        <span class="fa fa-arrows-v"></span>
                      </span>
                    </button>
                  </div>
	              <div class="btn-group">
                    <button type="button" class="btn btn-primary" data-method="crop" title="Crop">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;crop&quot;)">
                        <span class="fa fa-check"></span>
                      </span>
                    </button>
<!-- 					<button type="button" class="btn btn-primary" data-method="clear" title="Clear">
			            <span class="docs-tooltip" data-toggle="tooltip" title="" data-original-title="cropper.clear()">
			              <span class="fa fa-remove"></span>
			            </span>
			        </button> -->
                    <button type="button" class="btn btn-primary" data-method="disable" title="Disable">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;disable&quot;)">
                        <span class="fa fa-lock"></span>
                      </span>
                    </button>
                    <button type="button" class="btn btn-primary" data-method="enable" title="Enable">
                      <span class="docs-tooltip" data-toggle="tooltip" title="$().cropper(&quot;enable&quot;)">
                        <span class="fa fa-unlock"></span>
                      </span>
                    </button>
                  </div>
                </div>
                <div class="span3">
                  <button type="button" class="btn btn-primary btn-block avatar-original-save" >原图上传</button>
                  <button type="button" class="btn btn-primary btn-block avatar-save" >完成</button>
                </div>
              </div>
            </div>
          </div>
        </div> 
      </div>
    </div>
  </div>
  <div class="loading" aria-label="Loading" role="img" tabindex="-1"></div>
</div> 


<div id="fileContent" class="none p10">
  <!-- <div class="pl10"><a id="btnBack">返回上一级</a></div> -->
  <div id="Content2"></div>
</div>

<script>
	/* CropAvatarConfig must be used before cropper/main.js loaded*/
	//这里用于配置 裁剪参数,当前仅支持一个页面出现一个裁剪框
	// disable 为true,则会阻止裁剪插件的初始化
	// source_pic 设置背景图 width\height 用于设置最终生成图片的尺寸，单位为px
   var pic="";
  		<?if(isEmpty(value!)){?>
  		pic="${ctx!}/static/img/placeholder.png";
  		<?}else{?>
  		pic="${ctx!}/file/download?url=${value!}"  			
  		<?}?>

	window.CropAvatarConfig = {
		el: "#crop-avatar",
		inputId: "#${id!'image'}", //inputId配置表单隐藏输入框的el,以#开头,后面紧跟Id,默认为image
		avatar: {
			source_pic:pic,
			width: "${width!'150'}",
			height: "${height!'150'}"
		},
		disable: false
	};
	
	var targetImgSrc = null; //TODO define target img src
	//图片库弹出框
	 $("#remind2").click(function(ids){ 
		 
		 load_fileList('${image!}','${name!}');
		 
		 layer.open({
			    type : 1,
			    title : '文件空间',
			    shade : 0.5,
			    move : true,
			    area : ['780px', '450px'],
			    content : $('#fileContent'),
			    btn : ['确定','取消'],
			    	
			   yes: function(index, layero) {			   
				   //TODO open img 
				   // xxx.open(targetImgSrc);
				  CropAvatarConfig.server_img_url = targetImgSrc;
				  layer.close(index);
				 
				  $('.avatar-load-serverImg').click();
				
			    }
			});
	 }); 
 
	 var arr = [];
 	//加载文件列表
 	function load_fileList(dir,order){
 		$.ajax({
    		url : '${ctx!}/file/manager?dir='+dir+'&order='+order,
    		type : 'post',   
    		success : function(file){		
    			if(file){
    				fileAnalyze(file)
    			}
			}
 		});
 	}
	 
 	//解析JSON格式的文件列表
 	function fileAnalyze(json){		
 		var str_html=''; 		
 		    str_html += backHTML();
 		var current_url = json.current_url;
		var list = json.file_list;
		 for(var i=0;i<(list.length);i++){
			 var filename = list[i].filename;
			 var is_dir = list[i].is_dir;
			 var has_file = list[i].has_file;
			 var filesize = list[i].filesize;
			 var is_photo = list[i].is_photo;
			 var filetype = list[i].filetype;
			 
			 var path = current_url+filename;
					 
			 
			 if(has_file==true && is_dir==true){
				 //创建文件夹		
				  str_html+='<div class="fl"><img src="${ctx!}/static/img/ico-file.png" onclick="load_fileList(\''+path+'\',\'\')"/><p>'+filename+'</p></div>'
			 }else if(is_photo==true){
				 //创建文件
				 var $src = current_url.substring(current_url.lastIndexOf('\//'),current_url.length)+filename;
				 str_html +="<div class='fl picSpace'><img src='${ctx!}/file/download?url="+$src+"'/> <p class='pb10'>"+filename+"</p></div>";				 
			 }
			 
		 }

		 arr.push(str_html);
	 	
		$('#Content2').html(str_html);
 	}
 	//返回上一级
	$("#Content2").on('click','#btnBack',function(e){
		  if (arr.length > 1) { 
				var a = arr[arr.length-2];
			 	arr.length = arr.length-1;
			 	if(2>arr.length>=1){
			      $('#Content2').append(arr);
			      $('#Content2').html(arr); 
			 	}else{
			 	  $('#Content2').append(a);
				  $('#Content2').html(a);
			 	}			 	
		  }			
	}); 
//返回上一级结束	
 	
 	
 	//back
   	function backHTML(){
   		return "<div class='pl10'><a id='btnBack'>返回上一级</a></div>";
   	}
 	
  

 	//图片选中状态
 	
 		 $("#fileContent #Content2").on('click','.picSpace',function(e){
 	        		$(this).addClass("active");
 			        $(this).siblings().removeClass("active");
 			        targetImgSrc =  $(this).find('img').attr("src") || '';
 		    });
	
 	
</script>

<script src="${ctx!}/static/plugins/cropper/js/cropper.min.js"></script>
<script src="${ctx!}/static/plugins/cropper/js/main.js"></script>