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

    deleteProfile(userId) {
        const token = window.sessionStorage.getItem('token');
        fetch(`http://localhost:8080/users/${userId}`, {
            method: "DELETE",
            headers: {
                "Content-type": "application/json; charset=UTF-8",
                "Authorization": `Bearer ${token}`
            }
        }).then(response => {
            if (response.ok) {
                window.sessionStorage.setItem('username', null);
                window.location.href = '/';
            } else if (response.status === 400) {
                //     add error handling here
            }
        });
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
                response.json().then(responseJson => {
                    this.showErrors(responseJson['message']);
                });
            }
        });
    }

    showErrors = (message) => {
        const errors = message.split(";");
        console.log(message);
        const errorList = document.getElementById('errorList');
        while (errorList.firstChild) {
            errorList.removeChild(errorList.firstChild);
        }
        for (const str of errors) {
            const li = document.createElement('div');
            li.className = 'error-list-login';
            li.innerText = str;
            errorList.appendChild(li);
        }
        errorList.style.display = 'block';
        errorList.removeChild(errorList.lastChild);
    }

    toPasswordChange() {
        window.location.href = '/myPage/password';
    }


    render() {
        return <div className="div-form">
            <UserInfoComponent/>
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{right: '1810px'}}>Back
            </button>
            <button id="button_delete_profile" className="button-login" onClick={() => {
                if (window.confirm('Are you sure you want to delete your profile?') === true) {
                    this.deleteProfile(window.sessionStorage.getItem('currentUserId'));
                } else {
                    window.location.reload();
                }
            }} style={{right: '1610px'}}>Delete profile
            </button>
            <div className="login-form">
                <div className="login-subtitle-edit">Edit your data:</div>
                <div className="input-container ic2">
                    <input id="username" className="input" type="text" placeholder=" " value={this.state.username}
                           onChange={e => this.setState({username: e.target.value})}/>
                    <div className="cut"></div>
                    <label form="email" className="placeholder">Username</label>
                </div>
                <div className="input-container ic2">
                    <input id="email" className="input" type="text" placeholder=" " value={this.state.email}
                           onChange={e => this.setState({email: e.target.value})}/>
                    <div className="cut cut-short"></div>
                    <label form="email" className="placeholder">Email</label>
                </div>
                <button type="text" className="login-submit" onClick={this.toPasswordChange}>Change password</button>
                <div id="errorList" className="error-list" style={{display: 'none', marginTop: '80px', marginLeft: '-33px', marginBottom: '-20px'}}/>
                <button type="text" className="login-submit" onClick={this.sendData}>Submit</button>
            </div>
        </div>
    }
}

export default UpdateUserComponent