import React from "react";
import UserInfoComponent from "./UserInfoComponent";
import ApiClient from "../client/ApiClient";

class UpdateUserComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            role: "",
            username: "",
            password: ""
        }
    }

    componentDidMount() {
        const userId = window.sessionStorage.getItem('currentUserId')
        this.getUserInfo(userId).then(data => {
            this.setState({
                email: data.email,
                username: data.username,
                role: data.role,
                password: data.password
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
        const oldPassword = document.getElementById("old_password").value;
        const newPassword = document.getElementById("new_password").value;
        const userId = window.sessionStorage.getItem('currentUserId');
        const token = window.sessionStorage.getItem('token');
        fetch(`http://localhost:8080/users/password/${userId}`, {
            method: "PUT",
            body: JSON.stringify({
                currentPassword: oldPassword,
                newPassword: newPassword
            }),
            headers: {
                "Content-type": "application/json; charset=UTF-8",
                "Authorization": `Bearer ${token}`
            }
        }).then(response => {
            if (response.ok) {
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


    render() {
        return <div className="div-form">
            <UserInfoComponent/>
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{top: '70px'}}>Back
            </button>
            <div className="login-form">
                <div className="login-subtitle-edit">Enter your recent password:</div>
                <div className="input-container ic2">
                    <input id="old_password" className="input" type="text" placeholder=" "/>
                    <div className="cut cut-recent"></div>
                    <label form="old_password" className="placeholder">Recent</label>
                </div>
                <div id="div-new-pass" className="login-subtitle-edit">Enter your new
                    password:
                </div>
                <div id="div-new-pass-input" className="input-container ic2">
                    <input id="new_password" className="input" type="text" placeholder=" "/>
                    <div className="cut cut-new"></div>
                    <label form="old_password" className="placeholder">New</label>
                </div>
                <div id="errorList" className="error-list"
                     style={{display: 'none', marginTop: '100px', marginLeft: '-35px', marginBottom: '-20px'}}/>
                <button type="text" style={{marginTop: '10px'}} className="login-submit"
                        onClick={this.sendData}>Submit
                </button>
            </div>
        </div>
    }
}

export default UpdateUserComponent