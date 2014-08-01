App.User = DS.Model.extend({
	firstname: DS.attr('string'),
	lastname: DS.attr('string'),
	manager_id: DS.attr('number'),
	email: DS.attr('string'),
	birthDate: DS.attr('date'),
	isActive: DS.attr('boolean')
});
