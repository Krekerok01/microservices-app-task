import '../user.css';
import React from "react";


class UserCard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: props.username,
            email: props.email,
            userId: props.userId,
            role: props.role
        }
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
        let deleteButton, updateButton;
        const none = {
            display: 'none',
        };
        if (window.sessionStorage.getItem('isAdmin') === 'true') {
            deleteButton =
                <this.DeleteButton text="Delete" className="delete-update-button" userId={this.props.userId}/>;
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
                    <button className="btn btn--primary">Follow</button>
                    {deleteButton}
                    {updateButton}
                </div>
            </article>
        );
    }
}

export default UserCard