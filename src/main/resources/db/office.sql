#namespace("office")
  #sql("paginate")
    select * from sys_office o where o.status = 1
  #end
#end