
select(function(it) {
	if(!it.has("name")){
		return false;
	}
	
	return true;
	
}).each(function(it) {
	var oldName = it.get("name");
	var newName;
	
	newName = oldName.toLowerCase();
	
	log('old name is ' + oldName + ' to ' + newName);
	it.set("name", newName)
}).selectInEditor();