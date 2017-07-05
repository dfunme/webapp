<? if(operate! == 'view'){ ?>
  <label class="control-value">
  <? if(isEmpty(dictList!)){ ?>
  ${value!}
  <? } else { ?>
  ${dict(value!,dictList!,i18n('other'))}
  <? } ?>
  </label>
<? }else{ ?>
  <? if(type! == "text"){ ?>
  <input type="text" id="${id!}" name="${name!}" value="${value!}" class="${css!}" <? if(!isEmpty(vmodel!)){ ?>v-model="${id!}"<? } ?> placeholder="" ${option!}/>
  <? } else if(type! == "date"){ ?>
  <input type="text" id="${id!}" name="${name!}" value="${value!}" onclick="${onclick!}" class="${css!}">
  <? } else if(type! == "textarea"){ ?>
  <textarea id="${id!}" name="${name!}" class="${css!}" placeholder="${i18n('select')}" ${option!}>${value!}</textarea>
  <? } else if(type! == "select"){ ?>
  <select id="${id!}" name="${name!}" onselect="${onselect!}" onchange="${onchange!}" class="${css!}">
    <option value="">${i18n('select')}</option>
    <? for(item in list!){ ?>
      <? if(key! == "id"){ ?>
      <option value="${item.id!}" ${item.id! + "" == value! ?'selected':''}>${item.name!}</option>
      <? } else if(key! == "code"){ ?>
      <option value="${item.code!}" ${item.code! + "" == value! ?'selected':''}>${item.name!}</option>
      <? } else if(key! == "value"){ ?>
      <option value="${item.value!}" ${item.value! + "" == value! ?'selected':''}>${item.name!}</option>
      <? } else { ?>
      <option value="${item.name!}" ${value! == item.name!?'selected':''}>${item.name!}</option>
      <? } ?>    
    <? } ?>
    <? for(item in dictList(dictList!)){ ?>
      <? if(key! == "id"){ ?>
      <option value="${item.id!}" ${item.id! + "" == value! ?'selected':''}>${item.name!}</option>
      <? } else if(key! == "code"){ ?>
      <option value="${item.code!}" ${item.code! + "" == value! ?'selected':''}>${item.name!}</option>
      <? } else if(key! == "value"){ ?>
      <option value="${item.value!}" ${item.value! + "" == value! ?'selected':''}>${item.name!}</option>
      <? } else { ?>
      <option value="${item.name!}" ${value! == item.name!?'selected':''}>${item.name!}</option>
      <? } ?>    
    <? } ?>
  </select>
  <? } else { ?>
    <? for(item in list!){ ?>
      <? if(key! == "id"){ ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.id!}" class="${css!}" title="${item.name!}" ${value! == item.id!?'checked':''}>
      <? } else if(key! == "code"){ ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.code!}" class="${css!}" title="${item.name!}" ${value! == item.code!?'checked':''}>
      <? } else if(key! == "value"){ ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.value!}" class="${css!}" title="${item.name!}" ${value! == item.value!?'checked':''}>
      <? } else { ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.name!}" class="${css!}" title="${item.name!}" ${value! == item.name!?'checked':''}>
      <? } ?>
    <? } ?>
    <? for(item in dictList(dictList!)){ ?>
      <? if(key! == "id"){ ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.id!}" class="${css!}" title="${item.name!}" ${value! == item.id!?'checked':''}>
      <? } else if(key! == "code"){ ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.code!}" class="${css!}" title="${item.name!}" ${value! == item.code!?'checked':''}>
      <? } else if(key! == "value"){ ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.value!}" class="${css!}" title="${item.name!}" ${value! == item.value!?'checked':''}>
      <? } else { ?>
      <input type="${type!}" id="${id!}" name="${name!}" value="${item.name!}" class="${css!}" title="${item.name!}" ${value! == item.name!?'checked':''}>
      <? } ?>
    <? } ?>
  <? } ?>
<? } ?>