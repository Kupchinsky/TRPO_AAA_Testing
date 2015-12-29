// Store global values
var Root = React.createClass({
	getInitialState: function () {
		return {users: [], auth: [], accounting: []};
	},
	componentDidMount: function () {
		$.getJSON("/ajax/user/")
			.done(function (data) {
				this.setState({users: data, auth: [], accounting: []});
			}.bind(this));
	},
	handleUserSelected: function (userId) {
		$.getJSON("/ajax/authority/", {userId: userId})
			.done(function (data) {
				this.setState({auth: data, accounting: [] });
			}.bind(this));
	},
	handleAuthSelected: function (authorityId) {
		$.getJSON("/ajax/activity/", {authorityId: authorityId})
			.done(function (data) {
				this.setState({accounting: data});
			}.bind(this));
	},
	render: function () {
		return (
			<div>
				<Root.UserTable data={this.state.users} onUserSelected={this.handleUserSelected}/>
				<Root.AuthorityTable data={this.state.auth} onAuthSelected={this.handleAuthSelected}/>
				<Root.AccountingTable data={this.state.accounting}/>
				<Root.Authenticate />
			</div>
		);
	}
});

Root.UserTable = React.createClass({
	render: function () {
		return (
			<table>
				<thead>
				<tr>
					<th>ID</th>
					<th>Login</th>
					<th>Person name</th>
				</tr>
				</thead>
				<tbody>
				{this.props.data.map(function (user) {
					return (<tr className="cl" key={user.databaseId} onClick={this.props.onUserSelected.bind(null, user.databaseId)}>
						<td>{user.databaseId}</td>
						<td>{user.userName}</td>
						<td>{user.personName}</td>
					</tr>);
				}.bind(this))}
				</tbody>
			</table>
		);
	}
});

Root.AuthorityTable = React.createClass({
	render: function () {
		var data;

		if (this.props.data.length != 0) {
			data = this.props.data.map(function (auth) {
					return (<tr className="cl" key={auth.databaseId} onClick={this.props.onAuthSelected.bind(null, auth.databaseId)}>
						<td>{auth.databaseId}</td>
						<td>{auth.role}</td>
						<td>{auth.resource.name}</td>
					</tr>);
				}.bind(this));
		} else {
			data = (
				<tr>
					<td colSpan="4">
						<strong>No authorities found</strong>
					</td>
				</tr>
			);
		}

		return (
			<table>
				<thead>
				<tr>
					<th>ID</th>
					<th>Role</th>
					<th>Resource</th>
				</tr>
				</thead>
				<tbody>
					{ data }
				</tbody>
			</table>
		);
	}
});

Root.AccountingTable = React.createClass({
	render: function () {
		var data;

		if (this.props.data.length != 0) {
			data = this.props.data.map(function (accounting) {
					return (<tr className="borders" key={accounting.databaseId}>
						<td>{accounting.accounting.databaseId}</td>
						<td>{accounting.accounting.loginDate}</td>
						<td>{accounting.accounting.logoutDate}</td>
						<td>{accounting.accounting.volume}</td>
					</tr>);
				}.bind(this));
		} else {
			data = (
				<tr>
					<td colSpan="4">
						<strong>No activities found</strong>
					</td>
				</tr>
			);
		}

		return (
			<table>
				<thead>
				<tr>
					<th>ID</th>
					<th>Login date</th>
					<th>Logout date</th>
					<th>Volume</th>
				</tr>
				</thead>
				<tbody>
					{ data }
				</tbody>
			</table>
		);
	}
});

Root.Authenticate = React.createClass({
	render: function () {
		return <span />;
	}
});

ReactDOM.render(
	<Root />,
	document.getElementById('container')
);
