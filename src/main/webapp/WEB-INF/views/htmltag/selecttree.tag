<? if(operate! == 'view'){ ?>
  <label>${showValue!}</label>
<? }else{ ?>
<div class="layui-input-inline">
  <input type="hidden" id="${key!}_id" name="${isEmpty(model!)?'':model!+'.'}${key!}_id" value="${hideValue!}" />
  <input type="text" id="${key!}_name" name="${name!}" value="${showValue!}" class="${css!}" readonly="readonly" placeholder="${i18n('select')}" />
</div>
<div class="layui-input-inline">
  <a class="layui-btn fl select-${key!}"><i class="fa fa-search"></i></a>
</div>
<? } ?>
<script type="text/javascript">
    $('a.select-${key!}').click(function(){
    	$.ajax({
    		url : '${ctx!}/select/tree?url=${url!}&async=${async!}&key=${key!}&checked=${checked!}&chkStyle=${chkStyle!}&model=${model!}',
    		type : 'get',
    		success : function(data){
    			layer.open({
    	    	    type : 1,
    	    	    title : '${label!}选择',
    	    	    shade : 0.5,
    	    	    move : true,
    	    	    area : ['${width!"350px"}', ''],
    	    	    content : data,
    	    	    btn : ['确定','取消'],
    	    	    yes : function(index, layero){
    	    	    	var tree = $.fn.zTree.getZTreeObj("${key!}_tree");
    	    	    	var ids = [], names = [], nodes = [];
    	<? if (checked! == "true") { ?>
    	                nodes = tree.getCheckedNodes(true);
    	<? } else { ?>
    	                nodes = tree.getSelectedNodes();
    	<? } ?>
    	                for(var i=0; i<nodes.length; i++) {
    	                    ids.push(nodes[i].id);
    	                    names.push(nodes[i].name);
    	                }
    	                $("#${key!}_id").val(ids);
    	                $("#${key!}_name").val(names);
    	                $("#${key!}_name").focus();
    	    	    	layer.close(index);
    	    	    },
    	    	    no : function(index){
    	                $("#${key!}_id").val("");
    	                $("#${key!}_name").val("");
    	                $("#${key!}_name").focus();
    	    	    }
    	    	});
    		}
    	})
    });
</script>