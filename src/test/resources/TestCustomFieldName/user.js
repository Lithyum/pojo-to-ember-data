App.User = DS.Model.extend({
	prenom: DS.attr('string'),
	nom: DS.attr('string'),
	manager: DS.belongsTo('user'),
	email: DS.attr('string'),
	birthDate: DS.attr('date'),
	isActive: DS.attr('boolean')
});
