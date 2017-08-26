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