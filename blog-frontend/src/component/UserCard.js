import '../user.css';
import React from "react";


class UserCard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: props.username,
            email: props.email,
            userId: props.userId,
            currentUserId: props.currentUserId,
            role: props.role,
            subs: []
        }
    }

    componentDidMount() {
        const token = window.sessionStorage.getItem('token');
        fetch(`http://localhost:8080/subscriptions/subscriber?userSubscriberId=${this.state.currentUserId}`, {
            method: "GET",
            headers: {
                "Content-type": "application/json; charset=UTF-8",
                "Authorization": `Bearer ${token}`
            }
        }).then(response => {
            if (response.ok) {
                response.json().then(data => {
                    this.setState({
                        subs: data
                    });
                });
            } else if (response.status === 400) {
                //     add error handling here
            }
        });
    }

    DeleteButton(props) {
        return (
            <button id="delete_btn" className={props.className} style={props.style} onClick={() => {
                const token = window.sessionStorage.getItem('token');
                fetch(`http://localhost:8080/users/${props.userId}`, {
                    method: "DELETE",
                    headers: {
                        "Content-type": "application/json; charset=UTF-8",
                        "Authorization": `Bearer ${token}`
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else if (response.status === 400) {
                        //     add error handling here
                    }
                });
            }}>
                {props.text}
            </button>);
    }

    FollowButton(props) {
        return (
            <button id="follow_btn" className={props.className} style={props.style} onClick={() => {
                const token = window.sessionStorage.getItem('token');
                fetch(`http://localhost:8080/subscriptions/${props.userId}`, {
                    method: "POST",
                    headers: {
                        "Content-type": "application/json; charset=UTF-8",
                        "Authorization": `Bearer ${token}`
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else if (response.status === 400) {
                        //     add error handling here
                    }
                });
            }}>
                {props.text}
            </button>
        );
    }

    UnfollowButton(props) {
        return (
            <button id="unfollow_btn" className={props.className} style={props.style} onClick={() => {
                const token = window.sessionStorage.getItem('token');
                fetch(`http://localhost:8080/subscriptions/${props.userId}`, {
                    method: "DELETE",
                    headers: {
                        "Content-type": "application/json; charset=UTF-8",
                        "Authorization": `Bearer ${token}`
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else if (response.status === 400) {
                        //     add error handling here
                    }
                });
            }}>
                {props.text}
            </button>
        );
    }

    ChangePrivilegeButton(props) {
        return (
            <button id="admin_btn" className={props.className} style={props.style} onClick={() => {
                const token = window.sessionStorage.getItem('token');
                fetch(`http://localhost:8080/users/privilege/${props.userId}`, {
                    method: "PUT",
                    headers: {
                        "Content-type": "application/json; charset=UTF-8",
                        "Authorization": `Bearer ${token}`
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else if (response.status === 400) {
                        //     add error handling here
                    }
                });
            }}>
                {props.text}
            </button>);
    }

    render() {
        let deleteButton, updateButton, followButton;
        const none = {
            display: 'none',
        };
        if (this.state.subs.includes(this.props.userId)) {
            followButton = <this.UnfollowButton text="Unfollow" className="btn btn--primary" userId={this.state.userId}/>
        } else {
            if (window.sessionStorage.getItem('username') !== this.props.username) {
                followButton = <this.FollowButton text="Follow" className="btn btn--primary" userId={this.state.userId}/>
            } else {
                followButton = <this.FollowButton style={{display: 'none'}}/>
            }
        }
        if (window.sessionStorage.getItem('isAdmin') === 'true') {
            deleteButton = <this.DeleteButton text="Delete" className="delete-update-button" userId={this.props.userId}/>;
            if (this.state.role !== 'ADMIN') {
                updateButton = <this.ChangePrivilegeButton text="Change role" className="delete-update-button" userId={this.props.userId}/>;
            } else {
                updateButton = <this.ChangePrivilegeButton text="Change role" style={none}/>;
            }
        } else {
            deleteButton = <this.DeleteButton text="Delete" style={none}/>;
            updateButton = <this.ChangePrivilegeButton text="Change role" style={none}/>;
        }
        return (
            <article className="profile">
                <h2 className="profile-username">{this.state.username}</h2>
                <small className="profile-user-handle">{this.state.email}</small>
                <div className="profile-actions">
                    {followButton}
                    {deleteButton}
                    {updateButton}
                </div>
            </article>
        );
    }
}

export default UserCard