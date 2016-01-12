var update = React.addons.update;

// Store global values
var Root = React.createClass({
	getInitialState: function () {
		return {users: [], auth: [], accounting: [], result: "", currentAuth: null};
	},
	componentDidMount: function () {
		$.getJSON("/ajax/user/")
			.done(function (data) {
				this.setState(update(this.state, {$merge: {users: data, auth: [], accounting: []}}));
			}.bind(this));
	},
	handleUserSelected: function (userId) {
		$.getJSON("/ajax/authority/user/" + userId)
			.done(function (data) {
				this.setState(update(this.state, {$merge: {auth: data, accounting: [] }}));
			}.bind(this));
	},
	handleAuthSelected: function (authorityId) {
		$.getJSON("/ajax/activity/authority/" + authorityId)
			.done(function (data) {
				this.setState(update(this.state, {$merge: {accounting: data, currentAuth: authorityId}}));
			}.bind(this));
	},
    handleAddActivity: function (data) {
        $.ajax({
            type: 'POST',
            url: '/ajax/activity/',
            data: JSON.stringify(data),
            contentType: 'application/json'
        }).done(function (data) {
            this.setState(update(this.state, {$merge: {result: data}}));

            if (this.state.currentAuth) {
                this.handleAuthSelected(this.state.currentAuth);
            }
        }.bind(this));
    },
	render: function () {
		return (
			<div>
				<Root.UserTable data={this.state.users} onUserSelected={this.handleUserSelected}/>
				<Root.AuthorityTable data={this.state.auth} onAuthSelected={this.handleAuthSelected}/>
				<Root.AccountingTable data={this.state.accounting}/>
				<Root.Authenticate data={this.state.result} onActivityAdded={this.handleAddActivity} />
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
    getInitialState: function () {
        return {login: null, pass: null, res: null, role: null, ds: null, de: null, vol: null};
    },
    handleOnChange: function (e) {
        this.state[e.target.name] = e.target.value;
    },
    handleSubmit: function (e) {
        e.preventDefault();
        this.props.onActivityAdded(this.state);
    },
    render: function () {
        return (
            <div>
                <span id="result">{this.props.data}</span>
                <form onSubmit={this.handleSubmit}>
                    <div><label target="login">-login</label>
                        <input id="login" name="login" onChange={this.handleOnChange}/></div>
                    <div><label target="pass">-pass</label>
                        <input id="pass" name="pass" onChange={this.handleOnChange}/></div>
                    <div><label target="role">-role</label>
                        <select id="role" name="role" onChange={this.handleOnChange}>
                            <option value="">Select role</option>
                            <option value="READ">READ</option>
                            <option value="WRITE">WRITE</option>
                            <option value="EXECUTE">EXECUTE</option>
                        </select>
                    </div>
                    <div><label target="res">-res</label>
                        <input id="res" name="res" onChange={this.handleOnChange}/></div>
                    <div><label target="ds">-ds (Date start)</label>
                        <input id="ds" name="ds" onChange={this.handleOnChange}/></div>
                    <div><label target="de">-de (Date end)</label>
                        <input id="de" name="de" onChange={this.handleOnChange}/></div>
                    <div><label target="vol">-vol (Volume)</label>
                        <input id="vol" name="vol" onChange={this.handleOnChange}/></div>
                    <div><input type="submit"/></div>
                </form>
            </div>
        );
    }
});

ReactDOM.render(
	<Root />,
	document.getElementById('container')
);
