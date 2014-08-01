App.Group = DS.Model.extend({
	name: DS.attr('string'),
	maxUsers: DS.attr('number'),
	users: DS.hasMany('customer'),
	managers: DS.hasMany('customer')
});
