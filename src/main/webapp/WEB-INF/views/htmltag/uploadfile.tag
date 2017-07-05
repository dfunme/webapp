<a class="btn btn-sm btn-default select-upload-${key!}" ><i class="fa fa-import mr5"></i>${i18n("operate.import")}</a>

<div id="importBox" style="display:none">
	<form id="importForm" action="${action!}" method="post" enctype="multipart/form-data">
		<input id="uploadFile" name="file" type="file" style="width:240px"/>
		<? if (parentId! !=null) { ?>
			<input id="parentId" name="parentId" type="hidden" value="${parentId! }"/>
		<? }?>
	</form>
</div>

<div id="${key!}_upload_dialog" style="display:none; min-height: 300px">
	<form id="uploadform" method="post" action="${templateUrl}">
	</form>
</div>

<script type="text/javascript">
$('a.select-upload-${key!}').click(function(){
	layer.open({
	    type : 1,
	    title : '选择文件',
	    shade : 0.5,
	    move : true,
	    area : ['${width!"350px"}', ''],
	    content : $("#importBox"),
	    btn : ['导 入','下载模板','取 消'],
	    btn1 : function(index, layero){
	    	if($("#uploadFile").val() == null || $("#uploadFile").val() == ""){
	    		layer.close(index);
	    		layer.alert('没有选择文件!',{icon:0,title:'提示'});
			}else{
				$("#importForm").submit();
    	    	layer.close(index);
			}
	    },
	    btn2 : function(index, layero){
	    	layer.close(index);
	    	template();
	    },
		btn3 : function(index, layero){
    	    layer.close(index);
		},
		success : function(index, layero){
			$(".layui-layer-btn").append("<br><span style='color:red;font-size:smaller'>(上传的文件必须包含默认模板中的字段！)</span>");
		}
	});
});

function template() {
	$.ajax({
		//增加一个model属性，为了解决/问题，例如office/alias这样带/号的，在class属性里面不允许出现
		url : '${ctx!}${isEmpty(model!)?"":"/"+model!}/${key!}/attrsselect',
		type : 'post',
		success : function(data){
			var str = '';
			str += '<input type="checkbox" name="attrsAll_import">全选<br>';
			for(var i = 1; i < data.length - 1; i++){
				str += '<input type="checkbox" name="attrs" value="'
					+ data[i].column_name + '"';
					
				if(data[i].is_nullable == 'NO'){
					str += ' id="star-' + i + '" ';
					str += ' checked';
				}
				str	+= '>'
					+ data[i].column_description;
				if(data[i].is_nullable == 'NO'){
					str += '<span style="color:red">  *</span>';
				}
				str += '<br>';
			}
			$("#uploadform").html(str);
			
			layer.open({
	    	    type : 1,
	    	    title : '选择字段',
	    	    shade : 0.5,
	    	    move : true,
	    	    area : ['${width!"350px"}', ''],
	    	    content : $("#${key!}_upload_dialog"),
	    	    btn : ['确定','取消'],
	    	    yes : function(i, o){
	    	    	var allStarChecked = true;
	    	    	if($("input[name='attrs']:checked").length == 0){
	    	    		if($(".layui-layer-btn span").length > 0){
	    	    			$(".layui-layer-btn span").html("(未选择字段!)");
	    	    		}else{
	    	    		 	$(".layui-layer-btn").append("<br><span style='color:red;font-size:small'>(未选择字段!)</span>");
	    	    		}
	    	    	}else{
	    	    		$("#uploadform input[id^='star']").each(function(){
	    	    			if(!$(this).prop("checked")){
	    	    				allStarChecked = false;
	    					}
	    	    		});
	    	    		if(allStarChecked){
							$("#uploadform").submit();
							$("#uploadform").empty();
	    	    	    	layer.close(i);
	    	    		}else{
	    	    			if($(".layui-layer-btn span").length > 0){
	    	    				$(".layui-layer-btn span").html("(带*号为必勾选项！)");
	    	    			}else{
		    	    			$(".layui-layer-btn").append("<br><span style='color:red;font-size:small'>(带*号为必勾选项！)</span>");
	    	    				
	    	    			}
	    	    		}
	    	    	}
	    	    },
	    	    no : function(i){
	    	    	$("#uploadform").empty();
	    	    	layer.close(i);
	    	    }
	    	});
			
			//全选\全不选
			$("form[id='uploadform']").find("input[name='attrsAll_import']").on("click",function(){
				if($(this).prop("checked")){
					$("form[id='uploadform']").find("input[name='attrs']").prop("checked",true);
				}else{
					$("form[id='uploadform']").find("input[name='attrs']").prop("checked",false);
				}
				
			});
			
			$("form[id='uploadform']").find("input[name='attrs']").on("click",function(){
				//任何一个 非全部 选择 checkbox 没有选中，那么“全部” 设置为 非选中状态
				var allChecked = true;
				$("form[id='uploadform']").find("input[name='attrs']").each(function(){
					if(!$(this).prop("checked")){
						allChecked = false;
					}
				});
				$("form[id='uploadform']").find("input[name='attrsAll_import']").prop("checked",allChecked);
			});
		}
	});
}

</script>