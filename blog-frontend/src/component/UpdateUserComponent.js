import React from "react";
import UserInfoComponent from "./UserInfoComponent";
import ApiClient from "../client/ApiClient";

class UpdateUserComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            email: ""
        }
    }

    componentDidMount() {
        const userId = window.sessionStorage.getItem('currentUserId')
        this.getUserInfo(userId).then(data => {
            this.setState({
                username: data.username,
                email: data.email
            });
        });
    }


    getUserInfo(id): Promise {
        return ApiClient.oneUser(id).then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("User-service: error response");
                }
            }
        )
    }


    sendData = () => {
        const username = document.getElementById("username").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const userId = window.sessionStorage.getItem('currentUserId');
        const role = (window.sessionStorage.getItem('isAdmin') == null || window.sessionStorage.getItem('isAdmin') === 'false') ? 'DEFAULT' : 'ADMIN';
        const token = window.sessionStorage.getItem('token');
        fetch(`http://localhost:8080/users/${userId}`, {
            method: "PUT",
            body: JSON.stringify({
                username: username,
                email: email,
                password: password,
                id: userId,
                role: role
            }),
            headers: {
                "Content-type": "application/json; charset=UTF-8",
                "Authorization": `Bearer ${token}`
            }
        }).then(response => {
            if (response.ok) {
                window.sessionStorage.setItem('username', username);
                window.location.href = '/myPage';
            } else if (response.status === 400) {
                //     add error handling here
            }
        });
    }


    render() {
        return <div className="div-form">
            <UserInfoComponent/>
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{right: '1810px'}}>Back
            </button>
            <div className="login-form">
                <div className="login-subtitle-edit">Edit your data:</div>
                <div className="input-container ic2">
                    <input id="username" className="input" type="text" placeholder=" " value={this.state.username} onChange={e => this.setState({ username: e.target.value })}/>
                    <div className="cut"></div>
                    <label form="email" className="placeholder">Username</label>
                </div>
                <div className="input-container ic2">
                    <input id="email" className="input" type="text" placeholder=" " value={this.state.email} onChange={e => this.setState({ email: e.target.value })}/>
                    <div className="cut cut-short"></div>
                    <label form="email" className="placeholder">Email</label>
                </div>
                <div className="input-container ic2">
                    <input id="password" className="input" type="password" placeholder=" "/>
                    <div className="cut"></div>
                    <label form="password" className="placeholder">Password</label>
                </div>
                <button type="text" className="login-submit" onClick={this.sendData}>Submit</button>
            </div>
        </div>
    }
}

export default UpdateUserComponent