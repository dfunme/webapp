#namespace("user")
  #sql("login")
    select * from sys_user u where u.status = 1 and u.username = ?
  #end
  
  #sql("paginate")
    select * from sys_user where status = 1
  #end
#end