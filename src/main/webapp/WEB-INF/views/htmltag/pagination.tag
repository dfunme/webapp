

  <div class="pagination fl">
    <ul>
<?
	var pageNumber = page.pageNumber!1;   // 当前页
	var pageSize = page.pageSize!10;       // 页面行数，设置为“-1”表示不进行分页（分页无效）
	var totalPage = page.totalPage!0;     // 总页数
	var totalRow = page.totalRow!0;       // 总行数
    
	var first = 1;// 首页索引
	var last = (totalRow - (totalRow % pageSize)) / pageSize + first - 1;// 尾页索引
	var prev;// 上一页索引
	var next;// 下一页索引

	var firstPage;// 是否是第一页
	var lastPage;// 是否是最后一页

	var length = 8;// 显示页数
	var slider = 1;// 前后显示页面长度
	
	var pageSizeArray = [10,20,50];// 页面行数选项
	
	var funcName = funcName!"page";
	
	if (totalRow % pageSize != 0 || last == 0) {
		last++;
	}

	if (last < first) {
		last = first;
	}

	if (pageNumber <= 1) {
		pageNumber = first;
		firstPage = true;
	}

	if (pageNumber >= last) {
		pageNumber = last;
		lastPage = true;
	}

	if (pageNumber < last - 1) {
		next = pageNumber + 1;
	} else {
		next = last;
	}

	if (pageNumber > 1) {
		prev = pageNumber - 1;
	} else {
		prev = first;
	}

	if (pageNumber < first) {// 如果当前页小于首页
		pageNumber = first;
	}

	if (pageNumber > last) {// 如果当前页大于尾页
		pageNumber = last;
	}
   
	if (pageNumber == first) {// 如果是首页 
		printf("<li class=\"disabled\"><a href=\"javascript:\">");
	} else {
		printf("<li><a href=\"javascript:" + funcName + "(" + prev + "," + pageSize + ");\">");
	}
	printf("<i class=\"fa fa-angle-double-left mr5\"></i>上一页</a></li>\n");

	var begin = pageNumber - (length / 2);

	if (begin < first) {
		begin = first;
	}

	var end = begin + length - 1;

	if (end >= last) {
		end = last;
		begin = end - length + 1;
		if (begin < first) {
			begin = first;
		}
	}

	if (begin > first) {
		var i = first;
		while (i < first + slider && i < begin) {
			printf("<li><a href=\"javascript:" + funcName + "(" + i
					+ "," + pageSize + ");\">" + (i + 1 - first)
					+ "</a></li>\n");
			i++;
		}
		if (i < begin) {
			printf("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
		}
	}

	for (var i = begin; i <= end; i++) {
		if (i == pageNumber) {
			printf("<li class=\"active\"><a href=\"javascript:\">"
					+ (i + 1 - first) + "</a></li>\n");
		} else {
			printf("<li><a href=\"javascript:" + funcName + "(" + i
					+ "," + pageSize + ");\">" + (i + 1 - first)
					+ "</a></li>\n");
		}
	}

	if (last - end > slider) {
		printf("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
		end = last - slider;
	}

	for (var i = end + 1; i <= last; i++) {
		printf("<li><a href=\"javascript:" + funcName + "(" + i + ","
				+ pageSize + ");\">" + (i + 1 - first) + "</a></li>\n");
	}

	if (pageNumber == last) {
		printf("<li class=\"disabled\"><a href=\"javascript:\">");
	} else {
		printf("<li><a href=\"javascript:" + funcName + "(" + next + "," + pageSize + ");\">");
	}
	printf("下一页<i class=\"fa fa-angle-double-right ml5\"></i></a></li>\n");
	printf("</ul></div>");
	
    printf("<div class=\"currentpage fr\">当前第 ");
    printf("<input type=\"text\" class=\"pageno\" value=\"" + pageNumber
            + "\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)");
    printf(funcName + "(this.value," + pageSize + ");\" onclick=\"this.select();\" onblur=\"");
    printf(funcName + "(this.value," + pageSize + ");\"/> 页 / ");
    printf("<select style=\"width:60px; height:29px\" onchange=\"" + funcName + "(" + pageNumber
            + ",this.value);\">");
    for (var i = 0; i < pageSizeArray.~size; i++) {
        printf("<option value=\"" + pageSizeArray[i] + "\"");
        if (pageSize==pageSizeArray[i]) {
            printf(" selected");
        }
        printf(">" + pageSizeArray[i] + "</option>");
    }
    printf("</select> 条， 共 " +"<span>"+ totalRow +"</span>"+ " 条</div>");
?>
<script type="text/javascript">
	layui.laypage({
		cont: 'page1',
		pages: 100 , //总页数
		groups: 2 ,//连续显示分页数
		skin: '#1E9FFF', //皮肤
		skip: true //是否开启跳页
	});
	function page(n,s){
		$('input[name="pageSize"]').val(s);	
		//判断查询的页码是否大于总页数 
		if(n > '${page.totalPage!}'){
			var num = $(".pagination li[class='active'] a").html();
			$(".pageno").val(num);
			alert('输入页码不能大于总页数!');
		}else{
			if($('input[name="pageSize"]').val()!=s){
				$('input[name="pageNumber"]').val('1');
			}else{
				$('input[name="pageNumber"]').val(n);
			}
			$("#searchform").submit();
		}
	}
	$(document).ready(function(){
		if($("#orderBy").length > 0){
			var orderBy = $("#orderBy").val().split(" ");
			$(".table th.sort").each(function(){
				if ($(this).hasClass(orderBy[0])){
					orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
					$(this).html($(this).html()+" <i class=\"fa fa-arrow-"+ orderBy[1] +" mr5\"></i>");
				}
			});
	  		$(".table th.sort").click(function(){
	  			var order = $(this).attr("class").split(" ");
	  			var sort = $("#orderBy").val().split(" ");
	  			for(var i=0; i<order.length; i++){
	  				if (order[i] == "sort"){order = order[i+1]; break;}
	  			}
	  			if (order == sort[0]){
	  				sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
	  				$("#orderBy").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
	  			}else{
	  				$("#orderBy").val(order+" ASC");
	  			}
	  			$("#searchform").submit();
	  		});
		}
	});
</script>