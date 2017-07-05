#namespace("common")
  #sql("findById")
    select * from ? where id = ?
  #end
  #sql("findByParentIds")
    select * from ? where parent_ids like ?
  #end
  #sql("tree")
    select id, name, parent_id pid, (select 'true' from ? b where b.del_flag = 0 and b.parent_id = a.id limit 1) isParent from ? a where a.del_flag = 0 and a.parent_id = ? order by id
  #end
#end

#namespace("office")
  #sql("paginate")
    select * from sys_user u where u.del_flag = 0 and u.username = ?
  #end
  #sql("list")
    from sys_user where del_flag = 0
  #end
#end

#namespace("role")
  #sql("paginate")
    select * from sys_user u where u.del_flag = 0 and u.username = ?
  #end
  #sql("list")
    select * from sys_role r where del_flag = 0
    #if(user_id != null)
      and exist (select 1 from sys_user_role ur where ur.role_id = r.id and ur.user_id = #(user_id))
    #end
  #end
#end

#namespace("user")
  #sql("login")
    select * from sys_user u where u.del_flag = 0 and u.username = ?
  #end
  #sql("paginate")
    select * from sys_user where del_flag = 0
  #end
#end