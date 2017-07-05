<a class="btn btn-sm btn-default select-export-${key!}" ><i class="fa fa-share-square-o mr5"></i>${i18n("operate.export")}</a>
  	
<div id="${key!}_export_dialog" style="display:none; min-height: 300px">
	<form id="exportform" method="post" action="${url}">
	</form>
</div>

<script type="text/javascript">
    $('a.select-export-${key!}').click(function(){
    	$.ajax({
    		//增加一个model属性，为了解决/问题，例如office/alias这样带/号的，在class属性里面不允许出现
    		url : '${ctx!}${isEmpty(model!)?"":"/"+model!}/${key!}/attrsselect',
    		type : 'post',
    		success : function(data){
    			var str = '';
    			str += '<input type="checkbox" name="attrsAll_export">全选<br>';
    			for(var i = 1; i < data.length -1; i++){
    				str += '<input type="checkbox" name="attrs" value="'
    					+ data[i].column_name + '" class="mr5">'
    					+ data[i].column_comment
    					+ '<br>';
    			}
    			$("#exportform").html(str);
    			
    			layer.open({
    	    	    type : 1,
    	    	    title : '选择字段',
    	    	    shade : 0.5,
    	    	    move : true,
    	    	    area : ['${width!"350px"}', ''],
    	    	    content : $("#${key!}_export_dialog"),
    	    	    btn : ['确定','取消'],
    	    	    yes : function(index, layero){
    	    	    	if($("input[name='attrs']:checked").length == 0){
    	    	    		$("#exportform").empty();
    	    	    		layer.close(index);
    	    	    		layer.alert('未选择字段!',{icon:0,title:'提示'});
    	    	    	}else{
							$("#exportform").submit(); 
							$("#exportform").empty();
	    	    	    	layer.close(index);
    	    	    	}
    	    	    },
    	    	    no : function(index){
    	    	    	$("#exportform").empty();
    	    	    	layer.close(index);
    	    	    }
    	    	});
    			
    			//全选\全不选
    			$("form[id='exportform']").find("input[name='attrsAll_export']").on("click",function(){
    				if($(this).prop("checked")){
    					$("form[id='exportform']").find("input[name='attrs']").prop("checked",true);
    				}else{
    					$("form[id='exportform']").find("input[name='attrs']").prop("checked",false);
    				}
    				
    			});
    			
    			$("form[id='exportform']").find("input[name='attrs']").on("click",function(){
    				//任何一个 非全部 选择 checkbox 没有选中，那么“全部” 设置为 非选中状态
    				var allChecked = true;
    				$("form[id='exportform']").find("input[name='attrs']").each(function(){
    					if(!$(this).prop("checked")){
    						allChecked = false;
    					}
    				});
    				$("form[id='exportform']").find("input[name='attrsAll_export']").prop("checked",allChecked);
    			});
    		}
    	});
    });
    
</script>