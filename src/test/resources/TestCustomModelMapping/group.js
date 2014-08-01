App.Group = DS.Model.extend({
	name: DS.attr('string'),
	maxUsers: DS.attr('number'),
	users: DS.hasMany('user'),
	managers: DS.hasMany('user')
});
