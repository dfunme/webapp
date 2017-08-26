#namespace("role")
  #sql("paginate")
    select * from sys_role r where r.status = 1
  #end
  
  #sql("list")
    select * from sys_role r where status = 1
    #if(user_id != null)
      and exists (select 1 from sys_authority a where a.role_id = r.id and a.user_id = #(user_id))
    #end
  #end
#end