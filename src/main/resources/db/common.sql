#namespace("common")
  #sql("findByParentIds")
    select * from #(table) where parent_ids like #para(parentIds)
  #end
  
  #sql("tree")
    select id, name, parent_id pid, (select 'true' from #(table) b where b.status = 1 and b.parent_id = a.id limit 1) isParent from #(table) a where a.status = 1 and a.parent_id = #para(parentId) order by id
  #end
#end