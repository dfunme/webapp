#namespace("dict")
  #sql("list")
    select d.*, (select 'true' from sys_dict s where s.status = 1 and s.parent_id = d.id limit 1) isParent from sys_dict d where d.status = 1 and d.parent_id = ?
  #end
#end