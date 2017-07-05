<div class="input-append">
<? if(operate! == 'view'){ ?>
  <label>${showValue!}</label>
<? }else{ ?>
  <input type="hidden" id="${key!}_id" name="${model!}.${key!}_id" value="${hideValue!}" />
  <input type="text" id="${key!}_name" name="${key!}_name" value="${showValue!}" class="${css!}" readonly="readonly" placeholder="${i18n('select')}" />
  <span class="add-on">
    <a class="select-${key!}"><i class="fa fa-search"></i></a>
  </span>
<? } ?>
</div>
<script type="text/javascript">
    $('a.select-${key!}').click(function(){
    	$.ajax({
    		url : '${ctx!}/select/table?dataUrl=${dataUrl}&key=${key!}&selecttype=${selecttype!}&label=${label!}&columns=${columns!}&model=${model!}',
    		type : 'get',
    		success : function(data){
		    	layer.open({
		    	    type : 1,
		    	    title : '${label!}选择',
		    	    shade : 0.5,
		    	    move : true,
		    	    area : ['750px', '450px'],
		    	    content : data,
		    	    btn : ['确定','取消'],
		    	    yes : function(index, layero){
		    	    	var names = $("input[name='${key!}_id']:checked").val();
		    	    	var ids = $("input[name='${key!}_id']:checked").attr("data-id");
		    	        $("#${key!}_id").val(ids);
		    	        $("#${key!}_name").val(names);
		    	    	layer.close(index);
		    	    },
		    	    no : function(index){
		    	        $("#${key!}_id").val("");
		    	        $("#${key!}_name").val("");
		    	    }
		    	});
    		}
    	});
    });
</script>