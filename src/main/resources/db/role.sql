#namespace("role")
  #sql("paginate")
    select * from sys_role r where r.status = 1
  #end
  
  #sql("list")
    select * from sys_role r where r.status = 1
    #if(user_id != null)
      and exists (select 1 from sys_user_role ua where ua.role_id = r.id and ua.user_id = #para(user_id))
    #end
  #end
#end