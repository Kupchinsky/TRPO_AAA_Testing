// Store global values
var Root = React.createClass({
    getInitialState: function () {
        return {users: [], auth: [], actions: []};
    },
    componentDidMount: function () {
        $.getJSON("/ajax/user/")
            .done(function (data) {
                this.setState({users: data, auth: [], actions: []});
            }.bind(this));
    },
    handleUserSelected: function (userId) {
        $.getJSON("/ajax/authority/", {userId: userId})
            .done(function (data) {
                this.setState({auth: data, actions: [] });
            }.bind(this));
    },
    handleAuthSelected: function (authorityId) {
        $.getJSON("/ajax/activity/", {authorityId: authorityId})
            .done(function (data) {
                this.setState({activity: data});
            }.bind(this));
    },
    render: function () {
        return (
            <div>
                <Root.UserTable data={this.state.users} onUserSelected={this.handleUserSelected}/>
                <Root.AuthorityTable data={this.state.auth} onAuthSelected={this.handleAuthSelected}/>
                <Root.ActionTable data={this.state.actions}/>
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
                    return (<tr key={user.id} onClick={this.props.onUserSelected.bind(null, user.id)}>
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
        return (
            <table>
                <thead>
                <tr>
                    <th>Role</th>
                    <th>Site</th>
                </tr>
                </thead>
                <tbody>
                {this.props.data.map(function (auth) {
                    return (<tr key={auth.id} onClick={this.props.onAuthSelected.bind(null, auth.id)}>
                        <td>{auth.site}</td>
                        <td>{auth.role}</td>
                    </tr>);
                }.bind(this))}
                </tbody>
            </table>
        );
    }
});

Root.ActionTable = React.createClass({
    render: function () {
        return (
            <table>
                <thead>
                <tr>
                    <th>Login date</th>
                    <th>Logout date</th>
                    <th>Volume</th>
                </tr>
                </thead>
                <tbody>
                {this.props.data.map(function (activity) {
                    return (<tr key={activity.id}>
                        <td>{activity.loginDate}</td>
                        <td>{activity.logoutDate}</td>
                        <td>{activity.volume}</td>
                    </tr>);
                }.bind(this))}
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
