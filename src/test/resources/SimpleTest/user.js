App.User = DS.Model.extend({
	firstname: DS.attr('string'),
	lastname: DS.attr('string'),
	manager: DS.belongsTo('user'),
	email: DS.attr('string'),
	birthDate: DS.attr('date'),
	isActive: DS.attr('boolean')
});
